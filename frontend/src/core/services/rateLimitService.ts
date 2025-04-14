interface RateLimitConfig {
  tokensPerInterval: number;
  interval: number; // in milliseconds
  maxWaitTime?: number; // maximum time to wait for a token in milliseconds
}

/**
 * Service quản lý rate limiting cho các API calls
 * Giúp bảo vệ API khỏi các cuộc tấn công brute force
 * Đặc biệt hữu ích cho các endpoints xác thực
 */
class RateLimitService {
  private static instance: RateLimitService;
  private buckets: Map<string, {
    tokens: number;
    lastRefill: number;
  }> = new Map();
  private configs: Map<string, RateLimitConfig> = new Map();

  // Cấu hình mặc định cho các endpoints xác thực
  private readonly AUTH_ENDPOINTS = {
    login: { tokensPerInterval: 5, interval: 60000, maxWaitTime: 10000 }, // 5 requests per minute
    signup: { tokensPerInterval: 3, interval: 60000, maxWaitTime: 10000 }, // 3 requests per minute
    validateToken: { tokensPerInterval: 10, interval: 60000, maxWaitTime: 5000 }, // 10 requests per minute
    refreshToken: { tokensPerInterval: 5, interval: 60000, maxWaitTime: 5000 }, // 5 requests per minute
  };

  private constructor() {
    // Tự động cấu hình cho các endpoints xác thực
    this.configureAuthEndpoints();
  }

  static getInstance(): RateLimitService {
    if (!RateLimitService.instance) {
      RateLimitService.instance = new RateLimitService();
    }
    return RateLimitService.instance;
  }

  /**
   * Cấu hình mặc định cho các endpoints xác thực
   */
  private configureAuthEndpoints(): void {
    Object.entries(this.AUTH_ENDPOINTS).forEach(([endpoint, config]) => {
      this.configureEndpoint(`/auth/${endpoint}`, config);
    });
  }

  /**
   * Cấu hình rate limit cho một endpoint cụ thể
   */
  configureEndpoint(endpoint: string, config: RateLimitConfig): void {
    this.configs.set(endpoint, config);
  }

  /**
   * Chờ đợi token cho một endpoint
   * @param endpoint - Endpoint cần kiểm tra
   * @param userId - ID người dùng (nếu có) để rate limit theo người dùng
   * @returns Promise<boolean> - true nếu có thể tiếp tục, false nếu đã vượt quá rate limit
   */
  async waitForToken(endpoint: string, userId?: string): Promise<boolean> {
    // Tạo key duy nhất cho endpoint và user (nếu có)
    const bucketKey = userId ? `${endpoint}:${userId}` : endpoint;
    
    const config = this.configs.get(endpoint);
    if (!config) return true; // No rate limit configured

    const now = Date.now();
    let bucket = this.buckets.get(bucketKey);

    if (!bucket) {
      bucket = {
        tokens: config.tokensPerInterval,
        lastRefill: now,
      };
      this.buckets.set(bucketKey, bucket);
    }

    // Refill tokens
    const timePassed = now - bucket.lastRefill;
    const tokensToAdd = Math.floor(timePassed / config.interval) * config.tokensPerInterval;
    if (tokensToAdd > 0) {
      bucket.tokens = Math.min(config.tokensPerInterval, bucket.tokens + tokensToAdd);
      bucket.lastRefill = now - (timePassed % config.interval);
    }

    if (bucket.tokens > 0) {
      bucket.tokens--;
      return true;
    }

    // Check if we should wait or reject immediately
    const maxWaitTime = config.maxWaitTime || 0;
    if (maxWaitTime <= 0) {
      return false; // Don't wait, just reject
    }

    // Calculate wait time
    const waitTime = Math.min(
      config.interval - (now - bucket.lastRefill),
      maxWaitTime
    );

    // Wait for next token
    try {
      await new Promise(resolve => setTimeout(resolve, waitTime));
      
      // After waiting, check if we can get a token now
      bucket = this.buckets.get(bucketKey) || {
        tokens: config.tokensPerInterval,
        lastRefill: Date.now(),
      };
      
      if (bucket.tokens > 0) {
        bucket.tokens--;
        this.buckets.set(bucketKey, bucket);
        return true;
      }
      
      return false;
    } catch (error) {
      console.error('Error waiting for rate limit token:', error);
      return false;
    }
  }

  /**
   * Kiểm tra xem một endpoint có vượt quá rate limit không mà không chờ đợi
   * @param endpoint - Endpoint cần kiểm tra
   * @param userId - ID người dùng (nếu có)
   * @returns boolean - true nếu có thể tiếp tục, false nếu đã vượt quá rate limit
   */
  checkRateLimit(endpoint: string, userId?: string): boolean {
    const bucketKey = userId ? `${endpoint}:${userId}` : endpoint;
    const config = this.configs.get(endpoint);
    if (!config) return true; // No rate limit configured

    const now = Date.now();
    let bucket = this.buckets.get(bucketKey);

    if (!bucket) {
      return true; // First request, always allowed
    }

    // Refill tokens
    const timePassed = now - bucket.lastRefill;
    const tokensToAdd = Math.floor(timePassed / config.interval) * config.tokensPerInterval;
    if (tokensToAdd > 0) {
      bucket.tokens = Math.min(config.tokensPerInterval, bucket.tokens + tokensToAdd);
      bucket.lastRefill = now - (timePassed % config.interval);
      this.buckets.set(bucketKey, bucket);
    }

    return bucket.tokens > 0;
  }

  /**
   * Reset rate limit cho một endpoint cụ thể
   */
  reset(endpoint: string, userId?: string): void {
    if (userId) {
      this.buckets.delete(`${endpoint}:${userId}`);
    } else {
      // Reset all buckets for this endpoint
      for (const key of this.buckets.keys()) {
        if (key.startsWith(endpoint)) {
          this.buckets.delete(key);
        }
      }
    }
  }

  /**
   * Reset tất cả rate limits
   */
  resetAll(): void {
    this.buckets.clear();
  }

  /**
   * Lấy thời gian chờ còn lại cho một endpoint
   * @returns Số milliseconds cần chờ hoặc 0 nếu không cần chờ
   */
  getRemainingWaitTime(endpoint: string, userId?: string): number {
    const bucketKey = userId ? `${endpoint}:${userId}` : endpoint;
    const config = this.configs.get(endpoint);
    if (!config) return 0; // No rate limit configured

    const bucket = this.buckets.get(bucketKey);
    if (!bucket || bucket.tokens > 0) return 0;

    const now = Date.now();
    const timePassed = now - bucket.lastRefill;
    return Math.max(0, config.interval - timePassed);
  }
}

export const rateLimitService = RateLimitService.getInstance(); 