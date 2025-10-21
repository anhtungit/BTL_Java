# Phần mềm quản lý quán cà phê - JavaFX với MySQL

## Mô tả
Ứng dụng JavaFX để quản lý nhân viên quán cà phê với kết nối cơ sở dữ liệu MySQL.

## Tính năng
- Đăng nhập với phân quyền (Manager/Staff)
- Quản lý nhân viên (thêm, sửa, xóa, tìm kiếm)
- Lưu trữ dữ liệu trong MySQL database
- Giao diện JavaFX hiện đại

## Yêu cầu hệ thống
- Java 11 hoặc cao hơn
- MySQL Server 8.0 hoặc cao hơn
- Maven 3.6+

## Cài đặt

### 1. Cài đặt MySQL
Xem file `MYSQL_SETUP.md` để biết hướng dẫn chi tiết cài đặt MySQL.

### 2. Cấu hình database
1. Tạo database `coffee_shop_db` trong MySQL
2. Cập nhật thông tin kết nối trong `src/main/java/org/openjfx/DatabaseConfig.java`:
   ```java
   private static final String DB_URL = "jdbc:mysql://localhost:3306/coffee_shop_db";
   private static final String DB_USERNAME = "root";
   private static final String DB_PASSWORD = "your_password";
   ```

### 3. Chạy ứng dụng
```bash
# Clone repository
git clone <repository-url>
cd hellofx

# Chạy ứng dụng
mvn clean javafx:run
```

## Cấu trúc dự án
```
src/main/java/org/openjfx/
├── App.java                 # Main application class
├── DatabaseConfig.java      # Cấu hình kết nối MySQL
├── DatabaseUtil.java        # Utility methods cho database
├── Employee.java            # Model class cho nhân viên
├── EmployeeStore.java       # Data access layer
├── LoginController.java     # Controller cho màn hình đăng nhập
├── PrimaryController.java    # Controller chính
└── SecondaryController.java # Controller phụ

src/main/resources/org/openjfx/
├── *.fxml                  # FXML files cho giao diện
└── *.css                   # CSS files cho styling
```

## Tài khoản đăng nhập mặc định
| Username | Password | Role |
|----------|----------|------|
| admin1   | 123456   | MANAGER |
| vanthe14 | 123456   | STAFF |
| giangtt  | 123456   | MANAGER |
| yenvb    | 123456   | STAFF |

## Database Schema

### Bảng `employees`
- `id` (INT, AUTO_INCREMENT, PRIMARY KEY)
- `name` (VARCHAR(100), NOT NULL)
- `position` (VARCHAR(50), NOT NULL)
- `salary` (DECIMAL(10,2), NOT NULL)
- `email` (VARCHAR(100), UNIQUE)
- `phone` (VARCHAR(20))
- `address` (TEXT)
- `hire_date` (DATE)
- `created_at` (TIMESTAMP)
- `updated_at` (TIMESTAMP)

### Bảng `users`
- `id` (INT, AUTO_INCREMENT, PRIMARY KEY)
- `username` (VARCHAR(50), UNIQUE, NOT NULL)
- `password` (VARCHAR(255), NOT NULL)
- `role` (ENUM('MANAGER', 'STAFF'), NOT NULL)
- `employee_id` (INT, FOREIGN KEY)
- `created_at` (TIMESTAMP)

## Xử lý lỗi thường gặp

### Lỗi kết nối MySQL
1. Kiểm tra MySQL server có đang chạy không
2. Kiểm tra thông tin kết nối trong `DatabaseConfig.java`
3. Đảm bảo database `coffee_shop_db` đã được tạo

### Lỗi module accessibility
- Đây là warning không ảnh hưởng đến chức năng của ứng dụng
- Có thể bỏ qua hoặc cập nhật `module-info.java` nếu cần

### Lỗi JavaFX runtime
```bash
# Nếu gặp lỗi JavaFX runtime, thử chạy với:
mvn clean compile javafx:run
```

## Phát triển

### Thêm tính năng mới
1. Tạo model class trong package `org.openjfx`
2. Thêm methods tương ứng trong `DatabaseUtil.java`
3. Cập nhật `EmployeeStore.java` nếu cần
4. Tạo FXML file và controller cho giao diện

### Backup database
```bash
mysqldump -u root -p coffee_shop_db > backup.sql
```

### Restore database
```bash
mysql -u root -p coffee_shop_db < backup.sql
```

## License
MIT License
