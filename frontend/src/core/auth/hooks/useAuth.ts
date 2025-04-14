import { useAuth0 } from "@auth0/auth0-react";
import { useCallback, useEffect, useState, useRef } from "react";
import { useValidateTokenQuery } from "@/core/api/apiSlice";
import { authService } from "../services/authService";
import { useToast } from "@/shared/hooks/use-toast";

// Constants
const MAX_VALIDATION_RETRIES = 2;
const TOKEN_REFRESH_INTERVAL = 10 * 60 * 1000; // 10 minutes in milliseconds
const SESSION_TIMEOUT = 8 * 60 * 60 * 1000; // 8 hours

// Types
interface User {
  id: string;
  email: string;
  roles: string[];
  permissions: string[];
}

/**
 * Custom hook for authentication management
 * Handles Auth0 integration, token validation, and session management
 * Follows Auth0 best practices for token handling and refresh
 */
export const useAuth = () => {
  const { toast } = useToast();

  // Auth0 hook
  const {
    isAuthenticated: isAuth0Authenticated,
    isLoading: isAuth0Loading,
    loginWithRedirect,
    logout: auth0Logout,
    getAccessTokenSilently,
    getIdTokenClaims,
    user: auth0User,
    error: auth0Error,
  } = useAuth0();

  // Authentication state
  const [isAuthenticated, setIsAuthenticated] = useState<boolean>(false);
  const [isLoading, setIsLoading] = useState<boolean>(true);
  const [sessionExpired, setSessionExpired] = useState<boolean>(false);
  const [tokenValidationAttempted, setTokenValidationAttempted] =
    useState<boolean>(false);
  const [user, setUser] = useState<User | null>(null);

  // Refs for token refresh and validation
  const validationRetriesRef = useRef<number>(0);
  const isValidatingRef = useRef<boolean>(false);
  const refreshTokenIntervalRef = useRef<number | null>(null);
  const sessionTimeoutRef = useRef<number | null>(null);

  // Check if we have a token in storage
  const hasStoredToken = authService.hasToken();

  // Skip validation if we're still loading Auth0 or if we don't have a token
  const skipValidation =
    isAuth0Loading || !hasStoredToken || isValidatingRef.current;

  /**
   * Setup token refresh interval
   * Auth0 recommends proactively refreshing tokens before they expire
   */
  useEffect(() => {
    const setupTokenRefresh = () => {
      // Clear any existing interval
      if (refreshTokenIntervalRef.current) {
        window.clearInterval(refreshTokenIntervalRef.current);
      }

      // Only set up refresh if authenticated
      if (isAuthenticated) {
        refreshTokenIntervalRef.current = window.setInterval(async () => {
          try {
            // Check if token needs refresh
            if (authService.needsRefresh()) {
              const newToken = await getAccessTokenSilently({
                authorizationParams: {
                  audience: import.meta.env.VITE_AUTH0_AUDIENCE,
                },
                cacheMode: "off", // Force a new token request
              });

              if (newToken) {
                authService.setToken(newToken);
              }
            }
          } catch (error) {
            console.error("Error refreshing token:", error);
            // If refresh fails, we'll try again on next interval
          }
        }, TOKEN_REFRESH_INTERVAL);
      }
    };

    setupTokenRefresh();

    // Cleanup on unmount
    return () => {
      if (refreshTokenIntervalRef.current) {
        window.clearInterval(refreshTokenIntervalRef.current);
      }
    };
  }, [isAuthenticated, getAccessTokenSilently]);

  /**
   * Setup session timeout
   * Automatically log out user after session timeout
   */
  useEffect(() => {
    const setupSessionTimeout = () => {
      // Clear any existing timeout
      if (sessionTimeoutRef.current) {
        window.clearTimeout(sessionTimeoutRef.current);
      }

      // Only set up timeout if authenticated
      if (isAuthenticated) {
        // Get remaining time from token if possible
        const remainingTime = authService.getTokenRemainingTime();
        const timeoutDuration =
          remainingTime > 0
            ? remainingTime * 1000 // Convert seconds to milliseconds
            : SESSION_TIMEOUT;

        sessionTimeoutRef.current = window.setTimeout(() => {
          handleLogout();
          toast({
            title: "Session Expired",
            description: "Your session has expired. Please sign in again.",
            variant: "destructive",
          });
        }, timeoutDuration);
      }
    };

    setupSessionTimeout();

    // Cleanup on unmount
    return () => {
      if (sessionTimeoutRef.current) {
        window.clearTimeout(sessionTimeoutRef.current);
      }
    };
  }, [isAuthenticated, toast]);

  // Thêm log để debug
  useEffect(() => {
    if (!skipValidation) {
      const token = authService.getToken();
      console.log(`Validating token: ${token ? 'Token exists' : 'No token found'}`);
      
      if (token) {
        try {
          // Log token details for debugging
          const claims = authService.getTokenClaims();
          console.log('Token claims:', {
            sub: claims?.sub,
            exp: claims?.exp ? new Date(claims.exp * 1000).toISOString() : 'unknown',
            iss: claims?.iss,
            aud: claims?.aud
          });
          
          // Check if token is expired
          if (authService.isTokenExpired()) {
            console.warn('Token is expired according to local validation');
          }
        } catch (error) {
          console.error('Error parsing token:', error);
        }
      }
    }
  }, [skipValidation]);

  // Use RTK Query to validate the token
  const {
    data: validationData,
    error: validationError,
    isLoading: isValidating,
  } = useValidateTokenQuery(undefined, {
    skip: skipValidation,
    refetchOnMountOrArgChange: false,
  });

  /**
   * Handle token validation results
   */
  useEffect(() => {
    if (skipValidation) return;

    // Nếu đang trong quá trình validate, không làm gì cả
    if (isValidatingRef.current) return;

    // Đánh dấu đang trong quá trình validate
    isValidatingRef.current = true;

    // Token is valid
    if (validationData && !validationError) {
      console.log('Token validation successful:', validationData);
      // Cập nhật thông tin user nếu có
      if (validationData.userId) {
        const validatedUser: User = {
          id: validationData.userId,
          email: validationData.email || `user-${validationData.userId}`, // Fallback nếu không có email
          roles: [],
          permissions: []
        };
        setUser(validatedUser);
      }
      
      setIsAuthenticated(true);
      setSessionExpired(false);
      validationRetriesRef.current = 0;
      isValidatingRef.current = false;
      setTokenValidationAttempted(true);
      return;
    }

    // Token validation failed
    if (validationError && !isValidating) {
      console.warn("Token validation failed:", validationError);

      // Log detailed error information
      if (typeof validationError === 'object' && validationError !== null) {
        // Safe access to potential properties
        const errorObj = validationError as any;
        if (errorObj.status) {
          console.error(`Validation error status: ${errorObj.status}`);
        }
        if (errorObj.data) {
          console.error('Validation error data:', errorObj.data);
        }
      }

      // Max retries reached
      if (validationRetriesRef.current >= MAX_VALIDATION_RETRIES) {
        console.error("Max validation retries reached, clearing token");
        authService.clearToken();
        setIsAuthenticated(false);
        setSessionExpired(true);
        isValidatingRef.current = false;
        setTokenValidationAttempted(true);

        toast({
          title: "Session Expired",
          description: "Please sign in again to continue.",
          variant: "destructive",
        });
        return;
      }

      // Tính toán thời gian delay dựa trên số lần retry
      // Sử dụng exponential backoff: 1s, 2s, 4s, ...
      const delay = 1000 * Math.pow(2, validationRetriesRef.current);
      
      // Thêm jitter (độ trễ ngẫu nhiên) để tránh thundering herd
      const jitter = Math.random() * 1000;
      const totalDelay = delay + jitter;

      // Retry validation
      validationRetriesRef.current += 1;
      isValidatingRef.current = false;

      // Force a re-validation after a delay
      setTimeout(() => {
        console.log(
          `Attempting validation retry ${validationRetriesRef.current} of ${MAX_VALIDATION_RETRIES} after ${Math.round(totalDelay)}ms`
        );
        setTokenValidationAttempted(false);
      }, totalDelay);
    }
  }, [validationData, validationError, isValidating, skipValidation, toast]);

  /**
   * Handle Auth0 authentication state
   */
  useEffect(() => {
    const handleAuth0State = async () => {
      // If Auth0 is still loading, wait
      if (isAuth0Loading) return;

      // If user is authenticated with Auth0
      if (isAuth0Authenticated && auth0User) {
        try {
          // Get token and store it
          const token = await getToken();

          if (token) {
            // Get user claims from ID token
            const idTokenClaims = await getIdTokenClaims();

            // Set user information from claims
            if (idTokenClaims) {
              setUser({
                id: idTokenClaims.sub,
                email: idTokenClaims.email || auth0User.email || "",
                roles: idTokenClaims.roles || [],
                permissions: idTokenClaims.permissions || [],
              });
            }

            setIsAuthenticated(true);
            setSessionExpired(false);
          } else {
            setIsAuthenticated(false);
            setUser(null);
          }
        } catch (error) {
          console.error("Error getting token:", error);
          setIsAuthenticated(false);
          setUser(null);
        }
      } else if (!isAuth0Loading) {
        // If Auth0 is done loading and user is not authenticated
        if (!hasStoredToken) {
          // No stored token, definitely not authenticated
          setIsAuthenticated(false);
          setUser(null);
        }
        // Otherwise, rely on token validation result
      }

      // Auth state is determined, no longer loading
      setIsLoading(false);
    };

    handleAuth0State();
  }, [
    isAuth0Loading,
    isAuth0Authenticated,
    auth0User,
    hasStoredToken,
    tokenValidationAttempted,
    getIdTokenClaims,
  ]);

  /**
   * Handle Auth0 errors
   */
  useEffect(() => {
    if (auth0Error) {
      console.error("Auth0 error:", auth0Error);
      toast({
        title: "Authentication Error",
        description:
          auth0Error.message || "An error occurred during authentication.",
        variant: "destructive",
      });
    }
  }, [auth0Error, toast]);

  /**
   * Get authentication token with refresh logic
   * Follows Auth0 best practices for token acquisition
   */
  const getToken = useCallback(async () => {
    try {
      // If we're not authenticated with Auth0, return null
      if (!isAuth0Authenticated) {
        return null;
      }

      // If validation has failed and we're retrying, force a new token
      if (validationRetriesRef.current > 0) {
        console.log("Validation retry in progress, forcing new token acquisition");
        // Clear existing token to force a fresh one
        authService.clearToken();
        
        // Get a new token from Auth0
        const token = await getAccessTokenSilently({
          authorizationParams: {
            audience: import.meta.env.VITE_AUTH0_AUDIENCE,
          },
          cacheMode: "off", // Force a new token request
          detailedResponse: false, // Just get the token string
        });

        // Store the token
        if (token) {
          authService.setToken(token);
          return token;
        }
        
        return null;
      }

      // Check if we have a valid token in storage
      if (authService.hasToken() && !authService.isTokenExpired()) {
        return authService.getToken();
      }

      // Get a new token from Auth0
      const token = await getAccessTokenSilently({
        authorizationParams: {
          audience: import.meta.env.VITE_AUTH0_AUDIENCE,
        },
        detailedResponse: false, // Just get the token string
      });

      // Store the token
      if (token) {
        authService.setToken(token);
        return token;
      }

      return null;
    } catch (error) {
      console.error("Error getting token:", error);

      // Handle specific Auth0 errors
      if (error instanceof Error) {
        const errorMessage = error.message || "";

        // Handle token expired errors
        if (
          errorMessage.includes("expired") ||
          errorMessage.includes("expired_token") ||
          errorMessage.includes("invalid_token")
        ) {
          console.warn("Token expired or invalid, clearing local token");
          authService.clearToken();
          setSessionExpired(true);
        }
      }

      return null;
    }
  }, [isAuth0Authenticated, getAccessTokenSilently, validationRetriesRef.current]);

  /**
   * Logout from both Auth0 and local storage
   * Cleans up all auth state
   */
  const handleLogout = useCallback(() => {
    // Clear local token and state
    authService.clearToken();
    setIsAuthenticated(false);
    setUser(null);

    // Clear intervals and timeouts
    if (refreshTokenIntervalRef.current) {
      window.clearInterval(refreshTokenIntervalRef.current);
      refreshTokenIntervalRef.current = null;
    }

    if (sessionTimeoutRef.current) {
      window.clearTimeout(sessionTimeoutRef.current);
      sessionTimeoutRef.current = null;
    }

    // Logout from Auth0
    auth0Logout({
      logoutParams: {
        returnTo: window.location.origin,
      },
    });
  }, [auth0Logout]);

  /**
   * Login with redirect to specified return URL
   */
  const handleLogin = useCallback(
    (returnTo?: string) => {
      const loginOptions: any = {
        authorizationParams: {
          audience: import.meta.env.VITE_AUTH0_AUDIENCE,
          redirect_uri: window.location.origin,
        },
      };

      // Add returnTo if specified
      if (returnTo) {
        loginOptions.appState = { returnTo };
      }

      loginWithRedirect(loginOptions);
    },
    [loginWithRedirect]
  );

  return {
    isAuthenticated,
    isLoading,
    user,
    sessionExpired,
    login: handleLogin,
    logout: handleLogout,
    getToken,
    refreshToken: getToken, // Expose explicit refresh method
  };
};
