import axiosInstance from "@/core/api/axiosInstance";
import { authService } from "@/core/auth/services/authService";

interface Metric {
  name: string;
  value: number;
  timestamp: string;
  tags?: Record<string, string>;
}

/**
 * Loại metrics liên quan đến xác thực
 */
export enum AuthMetricType {
  LOGIN_SUCCESS = 'auth.login.success',
  LOGIN_FAILURE = 'auth.login.failure',
  LOGOUT = 'auth.logout',
  TOKEN_REFRESH = 'auth.token.refresh',
  TOKEN_VALIDATION = 'auth.token.validation',
  SESSION_EXPIRED = 'auth.session.expired',
  PERMISSION_DENIED = 'auth.permission.denied',
}

interface AuthMetric extends Metric {
  type: 'auth';
  userId?: string;
  sessionId?: string;
}

// Định nghĩa interface cho tham số của trackAuthMetric
interface AuthMetricParams {
  type: 'auth';
  name: string;
  value: number;
  userId?: string;
  sessionId?: string;
  tags?: Record<string, string>;
}

interface RequestMetric {
  type: string;
  userId?: string;
  url: string;
  method: string;
  statusCode?: number;
  durationMs: number;
  timestamp: string;
  success: boolean;
  errorMessage?: string;
}

/**
 * Service theo dõi metrics cho hệ thống xác thực
 * Giúp phân tích hiệu suất và phát hiện vấn đề bảo mật
 */
class MetricsService {
  private static instance: MetricsService;
  private metricsQueue: AuthMetric[] = [];
  private readonly BATCH_SIZE = 10;
  private readonly FLUSH_INTERVAL = 60000; // 1 minute
  private flushTimer: NodeJS.Timeout | null = null;
  private queue: RequestMetric[] = [];
  private isSending = false;
  private readonly MAX_QUEUE_SIZE = 50;
  private readonly BATCH_SEND_INTERVAL = 60000; // 60 seconds (increased from 30s)
  private readonly MIN_BATCH_SEND_INTERVAL = 30000; // Minimum 30 seconds between sends (increased from 5s)
  private readonly IGNORED_ENDPOINTS = [
    '/metrics/request', // Ignore metrics about metrics
    '/health', // Ignore health checks
    '/actuator/'
  ];
  private lastSendTime = 0;
  private sendTimeoutId: NodeJS.Timeout | null = null;
  private isAppActive = true; // Flag to track if app is in foreground

  private constructor() {
    this.startFlushTimer();
    // Schedule regular flush of metrics
    this.setupBatchInterval();
    
    // Listen for visibility change to reduce metrics when app is in background
    if (typeof document !== 'undefined') {
      document.addEventListener('visibilitychange', this.handleVisibilityChange);
      window.addEventListener('beforeunload', this.handleBeforeUnload);
    }
  }

  static getInstance(): MetricsService {
    if (!MetricsService.instance) {
      MetricsService.instance = new MetricsService();
    }
    return MetricsService.instance;
  }

  private startFlushTimer() {
    this.flushTimer = setInterval(() => {
      this.flushMetrics();
    }, this.FLUSH_INTERVAL);
  }

  /**
   * Tạo device fingerprint đơn giản cho metrics
   * Không sử dụng thông tin nhạy cảm
   */
  private getSimpleDeviceFingerprint(): Record<string, string> {
    return {
      userAgent: navigator.userAgent,
      language: navigator.language,
      platform: navigator.platform,
      screenResolution: `${window.screen.width}x${window.screen.height}`,
      timezone: Intl.DateTimeFormat().resolvedOptions().timeZone,
    };
  }

  /**
   * Theo dõi metric xác thực
   * @param metric - Thông tin metric cần theo dõi
   */
  trackAuthMetric(metric: AuthMetricParams) {
    const enrichedMetric: AuthMetric = {
      ...metric,
      timestamp: new Date().toISOString(),
      tags: {
        ...this.getSimpleDeviceFingerprint(),
        url: window.location.href,
        ...(metric.tags || {}),
      },
    };

    this.metricsQueue.push(enrichedMetric);

    if (this.metricsQueue.length >= this.BATCH_SIZE) {
      this.flushMetrics();
    }
  }

  /**
   * Theo dõi sự kiện đăng nhập thành công
   * @param userId - ID người dùng
   */
  trackLoginSuccess(userId: string) {
    this.trackAuthMetric({
      type: 'auth',
      name: AuthMetricType.LOGIN_SUCCESS,
      value: 1,
      userId,
      sessionId: crypto.randomUUID(),
    });
  }

