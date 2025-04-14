import { AxiosRequestConfig } from 'axios';
import { authService } from '@/core/auth/services/authService';

/**
 * Tạo device fingerprint đơn giản cho bảo mật
 * @returns Đối tượng chứa thông tin thiết bị
 */
const getSimpleDeviceFingerprint = () => {
  return {
    userAgent: navigator.userAgent,
    language: navigator.language,
    platform: navigator.platform,
    screenResolution: `${window.screen.width}x${window.screen.height}`,
    timezone: Intl.DateTimeFormat().resolvedOptions().timeZone,
  };
};

/**
 * Thêm các header bảo mật vào request
 * @param config - Cấu hình Axios request
 * @returns Cấu hình đã được bổ sung header bảo mật
 */
export const addSecurityHeaders = (config: AxiosRequestConfig): AxiosRequestConfig => {
  if (!config.headers) {
    config.headers = {};
  }

  // Basic security headers
  config.headers['X-Content-Type-Options'] = 'nosniff';
  config.headers['X-Frame-Options'] = 'DENY';
  config.headers['X-XSS-Protection'] = '1; mode=block';
  config.headers['Referrer-Policy'] = 'strict-origin-when-cross-origin';
  config.headers['Permissions-Policy'] = 'camera=(), microphone=(), geolocation=()';
  
  // Add device fingerprint
  config.headers['X-Device-Fingerprint'] = JSON.stringify(getSimpleDeviceFingerprint());
  
  // Add request tracking
  config.headers['X-Request-ID'] = crypto.randomUUID();
  config.headers['X-Request-Timestamp'] = Date.now().toString();
  
  // Add CSRF token for non-GET requests
  const method = config.method?.toUpperCase();
  if (method && method !== 'GET' && method !== 'HEAD') {
    const csrfToken = document.querySelector('meta[name="csrf-token"]')?.getAttribute('content');
    if (csrfToken) {
      config.headers['X-CSRF-Token'] = csrfToken;
    }
  }

  // Add Auth0 specific headers if token is available
  const token = authService.getToken();
  if (token) {
    // Extract token claims if available
    try {
      const claims = authService.getTokenClaims();
      if (claims) {
        // Add user ID for tracking
        if (claims.sub) {
          config.headers['X-User-ID'] = claims.sub;
        }
        
        // Add tenant ID if available (for multi-tenant applications)
        if (claims.tenant_id) {
          config.headers['X-Tenant-ID'] = claims.tenant_id;
        }
        
        // Add organization if available
        if (claims.org_id) {
          config.headers['X-Organization-ID'] = claims.org_id;
        }
      }
    } catch (error) {
      console.error('Error extracting token claims for headers:', error);
    }
  }

  return config;
};

/**
 * Thêm các header bảo mật cho Auth0 vào response
 * Sử dụng cho các middleware Express hoặc Next.js
 * @param headers - Headers hiện tại
 * @returns Headers đã được bổ sung
 */
export const addSecurityResponseHeaders = (headers: Record<string, string> = {}): Record<string, string> => {
  // Security headers
  headers['X-Content-Type-Options'] = 'nosniff';
  headers['X-Frame-Options'] = 'DENY';
  headers['X-XSS-Protection'] = '1; mode=block';
  headers['Referrer-Policy'] = 'strict-origin-when-cross-origin';
  headers['Permissions-Policy'] = 'camera=(), microphone=(), geolocation=()';
  
  // Content Security Policy
  headers['Content-Security-Policy'] = [
    "default-src 'self'",
    // Allow Auth0 domains
    `connect-src 'self' *.auth0.com https://auth0.com`,
    // Allow Auth0 scripts
    `script-src 'self' 'unsafe-inline' *.auth0.com`,
    // Allow Auth0 frames
    `frame-src 'self' *.auth0.com`,
    // Allow Auth0 styles
    `style-src 'self' 'unsafe-inline' *.auth0.com`,
    // Allow Auth0 images
    `img-src 'self' data: *.auth0.com`,
  ].join('; ');
  
  // Strict Transport Security
  headers['Strict-Transport-Security'] = 'max-age=31536000; includeSubDomains; preload';
  
  return headers;
}; 