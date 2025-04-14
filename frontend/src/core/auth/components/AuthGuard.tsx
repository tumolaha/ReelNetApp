import { ReactNode, useEffect } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import { useAuth } from '../hooks/useAuth';
import { ROUTES } from '@/core/routes/constants';
import { useToast } from '@/shared/hooks/use-toast';

interface AuthGuardProps {
  children: ReactNode;
  requiredPermissions?: string[];
  requiredRoles?: string[];
}

/**
 * AuthGuard - Bảo vệ các route yêu cầu xác thực
 * Chuyển hướng người dùng chưa đăng nhập đến trang đăng nhập
 * Kiểm tra quyền và vai trò nếu được chỉ định
 */
export const AuthGuard = ({ 
  children, 
  requiredPermissions = [], 
  requiredRoles = [] 
}: AuthGuardProps) => {
  const { isAuthenticated, isLoading, user } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();
  const { toast } = useToast();

  useEffect(() => {
    // Chờ cho đến khi trạng thái xác thực được xác định
    if (isLoading) return;

    // Nếu người dùng chưa đăng nhập, chuyển hướng đến trang đăng nhập
    if (!isAuthenticated) {
      // Lưu đường dẫn hiện tại để chuyển hướng lại sau khi đăng nhập
      const returnPath = location.pathname + location.search;
      navigate(ROUTES.AUTH.LOGIN, { state: { returnTo: returnPath } });
      return;
    }

    // Kiểm tra quyền nếu được chỉ định
    if (requiredPermissions.length > 0 && user) {
      const hasRequiredPermissions = requiredPermissions.every(permission => 
        user.permissions.includes(permission)
      );

      if (!hasRequiredPermissions) {
        toast({
          title: "Không có quyền truy cập",
          description: "Bạn không có quyền truy cập vào trang này.",
          variant: "destructive",
        });
        navigate(ROUTES.HOME);
        return;
      }
    }

    // Kiểm tra vai trò nếu được chỉ định
    if (requiredRoles.length > 0 && user) {
      const hasRequiredRoles = requiredRoles.some(role => 
        user.roles.includes(role)
      );

      if (!hasRequiredRoles) {
        toast({
          title: "Không có quyền truy cập",
          description: "Vai trò của bạn không được phép truy cập vào trang này.",
          variant: "destructive",
        });
        navigate(ROUTES.HOME);
        return;
      }
    }
  }, [isAuthenticated, isLoading, user, navigate, location, requiredPermissions, requiredRoles, toast]);

  // Hiển thị loading hoặc null trong khi kiểm tra xác thực
  if (isLoading) {
    return null; // Hoặc hiển thị loading spinner
  }

  // Nếu người dùng đã đăng nhập và có đủ quyền, hiển thị nội dung
  return isAuthenticated ? <>{children}</> : null;
}; 