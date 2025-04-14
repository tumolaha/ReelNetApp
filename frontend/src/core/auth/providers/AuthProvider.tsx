import { Auth0Provider } from '@auth0/auth0-react';
import { ROUTES } from '@/core/routes/constants';
import { useEffect, useState, useMemo, useCallback } from 'react';
import { useToast } from '@/shared/hooks/use-toast';

/**
 * AuthProvider - Quản lý xác thực với Auth0
 * Cung cấp context xác thực cho toàn bộ ứng dụng
 * Tuân thủ các best practices của Auth0
 */
export const AuthProvider = ({ children }: { children: React.ReactNode }) => {
  const { toast } = useToast();
  const [isConfigValid, setIsConfigValid] = useState(false);

  // Lấy thông tin cấu hình từ biến môi trường
  const config = useMemo(() => {
    const domain = import.meta.env.VITE_AUTH0_DOMAIN as string;
    const clientId = import.meta.env.VITE_AUTH0_CLIENT_ID as string;
    const audience = import.meta.env.VITE_AUTH0_AUDIENCE as string;
    const redirectUri = window.location.origin;
    
    return { domain, clientId, audience, redirectUri };
  }, []);

  // Kiểm tra cấu hình Auth0
  useEffect(() => {
    const { domain, clientId, audience } = config;
    
    if (!(domain && clientId && audience)) {
      console.error('Auth0 configuration is incomplete. Please check your environment variables.');
      toast({
        title: "Lỗi cấu hình xác thực",
        description: "Cấu hình xác thực không đầy đủ. Vui lòng liên hệ hỗ trợ.",
        variant: "destructive",
      });
      setIsConfigValid(false);
    } else {
      setIsConfigValid(true);
    }
  }, [config, toast]);

  // Xử lý callback sau khi đăng nhập
  const onRedirectCallback = useCallback((appState: any) => {
    // Kiểm tra bảo mật: xác thực appState
    if (appState && typeof appState === 'object' && 'returnTo' in appState) {
      // Đảm bảo returnTo URL nằm trong ứng dụng của chúng ta
      const returnUrl = appState.returnTo as string;
      
      // Chỉ điều hướng đến đường dẫn nội bộ, không phải URL bên ngoài
      if (returnUrl.startsWith('/') && !returnUrl.startsWith('//')) {
        window.location.href = returnUrl;
        return;
      }
    }
    
    // Fallback mặc định
    window.location.href = ROUTES.HOME;
  }, []);

  // Thiết lập listener xử lý lỗi Auth0 toàn cục
  useEffect(() => {
    const handleAuthError = (event: ErrorEvent) => {
      // Kiểm tra xem đây có phải là lỗi liên quan đến Auth0 không
      if (event.message && (
        event.message.includes('auth0') || 
        event.message.includes('authentication') || 
        event.message.includes('token')
      )) {
        console.error('Auth0 Error:', event.error || event.message);
        toast({
          title: "Lỗi xác thực",
          description: "Đã xảy ra lỗi trong quá trình xác thực. Vui lòng thử lại.",
          variant: "destructive",
        });
      }
    };

    // Xử lý sự kiện đăng xuất từ tab khác
    const handleAuthStateChange = (event: CustomEvent) => {
      if (event.detail?.action === 'logout') {
        // Đồng bộ trạng thái đăng xuất giữa các tab
        window.location.href = ROUTES.AUTH.LOGIN;
      }
    };

    window.addEventListener('error', handleAuthError);
    window.addEventListener('auth_state_changed', handleAuthStateChange as EventListener);
    
    return () => {
      window.removeEventListener('error', handleAuthError);
      window.removeEventListener('auth_state_changed', handleAuthStateChange as EventListener);
    };
  }, [toast]);

  // Không render gì nếu cấu hình không hợp lệ
  if (!isConfigValid) {
    return null;
  }

  return (
    <Auth0Provider
      domain={config.domain}
      clientId={config.clientId}
      authorizationParams={{
        redirect_uri: config.redirectUri,
        audience: config.audience,
        scope: 'openid profile email offline_access',
      }}
      useRefreshTokens={true}
      useRefreshTokensFallback={true}
      cacheLocation="localstorage"
      onRedirectCallback={onRedirectCallback}
      skipRedirectCallback={window.location.pathname === '/auth/callback'}
    >
      {children}
    </Auth0Provider>
  );
}; 