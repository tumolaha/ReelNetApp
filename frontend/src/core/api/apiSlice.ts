import { createApi, retry } from "@reduxjs/toolkit/query/react";
import { axiosBaseQuery } from "./AxiosCustomBaseQuery";
import axiosInstance from "./axiosInstance";
import { ApiErrorType, ApiError } from "./AxiosCustomBaseQuery";

/**
 * Cấu hình retry cho các endpoints
 */
export const defaultRetryConfig = {
  maxRetries: 2,
  retryDelay: (attempt: number) => Math.min(1000 * Math.pow(2, attempt), 5000), // Exponential backoff với giới hạn 5s
};

/**
 * Tạo một retry query để tự động thử lại các request thất bại
 */
const baseQueryWithRetry = retry(
  axiosBaseQuery({ axiosInstance }),
  defaultRetryConfig
);

/**
 * API Slice cơ sở của ứng dụng - không chứa endpoints cụ thể
 * Được sử dụng như nền tảng chung cho các API slice khác
 */
export const apiSlice = createApi({
  reducerPath: "api",
  baseQuery: baseQueryWithRetry,
  tagTypes: [
    // Core tags
    "User",
    "Profile",
    "Settings",

    // Feature tags
    "VocabularySet",
    "Vocabulary",
    "VocabularyCategory",
    "Learning",
    "Statistics",
  ],
  keepUnusedDataFor: 300, // 5 minutes
  refetchOnMountOrArgChange: 30, // Refetch after 30 seconds
  refetchOnFocus: false,
  refetchOnReconnect: true,
  endpoints: (builder) => ({

  }),
});

/**
 * Helper để xử lý lỗi chung từ các API calls
 * @param err Lỗi bắt được từ API
 */
export const handleApiError = (err: any): ApiError => {
  // If error is already in the standard format
  if (err.error?.type && Object.values(ApiErrorType).includes(err.error.type)) {
    return err.error as ApiError;
  }

  // Nếu không có lỗi cụ thể
  if (!err.error) {
    return {
      type: ApiErrorType.NETWORK,
      status: "NETWORK_ERROR",
      message: "Could not connect to server",
      details: { network: true },
      originalError: err,
    };
  }

  // Lỗi xác thực
  if (err.error.status === "UNAUTHENTICATED" || err.error.status === 401) {
    return {
      type: ApiErrorType.AUTH,
      status: err.error.status || 401,
      message: err.error.message || "Authentication required",
      details: { requiresAuth: true },
      originalError: err,
    };
  }

  // Lỗi permission
  if (err.error.status === "FORBIDDEN" || err.error.status === 403) {
    return {
      type: ApiErrorType.PERMISSION,
      status: err.error.status || 403,
      message:
        err.error.message ||
        "You do not have permission to perform this action",
      details: { requiredPermission: err.error.details?.requiredPermission },
      originalError: err,
    };
  }

  // Lỗi rate limit
  if (err.error.status === "RATE_LIMITED" || err.error.status === 429) {
    return {
      type: ApiErrorType.RATE_LIMIT,
      status: err.error.status || 429,
      message: err.error.message || "Too many requests. Please try again later",
      details: {
        retryAfter: err.error.details?.retryAfter || 60,
      },
      originalError: err,
    };
  }

  // Lỗi timeout
  if (err.error.status === "TIMEOUT") {
    return {
      type: ApiErrorType.TIMEOUT,
      status: "TIMEOUT",
      message: err.error.message || "Request timed out",
      details: err.error.details,
      originalError: err,
    };
  }

  // Lỗi server
  if (typeof err.error.status === "number" && err.error.status >= 500) {
    return {
      type: ApiErrorType.SERVER,
      status: err.error.status,
      message: err.error.message || "Server error. Please try again later",
      details: err.error.details,
      originalError: err,
    };
  }

  // Lỗi validation
  if (err.error.status === 400) {
    return {
      type: ApiErrorType.VALIDATION,
      status: 400,
      message: err.error.message || "Validation error",
      details: err.error.details,
      originalError: err,
    };
  }

  // Lỗi khác
  return {
    type: ApiErrorType.UNKNOWN,
    status: err.error.status || "UNKNOWN_ERROR",
    message: err.error.message || "Unknown error",
    details: err.error.details,
    originalError: err,
  };
};

/**
 * Trích xuất dữ liệu và lỗi từ các RTK Query hook
 * @param queryResult Kết quả từ hook RTK Query
 */
export const extractQueryResult = <T>(queryResult: any) => {
  const { data, error, isLoading, isFetching, isError, isSuccess, refetch } =
    queryResult;
  let apiError: ApiError | null = null;
  if (isError) {
    apiError = handleApiError(error);
  }

  return {
    data: data?.data as T,
    metadata: data?.metadata,
    status: data?.status,
    error: apiError,
    isLoading,
    isFetching,
    isError,
    isSuccess,
    refetch,
  };
};

// Export các selectors
export const { endpoints, reducerPath, reducer, middleware } = apiSlice;
