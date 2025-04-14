# Hệ thống xác thực Auth0

Hệ thống xác thực này sử dụng Auth0 để quản lý người dùng, xác thực và phân quyền. Hệ thống được thiết kế để tuân thủ các best practices của Auth0 và cung cấp trải nghiệm người dùng tốt nhất.

## Cấu trúc thư mục

```
frontend/src/core/auth/
├── components/         # Components liên quan đến xác thực
│   └── AuthGuard.tsx   # Bảo vệ các route yêu cầu xác thực
├── hooks/              # Custom hooks
│   └── useAuth.ts      # Hook chính để quản lý xác thực
├── providers/          # Context providers
│   └── AuthProvider.tsx # Provider cho Auth0
├── services/           # Services
│   └── authService.ts  # Service quản lý token
└── utils/              # Utilities
    └── secureStorage.ts # Lưu trữ an toàn cho token
```

## Cài đặt

1. Cấu hình biến môi trường trong file `.env`:

```
VITE_AUTH0_DOMAIN=your-domain.auth0.com
VITE_AUTH0_CLIENT_ID=your-client-id
VITE_AUTH0_AUDIENCE=your-audience
```

2. Đảm bảo Auth0 Provider được bọc ở cấp cao nhất trong ứng dụng:

```tsx
import { AuthProvider } from '@/core/auth/providers/AuthProvider';

function App() {
  return (
    <AuthProvider>
      <Router>
        {/* Routes của ứng dụng */}
      </Router>
    </AuthProvider>
  );
}
```

## Sử dụng

### Bảo vệ route

Sử dụng `AuthGuard` để bảo vệ các route yêu cầu xác thực:

```tsx
import { AuthGuard } from '@/core/auth/components/AuthGuard';

function ProtectedRoute() {
  return (
    <AuthGuard>
      <YourProtectedComponent />
    </AuthGuard>
  );
}

// Với yêu cầu về quyền và vai trò
function AdminRoute() {
  return (
    <AuthGuard 
      requiredRoles={['admin']} 
      requiredPermissions={['read:users', 'write:users']}
    >
      <AdminDashboard />
    </AuthGuard>
  );
}
```

### Sử dụng hook useAuth

```tsx
import { useAuth } from '@/core/auth/hooks/useAuth';

function Profile() {
  const { user, isAuthenticated, isLoading, logout } = useAuth();

  if (isLoading) {
    return <div>Loading...</div>;
  }

  if (!isAuthenticated) {
    return <div>Please log in</div>;
  }

  return (
    <div>
      <h1>Welcome, {user?.email}</h1>
      <button onClick={logout}>Logout</button>
    </div>
  );
}
```

### Gọi API với token

Hệ thống tự động thêm token vào các request API thông qua axios interceptor. Không cần thêm token thủ công.

```tsx
import { apiSlice } from '@/core/api/apiSlice';

// Trong component
const { data, isLoading } = apiSlice.endpoints.getCurrentUser.useQuery();
```

## Xử lý token

- Token được lưu trữ an toàn trong localStorage với mã hóa
- Token được tự động làm mới trước khi hết hạn
- Phiên làm việc tự động hết hạn sau 8 giờ hoặc khi token hết hạn

## Bảo mật

Hệ thống xác thực này tuân thủ các best practices về bảo mật:

1. **Mã hóa token**: Token được mã hóa trước khi lưu vào localStorage
2. **Refresh token**: Sử dụng refresh token để kéo dài phiên làm việc
3. **Kiểm tra quyền**: Kiểm tra quyền và vai trò của người dùng
4. **Xử lý lỗi**: Xử lý lỗi xác thực và hiển thị thông báo phù hợp
5. **Headers bảo mật**: Thêm các headers bảo mật vào request

## Tương tác với backend

Backend cần cung cấp các endpoint sau:

1. `/auth/validate`: Kiểm tra tính hợp lệ của token
2. `/auth/user`: Lấy thông tin người dùng hiện tại
3. `/auth/roles`: Lấy danh sách vai trò của người dùng

Backend cần xác thực token JWT từ Auth0 và kiểm tra quyền của người dùng. 