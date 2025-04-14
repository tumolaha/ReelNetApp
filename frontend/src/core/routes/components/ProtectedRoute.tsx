import { Navigate, Outlet, useLocation } from "react-router-dom";
import { useAuth } from "../../auth/hooks/useAuth";
import LoadingFallbackPage from "@/shared/pages/LoadingFallbackPage";
import { ROUTES } from "../constants";
import { useEffect } from "react";
export const ProtectedRoute = () => {
  const { isLoading, isAuthenticated, getToken } = useAuth();
  const location = useLocation();
  useEffect(() => {
    if (!isAuthenticated && !isLoading && location.pathname !== ROUTES.HOME) {
      getToken();
    }
  }, [isAuthenticated, isLoading]);


  if (isLoading) {
    return <LoadingFallbackPage />;
  }


  if (isAuthenticated && location.pathname === ROUTES.HOME) {
    return <Navigate to={ROUTES.WORKSPACE.HOME} replace />;
  }

  return <Outlet />;
};
