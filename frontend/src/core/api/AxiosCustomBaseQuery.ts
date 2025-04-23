import type { BaseQueryFn } from '@reduxjs/toolkit/query';
import type { AxiosInstance, AxiosRequestConfig } from 'axios';
import { toast } from '@/shared/hooks/use-toast';
import { authService } from '@/core/auth/services/authService';

// Define API error types enum
export enum ApiErrorType {
  AUTH = 'auth_error',
  PERMISSION = 'permission_error',
  RATE_LIMIT = 'rate_limit_error',
  SERVER = 'server_error',
  NETWORK = 'network_error',
  VALIDATION = 'validation_error',
  TIMEOUT = 'timeout_error',
  UNKNOWN = 'unknown_error'
}

// Define error response types
interface ValidationErrors {
  [key: string]: string[];
}

/**
 * Standard error response interface from API
 */
export interface ApiErrorResponse {
  message: string;
  errors?: ValidationErrors;
  status?: number;
  requiresAuth?: boolean;
  requiredPermission?: string;
  code?: string;
}

/**
 * Standard error object for internal error handling
 */
export interface ApiError {
  type: ApiErrorType;
  status: number | string;
  message: string;
  details?: Record<string, any>;
  originalError?: any;
}

/**
 * Type for handling Axios errors
 */
interface ErrorAxiosType {
  response?: {
    data: ApiErrorResponse;
    status: number;
    statusText: string;
    headers: Record<string, string>;
  };
  message: string;
  code?: string;
  isAxiosError: boolean;
  isAuthError?: boolean;
  isRateLimitError?: boolean;
  request?: any;
}

// Keep track of auth errors to prevent duplicate toasts
let authErrorShown = false;
const resetAuthErrorFlag = () => {
  setTimeout(() => {
    authErrorShown = false;
  }, 5000); // Reset after 5 seconds
};

/**
 * Standard request options for all API calls
 */
export interface ApiRequestOptions {
  url: string;
  method: AxiosRequestConfig['method'];
  data?: AxiosRequestConfig['data'];
  params?: AxiosRequestConfig['params'];
  headers?: AxiosRequestConfig['headers'];
  skipAuthError?: boolean;
}

/**
 * Create a base query function using Axios for RTK Query
 */
export const axiosBaseQuery =
  (
    { axiosInstance }: { axiosInstance: AxiosInstance }
  ): BaseQueryFn<
    ApiRequestOptions,
    unknown,
    ApiError
  > =>
  async ({ url, method, data, params, headers, skipAuthError = false }) => {
    try {
      const result = await axiosInstance({
        url,
        method,
        data,
        params,
        headers,
      });
      
      return { data: result.data };
    } catch (axiosError) {
      const err = axiosError as ErrorAxiosType;
      let errorMessage = 'An error occurred';

      // Handle rate limit errors
      if (err.isRateLimitError || (err.response && err.response.status === 429)) {
        const waitTime = err.response?.headers?.['retry-after'] || '60';
        errorMessage = `Rate limit exceeded. Please try again in ${waitTime} seconds.`;
        
        if (!skipAuthError) {
          toast({
            title: "Rate Limit Exceeded",
            description: errorMessage,
            variant: "destructive",
          });
        }
            return {
          error: {
            type: ApiErrorType.RATE_LIMIT,
            status: 'RATE_LIMITED',
            message: errorMessage,
            details: { 
              retryAfter: parseInt(waitTime)
            }
          },
        };
      }

      // Handle custom rejection due to missing token
      if (err.message === 'Authentication required but no token available' || err.isAuthError) {
        return {
          error: {
            type: ApiErrorType.AUTH,
            status: 'UNAUTHORIZED',
            message: 'Authentication required',
            details: { requiresAuth: true }
          },
        };
      }

      // Handle auth errors
      if (err.isAuthError || (err.response && err.response.status === 401)) {
        if (!skipAuthError && !authErrorShown) {
          authErrorShown = true;
          toast({
            title: "Authentication Error",
            description: "Please sign in again to continue.",
            variant: "destructive",
          });
          resetAuthErrorFlag();
        }
          return {
          error: {
            type: ApiErrorType.AUTH,
            status: 'UNAUTHENTICATED',
            message: 'Authentication failed',
            details: { requiresAuth: true }
          },
        };
      }

      if (err.response) {
        // The request was made and the server responded with a status code
        // that falls out of the range of 2xx
        errorMessage = err.response.data.message || err.message;
        
        // Show toast for specific error types
        if (err.response.status === 403) {
          toast({
            title: "Access Denied",
            description: "You don't have permission to perform this action.",
            variant: "destructive",
          });
        } else if (err.response.status === 429) {
          toast({
            title: "Too Many Requests",
            description: "Please wait a moment before trying again.",
            variant: "destructive",
          });
        }        
        // Standard response error
        const errorType = err.response.status >= 500 
          ? ApiErrorType.SERVER 
          : err.response.status === 400 
            ? ApiErrorType.VALIDATION 
            : ApiErrorType.UNKNOWN;
            
        return {
          error: {
            type: errorType,
            status: err.response.status,
            message: errorMessage,
            details: err.response.data,
            originalError: err
          },
        };
      } else if (err.code === 'ECONNABORTED') {
        // The request was made but no response was received
        errorMessage = 'Request timeout. Please try again.';
        toast({
          title: "Connection Timeout",
          description: errorMessage,
          variant: "destructive",
        });
        
        return {
          error: {
            type: ApiErrorType.TIMEOUT,
            status: 'TIMEOUT',
            message: errorMessage,
            details: { timeout: true },
            originalError: err
          },
        };
      } else {
        // Something happened in setting up the request that triggered an Error
        errorMessage = 'Network error. Please check your connection.';
        toast({
          title: "Network Error",
          description: errorMessage,
          variant: "destructive",
        });
        
        return {
          error: {
            type: ApiErrorType.NETWORK,
            status: 'NETWORK_ERROR',
            message: errorMessage,
            details: { network: true },
            originalError: err
          },
        };
      }
    }
  };
