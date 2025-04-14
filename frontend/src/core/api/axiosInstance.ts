import axios, {
  AxiosError,
  AxiosRequestConfig,
  AxiosResponse,
  InternalAxiosRequestConfig,
} from "axios";
import { authService } from "@/core/auth/services/authService";
import { addSecurityHeaders } from "@/core/middleware/securityHeaders";
import { rateLimitService } from "@/core/services/rateLimitService";
import { metricsService } from "@/core/services/metricsService";
import { createAxiosParams } from "./queryParams";

// Extend AxiosError interface to include custom properties
interface CustomAxiosError extends AxiosError {
  isAuthError?: boolean;
  isRateLimitError?: boolean;
}

// Custom error for request interceptor
interface RequestError {
  message: string;
  config: any;
  isAuthError?: boolean;
  isRateLimitError?: boolean;
}

const baseURL = "/api/v1";

// Create axios instance
const axiosInstance = axios.create({
  baseURL: baseURL,
  headers: {
    "Content-Type": "application/json",
    'Accept': 'application/json',
  },
  withCredentials: true,
  timeout: 10000, // 10 seconds
  // Add paramsSerializer to handle complex query parameters
  paramsSerializer: (params) => {
    // Use our custom createAxiosParams function to handle all special cases
    const processedParams = createAxiosParams(params);
    
    // Convert to URLSearchParams
    const searchParams = new URLSearchParams();
    
    // Process each parameter
    Object.entries(processedParams).forEach(([key, value]) => {
      if (value === undefined || value === null) {
        return;
      }
      
      // Handle arrays
      if (Array.isArray(value)) {
        value.forEach((item) => {
          if (item !== undefined && item !== null) {
            searchParams.append(key, String(item));
          }
        });
      } else {
        // Handle simple values
        searchParams.append(key, String(value));
      }
    });
    
    return searchParams.toString();
  }
});

// Keep track of pending requests that need authentication
const pendingAuthRequests: Record<string, boolean> = {};

// Check if request requires authentication
const requiresAuth = (url: string): boolean => {
  // Define public endpoints
  const publicEndpoints = [
    '/auth/login',
    '/auth/status',
    '/public/',
    '/metrics/request'
  ];
  
  // Check if URL matches any public endpoint
  return !publicEndpoints.some(endpoint => url.includes(endpoint));
};

// Track request timing
const startRequestTimer = (): () => number => {
  const start = performance.now();
  return () => Math.round(performance.now() - start);
};

// Request interceptor
axiosInstance.interceptors.request.use(
  async (config: InternalAxiosRequestConfig) => {
    // Add timing function to config for performance tracking
    const getElapsedTime = startRequestTimer();
    config.headers.set('X-Request-Start', Date.now().toString());
    
    // Add metadata for tracking
    const originalConfig = { ...config };
    
    try {
      // Add security headers
      config = addSecurityHeaders(config) as InternalAxiosRequestConfig;

      // Check if request requires authentication
      const url = config.url || "";
      const needsAuth = requiresAuth(url);
      
      // Apply rate limiting for auth endpoints
      if (url.includes("/auth/") && !url.includes("/validate") && !url.includes("/status")) {
        const endpoint = url.split("?")[0];
        const userId = authService.getTokenClaims()?.sub;

        if (!rateLimitService.checkRateLimit(endpoint, userId)) {
          const waitTime = rateLimitService.getRemainingWaitTime(endpoint, userId);
          const error: RequestError = {
            message: `Rate limit exceeded. Please try again in ${Math.ceil(waitTime / 1000)} seconds.`,
            config: originalConfig,
            isRateLimitError: true,
          };
          throw error;
        }
      }

      // Handle authentication
      if (needsAuth) {
        const token = authService.getToken();
        
        // If token is missing but we have claims, try to recover
        if (!token && authService.getTokenClaims() && !authService.isTokenExpired()) {
          console.debug("Token missing but claims exist - possible localStorage issue");
          
          // Create recovery entry in metrics
          metricsService.trackRequest(
            url,
            config.method?.toUpperCase() || "GET",
            0,
            undefined,
            false,
            "Token missing but claims exist"
          );
        }
        
        if (!token) {
          // Track auth failure
          const userId = authService.getTokenClaims()?.sub;
          metricsService.trackTokenValidation(userId || "unknown", false);
          
          const error: RequestError = {
            message: "Authentication required but no token available",
            config: originalConfig,
            isAuthError: true,
          };
          throw error;
        }

        // Add token to request
        config.headers.set("Authorization", `Bearer ${token}`);
        
        // Track successful token usage
        const userId = authService.getTokenClaims()?.sub;
        if (userId) {
          metricsService.trackTokenValidation(userId, true);
        }
      }

      return config;
    } catch (error: any) {
      // Track failed request
      metricsService.trackRequest(
        config.url || "unknown",
        config.method?.toUpperCase() || "GET",
        getElapsedTime(),
        undefined,
        false,
        error.message || "Request preparation failed"
      );
      
      return Promise.reject(error);
    }
  }
);