  /**
   * Theo dõi sự kiện đăng nhập thất bại
   * @param errorCode - Mã lỗi (nếu có)
   */
  trackLoginFailure(errorCode?: string) {
    this.trackAuthMetric({
      type: 'auth',
      name: AuthMetricType.LOGIN_FAILURE,
      value: 1,
      tags: {
        errorCode: errorCode || 'unknown',
      },
    });
  }

  /**
   * Theo dõi sự kiện đăng xuất
   * @param userId - ID người dùng
   * @param reason - Lý do đăng xuất
   */
  trackLogout(userId?: string, reason?: string) {
    this.trackAuthMetric({
      type: 'auth',
      name: AuthMetricType.LOGOUT,
      value: 1,
      userId,
      tags: {
        reason: reason || 'user_initiated',
      },
    });
  }

  /**
   * Theo dõi sự kiện làm mới token
   * @param userId - ID người dùng
   * @param success - Thành công hay thất bại
   */
  trackTokenRefresh(userId: string, success: boolean) {
    this.trackAuthMetric({
      type: 'auth',
      name: AuthMetricType.TOKEN_REFRESH,
      value: success ? 1 : 0,
      userId,
    });
  }

  /**
   * Theo dõi sự kiện xác thực token
   * @param userId - ID người dùng
   * @param success - Thành công hay thất bại
   */
  trackTokenValidation(userId: string | undefined, success: boolean) {
    this.trackAuthMetric({
      type: 'auth',
      name: AuthMetricType.TOKEN_VALIDATION,
      value: success ? 1 : 0,
      userId,
    });
  }

  /**
   * Theo dõi sự kiện phiên hết hạn
   * @param userId - ID người dùng
   */
  trackSessionExpired(userId?: string) {
    this.trackAuthMetric({
      type: 'auth',
      name: AuthMetricType.SESSION_EXPIRED,
      value: 1,
      userId,
    });
  }

  /**
   * Theo dõi sự kiện từ chối quyền truy cập
   * @param userId - ID người dùng
   * @param resource - Tài nguyên bị từ chối
   * @param requiredPermission - Quyền yêu cầu
   */
  trackPermissionDenied(userId: string, resource: string, requiredPermission?: string) {
    this.trackAuthMetric({
      type: 'auth',
      name: AuthMetricType.PERMISSION_DENIED,
      value: 1,
      userId,
      tags: {
        resource,
        requiredPermission: requiredPermission || 'unknown',
      },
    });
  }

  /**
   * Track a request metric with filtering
   */
  public trackRequest(
    url: string,
    method: string,
    durationMs: number,
    statusCode?: number,
    success = true,
    errorMessage?: string
  ): void {
    // Ignore certain endpoints that generate too many metrics
    if (this.shouldIgnoreMetric(url)) {
      return;
    }
    
    // Don't track excessive metrics when app is in background
    if (!this.isAppActive && !errorMessage && success) {
      // Only track errors when app is in background
      return;
    }

    const metric: RequestMetric = {
      type: "api",
      userId: authService.getTokenClaims()?.sub,
      url,
      method,
      statusCode,
      durationMs,
      timestamp: new Date().toISOString(),
      success,
      errorMessage
    };

    this.queueMetric(metric);
  }

  /**
   * Track a page view
   */
  public trackPageView(path: string, durationMs: number): void {
    const metric: RequestMetric = {
      type: "page",
      userId: authService.getTokenClaims()?.sub,
      url: path,
      method: "GET",
      durationMs,
      timestamp: new Date().toISOString(),
      success: true
    };

    this.queueMetric(metric);
  }

  /**
   * Queue a metric for sending
   */
  private queueMetric(metric: RequestMetric): void {
    // Avoid duplicating very similar metrics in a short time period
    const similarMetric = this.findSimilarRecentMetric(metric);
    if (similarMetric) {
      return;
    }
    
    this.queue.push(metric);
    
    // If queue gets too large, send immediately
    if (this.queue.length >= this.MAX_QUEUE_SIZE) {
      this.sendQueuedMetrics();
    }
  }

  /**
   * Find a similar metric that was recently queued
   */
  private findSimilarRecentMetric(metric: RequestMetric): boolean {
    // Look at the last few metrics to find similar ones
    const recentMetrics = this.queue.slice(-5);
    return recentMetrics.some(existingMetric => 
      existingMetric.url === metric.url && 
      existingMetric.method === metric.method &&
      Math.abs(Date.now() - new Date(existingMetric.timestamp).getTime()) < 2000 // Within 2 seconds
    );
  }

