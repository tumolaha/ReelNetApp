import { Auth0Provider } from "@auth0/auth0-react";
import { ROUTES } from "@/core/routes/constants";
import { useEffect, useMemo, useCallback } from "react";
import { useToast } from "@/shared/hooks/use-toast";

/**
 * AuthProvider - Manages authentication with Auth0
 * Provides authentication context for the entire application
 * Follows Auth0 best practices
 */
export const AuthProvider = ({ children }: { children: React.ReactNode }) => {
  const { toast } = useToast();

  // Get configuration from environment variables
  const config = useMemo(() => {
    const domain = import.meta.env.VITE_AUTH0_DOMAIN as string;
    const clientId = import.meta.env.VITE_AUTH0_CLIENT_ID as string;
    const audience = import.meta.env.VITE_AUTH0_AUDIENCE as string;
    const redirectUri = window.location.origin;
    return { domain, clientId, audience, redirectUri };
  }, []);

  // Handle callback after login
  const onRedirectCallback = useCallback((appState: any) => {
    try {
      // Security check: verify appState
      if (appState && typeof appState === "object" && "returnTo" in appState) {
        // Ensure returnTo URL is within our application
        const returnUrl = appState.returnTo as string;
        // Only navigate to internal paths, not external URLs
        if (returnUrl.startsWith("/") && !returnUrl.startsWith("//")) {
          window.location.href = returnUrl;
          return;
        }
      }
      // Default fallback
      window.location.href = ROUTES.HOME;
    } catch (error) {
      window.location.href = ROUTES.HOME;
    }
  }, []);

  // Set up listener for global Auth0 errors
  useEffect(() => {
    const handleAuthError = (event: ErrorEvent) => {
      // Check if this is an Auth0-related error
      if (
        event.message &&
        (event.message.includes("auth0") ||
          event.message.includes("authentication") ||
          event.message.includes("token") ||
          event.message.includes("Service not found"))
      ) {
        // Only show critical auth errors
        if (event.message.includes("Service not found") && event.message.includes("/api/v2/")) {
          toast({
            title: "Authentication Error",
            description: "An error occurred. Please try again.",
            variant: "destructive",
          });
        }
      }
    };

    // Handle logout events from other tabs
    const handleAuthStateChange = (event: CustomEvent) => {
      if (event.detail?.action === "logout") {
        // Synchronize logout state across tabs
        window.location.href = ROUTES.AUTH.LOGIN;
      }
    };

    window.addEventListener("error", handleAuthError);
    window.addEventListener(
      "auth_state_changed",
      handleAuthStateChange as EventListener
    );

    return () => {
      window.removeEventListener("error", handleAuthError);
      window.removeEventListener(
        "auth_state_changed",
        handleAuthStateChange as EventListener
      );
    };
  }, [toast]);

  return (
    <Auth0Provider
      domain={config.domain}
      clientId={config.clientId}
      authorizationParams={{
        redirect_uri: config.redirectUri,
        audience: config.audience,
        scope: "openid profile email offline_access",
      }}
      useRefreshTokens={true}
      useRefreshTokensFallback={true}
      cacheLocation="localstorage"
      onRedirectCallback={onRedirectCallback}
      skipRedirectCallback={window.location.pathname === "/auth/callback"}
    >
      {children}
    </Auth0Provider>
  );
};
