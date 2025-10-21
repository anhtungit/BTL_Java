# Hướng dẫn cài đặt MySQL cho dự án JavaFX

## 1. Cài đặt MySQL Server

### Trên Windows:
1. Tải MySQL Community Server từ: https://dev.mysql.com/downloads/mysql/
2. Chọn phiên bản phù hợp với hệ điều hành của bạn
3. Chạy file cài đặt và làm theo hướng dẫn
4. Ghi nhớ mật khẩu root mà bạn đặt trong quá trình cài đặt

### Trên macOS:
```bash
# Sử dụng Homebrew
brew install mysql
brew services start mysql
```

### Trên Linux (Ubuntu/Debian):
```bash
sudo apt update
sudo apt install mysql-server
sudo mysql_secure_installation
```

## 2. Tạo Database

1. Mở MySQL Command Line Client hoặc MySQL Workbench
2. Đăng nhập với tài khoản root
3. Tạo database mới:

```sql
CREATE DATABASE coffee_shop_db;
USE coffee_shop_db;
```

## 3. Cấu hình kết nối

Mở file `src/main/java/org/openjfx/DatabaseConfig.java` và cập nhật thông tin kết nối:

```java
private static final String DB_URL = "jdbc:mysql://localhost:3306/coffee_shop_db";
private static final String DB_USERNAME = "root";
private static final String DB_PASSWORD = "your_password_here"; // Thay đổi mật khẩu của bạn
```

## 4. Tạo tài khoản người dùng (tùy chọn)

Để bảo mật hơn, bạn có thể tạo một tài khoản người dùng riêng cho ứng dụng:

```sql
CREATE USER 'coffee_app'@'localhost' IDENTIFIED BY 'your_app_password';
GRANT ALL PRIVILEGES ON coffee_shop_db.* TO 'coffee_app'@'localhost';
FLUSH PRIVILEGES;
```

Nếu sử dụng tài khoản này, cập nhật `DatabaseConfig.java`:

```java
private static final String DB_USERNAME = "coffee_app";
private static final String DB_PASSWORD = "your_app_password";
```

## 5. Chạy ứng dụng

1. Đảm bảo MySQL server đang chạy
2. Chạy ứng dụng JavaFX:

```bash
mvn clean javafx:run
```

## 6. Kiểm tra kết nối

Ứng dụng sẽ tự động:
- Tạo các bảng `employees` và `users` nếu chưa tồn tại
- Thêm dữ liệu mẫu nếu chưa có dữ liệu
- Hiển thị thông báo kết nối thành công trong console

## 7. Tài khoản đăng nhập mặc định

Sau khi chạy lần đầu, bạn có thể đăng nhập với các tài khoản sau:

| Username | Password | Role |
|----------|----------|------|
| admin1   | 123456   | MANAGER |
| vanthe14 | 123456   | STAFF |
| giangtt  | 123456   | MANAGER |
| yenvb    | 123456   | STAFF |

## 8. Xử lý lỗi thường gặp

### Lỗi "Access denied for user"
- Kiểm tra username và password trong `DatabaseConfig.java`
- Đảm bảo MySQL server đang chạy
- Kiểm tra quyền truy cập của user

### Lỗi "Unknown database"
- Đảm bảo database `coffee_shop_db` đã được tạo
- Kiểm tra tên database trong `DatabaseConfig.java`

### Lỗi "Communications link failure"
- Kiểm tra MySQL server có đang chạy không
- Kiểm tra port 3306 có bị chặn không
- Thử thay đổi host từ `localhost` thành `127.0.0.1`

## 9. Backup và Restore

### Backup database:
```bash
mysqldump -u root -p coffee_shop_db > backup.sql
```

### Restore database:
```bash
mysql -u root -p coffee_shop_db < backup.sql
```
