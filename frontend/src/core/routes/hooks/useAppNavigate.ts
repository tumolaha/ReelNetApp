import { useNavigate as useRouterNavigate } from 'react-router-dom';
import { ROUTES } from '../constants';

// type RouteKeys = keyof typeof ROUTES;
// type SubRouteKeys<T extends RouteKeys> = keyof typeof ROUTES[T];

export const useAppNavigate = () => {
  const navigate = useRouterNavigate();

  return {
    // Navigate to root level routes
    toHome: () => navigate(ROUTES.HOME),

    // Auth routes
    toLogin: () => navigate(ROUTES.AUTH.LOGIN),
    toRegister: () => navigate(ROUTES.AUTH.REGISTER),
    toForgotPassword: () => navigate(ROUTES.AUTH.FORGOT_PASSWORD),

    // User Profile routes
    toUserProfile: () => navigate(ROUTES.USER_PROFILE.PROFILE),

    // Community routes
    toCommunity: () => navigate(ROUTES.COMMUNITY.COMMUNITY),

    // Workspace routes
    toWorkspace: () => navigate(ROUTES.WORKSPACE.HOME),


    // Video Editor routes
    toVideoEditor: () => navigate(ROUTES.WORKSPACE.HOME),
    toVideoProject: (projectId: string) => 
      navigate(ROUTES.WORKSPACE.MY_WORKSPACE.replace(':projectId', projectId)),

    // Generic navigation methods
    to: (path: string) => navigate(path),
    back: () => navigate(-1),
    forward: () => navigate(1),
  };
}; 