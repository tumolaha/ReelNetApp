-- Tạo bảng user_permissions để lưu trữ quyền của người dùng
CREATE TABLE user_permissions (
    user_id UUID NOT NULL,
    permission VARCHAR(255) NOT NULL,
    PRIMARY KEY (user_id, permission),
    CONSTRAINT fk_user_permissions_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Tạo index cho tìm kiếm nhanh theo permission
CREATE INDEX idx_user_permissions_permission ON user_permissions(permission); 