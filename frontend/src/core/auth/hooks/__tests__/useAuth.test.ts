import { renderHook, act } from '@testing-library/react';
import { useAuth } from '../useAuth';
import { useAuth0 } from '@auth0/auth0-react';
import { useGetCurrentUserQuery, useGetUserRolesQuery } from '@/core/api/apiSlice';

// Mock dependencies
jest.mock('@auth0/auth0-react');
jest.mock('@/core/api/apiSlice');
jest.mock('@/core/auth/utils/secureStorage');

describe('useAuth', () => {
  const mockAuth0 = {
    isAuthenticated: false,
    isLoading: false,
    loginWithRedirect: jest.fn(),
    logout: jest.fn(),
    getAccessTokenSilently: jest.fn(),
    getIdTokenClaims: jest.fn(),
  };

  const mockApi = {
    useGetCurrentUserQuery: jest.fn(),
    useGetUserRolesQuery: jest.fn(),
  };

  beforeEach(() => {
    jest.clearAllMocks();
    (useAuth0 as jest.Mock).mockReturnValue(mockAuth0);
    (useGetCurrentUserQuery as jest.Mock).mockReturnValue({ data: null, isLoading: false });
    (useGetUserRolesQuery as jest.Mock).mockReturnValue({ data: null, isLoading: false });
  });

  it('should initialize with default values', () => {
    const { result } = renderHook(() => useAuth());

    expect(result.current.isAuthenticated).toBe(false);
    expect(result.current.isLoading).toBe(false);
    expect(result.current.user).toBeNull();
    expect(result.current.roles).toEqual([]);
  });

  it('should handle login', async () => {
    const { result } = renderHook(() => useAuth());

    await act(async () => {
      await result.current.login();
    });

    expect(mockAuth0.loginWithRedirect).toHaveBeenCalledWith({
      appState: {
        returnTo: window.location.pathname,
      },
    });
  });

  it('should handle logout', async () => {
    const { result } = renderHook(() => useAuth());

    await act(async () => {
      await result.current.logout();
    });

    expect(mockAuth0.logout).toHaveBeenCalledWith({
      logoutParams: {
        returnTo: window.location.origin,
      },
    });
  });

  it('should handle token refresh', async () => {
    const mockToken = 'mock-token';
    mockAuth0.getAccessTokenSilently.mockResolvedValue({ access_token: mockToken });
    mockAuth0.getIdTokenClaims.mockResolvedValue({ exp: Date.now() + 3600, iat: Date.now() });

    const { result } = renderHook(() => useAuth());

    await act(async () => {
      const token = await result.current.getToken();
      expect(token).toBe(mockToken);
    });
  });

  it('should handle authentication state changes', async () => {
    const mockUser = { id: '1', email: 'test@example.com' };
    const mockRoles = { roles: ['admin'] };

    (useGetCurrentUserQuery as jest.Mock).mockReturnValue({ data: mockUser, isLoading: false });
    (useGetUserRolesQuery as jest.Mock).mockReturnValue({ data: mockRoles, isLoading: false });
    (useAuth0 as jest.Mock).mockReturnValue({ ...mockAuth0, isAuthenticated: true });

    const { result } = renderHook(() => useAuth());

    expect(result.current.isAuthenticated).toBe(true);
    expect(result.current.user).toEqual(mockUser);
    expect(result.current.roles).toEqual(['admin']);
  });

  it('should handle errors during token refresh', async () => {
    mockAuth0.getAccessTokenSilently.mockRejectedValue(new Error('Token refresh failed'));

    const { result } = renderHook(() => useAuth());

    await act(async () => {
      const token = await result.current.getToken();
      expect(token).toBeNull();
    });
  });

  it('should handle session timeout', async () => {
    jest.useFakeTimers();

    const { result } = renderHook(() => useAuth());

    await act(async () => {
      // Simulate authentication
      (useAuth0 as jest.Mock).mockReturnValue({ ...mockAuth0, isAuthenticated: true });
      
      // Fast forward time
      jest.advanceTimersByTime(8 * 60 * 60 * 1000 + 1);
    });

    expect(mockAuth0.logout).toHaveBeenCalled();
  });
}); 