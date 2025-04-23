import axios, {
  AxiosError,
  AxiosRequestConfig,
  AxiosResponse,
  InternalAxiosRequestConfig,
} from "axios";
import { authService } from "@/core/auth/services/authService";
import { addSecurityHeaders } from "@/core/middleware/securityHeaders";
import { createAxiosParams } from "./queryParams";

// Custom error interfaces
interface CustomAxiosError extends AxiosError {
  isAuthError?: boolean;
  isRateLimitError?: boolean;
}

interface RequestError {
  message: string;
  config: any;
  isAuthError?: boolean;
  isRateLimitError?: boolean;
}

// API configuration
const baseURL = "/api/v1";
const PUBLIC_ENDPOINTS = ['/auth/login', '/auth/status', '/public/'];

// Create axios instance with default configuration
const axiosInstance = axios.create({
  baseURL,
  headers: {
    "Content-Type": "application/json",
    'Accept': 'application/json',
  },
  withCredentials: true,
  timeout: 10000, // 10 seconds
  paramsSerializer: (params) => {
    const processedParams = createAxiosParams(params);
    const searchParams = new URLSearchParams();
    
    Object.entries(processedParams).forEach(([key, value]) => {
      if (value === undefined || value === null) return;
      
      if (Array.isArray(value)) {
        value.forEach(item => {
          if (item !== undefined && item !== null) {
            searchParams.append(key, String(item));
          }
        });
      } else {
        searchParams.append(key, String(value));
      }
    });
    
    return searchParams.toString();
  }
});

// Check if request requires authentication
const requiresAuth = (url: string): boolean => 
  !PUBLIC_ENDPOINTS.some(endpoint => url.includes(endpoint));

// Request interceptor
axiosInstance.interceptors.request.use(
  async (config: InternalAxiosRequestConfig) => {
    try {
      // Add security headers
      config = addSecurityHeaders(config) as InternalAxiosRequestConfig;
      
      // Handle authentication
      const url = config.url || "";
      if (requiresAuth(url)) {
        const token = authService.getToken();
        
        // Handle missing token
        if (!token) {
          if (authService.getTokenClaims() && !authService.isTokenExpired()) {
            console.debug("Token missing but claims exist - possible localStorage issue");
          }
          
          throw {
            message: "Authentication required but no token available",
            config,
            isAuthError: true,
          } as RequestError;
        }

        // Add token to request
        config.headers.set("Authorization", `Bearer ${token}`);
      }

      return config;
    } catch (error) {
      return Promise.reject(error);
    }
  },
  // Reject handler
  error => Promise.reject(error)
);

// Response interceptor
axiosInstance.interceptors.response.use(
  // Success handler - just pass the response through
  response => response,
  
  // Error handler
  (error: CustomAxiosError) => {
    const config = error.config || {} as AxiosRequestConfig;
    const url = typeof config.url === 'string' ? config.url : "unknown";
    
    // Handle auth errors (401)
    if (error.response?.status === 401 && requiresAuth(url)) {
      console.debug("Authentication error for:", url);
      authService.clearToken();
      
      // Enhance error with auth information
      return Promise.reject({
        ...error,
        isAuthError: true,
        message: "Authentication token expired or invalid"
      });
    }
    
    // Log network errors
    if (error.request && !error.response) {
      console.debug("Network error for:", url);
    }
    
    return Promise.reject(error);
  }
);

export default axiosInstance; 