// Response interceptor
axiosInstance.interceptors.response.use(
  (response: AxiosResponse) => {
    // Calculate request duration
    const requestStartTime = Number(response.config.headers?.['X-Request-Start'] || Date.now());
    const duration = Date.now() - requestStartTime;
    
    // Only track important requests with significant duration
    const url = response.config.url || "unknown";
    const shouldTrackSuccess = !isMetricsEndpoint(url) && (
      duration > 1000 || // Only track requests that take more than 1 second
      response.status < 200 || 
      response.status >= 300 // Always track non-successful responses
    );
    
    if (shouldTrackSuccess) {
      // Track successful request
      metricsService.trackRequest(
        url,
        response.config.method?.toUpperCase() || "GET",
        duration,
        response.status,
        true
      );
    }
    
    return response;
  },
  async (error: CustomAxiosError) => {
    // Always track errors, they're important
    
    // Calculate request duration
    const config = error.config || {} as AxiosRequestConfig;
    const requestStartTime = Number(
      config.headers && typeof config.headers === 'object' 
        ? (config.headers as Record<string, any>)['X-Request-Start'] 
        : Date.now()
    );
    const duration = Date.now() - requestStartTime;
    
    // Get request data
    const url = typeof config.url === 'string' ? config.url : "unknown";
    const method = typeof config.method === 'string' 
      ? config.method.toUpperCase() 
      : "GET";
    const status = error.response?.status;
    
    // Don't track metrics requests
    if (isMetricsEndpoint(url)) {
      return Promise.reject(error);
    }
    
    // Handle specific errors
    if (error.response) {
      // Server returned error response
      switch (status) {
        case 401: // Unauthorized
          // Handle token expiration
          if (requiresAuth(url)) {
            console.warn("Authentication error for", url);
            
            // Clear invalid token
            authService.clearToken();
            
            // Track auth failure
            const userId = authService.getTokenClaims()?.sub;
            if (userId) {
              metricsService.trackSessionExpired(userId);
            }
            
            // Return enhanced error
            return Promise.reject({
              ...error,
              isAuthError: true,
              message: "Authentication token expired or invalid"
            });
          }
          break;
          
        case 403: // Forbidden
          // Track permission denied
          const userId = authService.getTokenClaims()?.sub;
          if (userId) {
            const requiredPermission = error.response.headers 
              && typeof error.response.headers === 'object'
              ? (error.response.headers as Record<string, any>)['x-required-permission'] as string || 'unknown'
              : 'unknown';
              
            metricsService.trackPermissionDenied(
              userId,
              url,
              requiredPermission
            );
          }
          break;
          
        case 429: // Too Many Requests
          // Track rate limit error
          metricsService.trackRequest(
            url,
            method,
            duration,
            status,
            false,
            "Rate limit exceeded"
          );
          break;
      }
    } else if (error.request) {
      // Request made but no response received (network error)
      console.warn("Network error for", url);
    }
    
    // Track failed request (if not a special error type)
    if (!error.isAuthError && !error.isRateLimitError) {
      metricsService.trackRequest(
        url,
        method,
        duration,
        status,
        false,
        error.message || "Unknown error"
      );
    }
    
    return Promise.reject(error);
  }
);

/**
 * Check if a URL is a metrics endpoint (to avoid tracking metrics about metrics)
 */
function isMetricsEndpoint(url: string): boolean {
  return url.includes('/metrics/') || url.includes('/actuator/');
}

export default axiosInstance;