  /**
   * Send queued metrics to the server
   */
  private sendQueuedMetrics(isSync: boolean = false): void {
    if (this.isSending || this.queue.length === 0) {
      return;
    }
    
    // Implement throttling - don't send more often than the minimum interval
    const now = Date.now();
    if (!isSync && now - this.lastSendTime < this.MIN_BATCH_SEND_INTERVAL) {
      // Already scheduled to send later?
      if (!this.sendTimeoutId) {
        this.sendTimeoutId = setTimeout(() => {
          this.sendTimeoutId = null;
          this.sendQueuedMetrics();
        }, this.MIN_BATCH_SEND_INTERVAL - (now - this.lastSendTime));
      }
      return;
    }
    
    this.lastSendTime = now;
    this.isSending = true;
    
    // Batch all available metrics instead of just some
    const metrics = [...this.queue];
    this.queue = [];

    // Create a unique identifier for this batch to prevent duplicate tracking
    const batchId = `${Date.now()}-${Math.random().toString(36).substring(2, 9)}`;
    
    // Send metrics to the server
    if (isSync) {
      // Use synchronous request for beforeunload event
      const xhr = new XMLHttpRequest();
      xhr.open("POST", `${axiosInstance.defaults.baseURL}/metrics/request`, false);
      xhr.setRequestHeader("Content-Type", "application/json");
      xhr.setRequestHeader("X-Metrics-Batch-ID", batchId);
      xhr.send(JSON.stringify({
        batchId,
        metrics: metrics
      }));
    } else {
      // Use asynchronous request with batching
      axiosInstance.post("/metrics/request", {
        batchId,
        metrics: metrics
      })
        .catch(() => {
          // Silent failure - don't affect user experience for metrics
          console.debug("Failed to send metrics");
        })
        .finally(() => {
          this.isSending = false;
        });
    }
  }

  /**
   * Gửi metrics đã thu thập đến backend
   */
  private async flushMetrics() {
    if (this.metricsQueue.length === 0) return;

    const metricsToSend = [...this.metricsQueue];
    this.metricsQueue = [];

    try {
      // Sử dụng cùng một endpoint với sendQueuedMetrics
      const batchId = `${Date.now()}-${Math.random().toString(36).substring(2, 9)}`;
      
      await axiosInstance.post("/metrics/request", {
        batchId,
        metrics: metricsToSend
      });
    } catch (error) {
      console.error('Failed to send metrics:', error);
      // Retry failed metrics
      this.metricsQueue = [...metricsToSend, ...this.metricsQueue];
    }
  }

  /**
   * Clean up event listeners on destroy
   */
  destroy() {
    if (this.flushTimer) {
      clearInterval(this.flushTimer);
      this.flushTimer = null;
    }
    
    if (this.sendTimeoutId) {
      clearTimeout(this.sendTimeoutId);
      this.sendTimeoutId = null;
    }
    
    if (typeof document !== 'undefined') {
      document.removeEventListener('visibilitychange', this.handleVisibilityChange);
      window.removeEventListener('beforeunload', this.handleBeforeUnload);
    }
    
    this.flushMetrics();
  }

  /**
   * Handle visibility change to reduce metrics when app is in background
   */
  private handleVisibilityChange = () => {
    if (document.visibilityState === 'visible') {
      this.isAppActive = true;
    } else {
      this.isAppActive = false;
      // Send any pending metrics when app goes to background
      if (this.queue.length > 0) {
        this.sendQueuedMetrics();
      }
    }
  };

  /**
   * Handle beforeunload event to send metrics before page unload
   */
  private handleBeforeUnload = () => {
    if (this.queue.length > 0) {
      this.sendQueuedMetrics(true);
    }
  };

  /**
   * Set up interval to send batched metrics
   */
  private setupBatchInterval() {
    // Clear any existing interval
    if (this.sendTimeoutId) {
      clearTimeout(this.sendTimeoutId);
      this.sendTimeoutId = null;
    }
    
    // Schedule sending metrics at regular intervals
    this.sendTimeoutId = setTimeout(() => {
      if (this.queue.length > 0) {
        this.sendQueuedMetrics();
      }
      this.setupBatchInterval();
    }, this.BATCH_SEND_INTERVAL);
  }

  /**
   * Check if we should ignore metrics for this URL
   */
  private shouldIgnoreMetric(url: string): boolean {
    return this.IGNORED_ENDPOINTS.some(endpoint => url.includes(endpoint));
  }
}

export const metricsService = MetricsService.getInstance(); 