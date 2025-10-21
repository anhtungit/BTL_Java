# Hướng dẫn tích hợp Database với JavaFX

## ✅ Đã hoàn thành tích hợp MySQL

### 🔧 **Các tính năng đã được cập nhật:**

#### 1. **Thêm nhân viên mới**
- Khi thêm nhân viên từ giao diện, dữ liệu sẽ được lưu vào MySQL database
- Hiển thị thông báo thành công/thất bại
- Tự động refresh danh sách sau khi thêm

#### 2. **Chỉnh sửa nhân viên**
- Chọn nhân viên từ bảng danh sách
- Chỉnh sửa thông tin trong form
- Dữ liệu được cập nhật trực tiếp vào database
- Hiển thị thông báo kết quả

#### 3. **Xóa nhân viên**
- Xóa nhân viên từ database (không chỉ trong memory)
- Xóa cả user account liên quan
- Hiển thị thông báo xác nhận và kết quả

#### 4. **Tìm kiếm nhân viên**
- Tìm kiếm trực tiếp trong database
- Kết quả được load từ MySQL
- Hiển thị kết quả tìm kiếm

### 📋 **Cách sử dụng:**

#### **Thêm nhân viên:**
1. Vào menu "Quản lý nhân viên" → "Thêm nhân viên"
2. Điền thông tin bắt buộc:
   - Tên nhân viên
   - Chức vụ
   - Lương
   - Số điện thoại
   - Email (trong field Address)
3. Nhấn "Thêm" để lưu vào database

#### **Chỉnh sửa nhân viên:**
1. Vào "Danh sách nhân viên"
2. Chọn nhân viên cần sửa
3. Vào "Chỉnh sửa nhân viên"
4. Sửa thông tin cần thiết
5. Nhấn "Cập nhật" để lưu vào database

#### **Xóa nhân viên:**
1. Vào "Xóa nhân viên"
2. Click vào dòng nhân viên cần xóa
3. Xác nhận xóa
4. Dữ liệu sẽ bị xóa khỏi database

#### **Tìm kiếm nhân viên:**
1. Vào "Tìm kiếm nhân viên"
2. Nhập tên cần tìm
3. Nhấn "Tìm kiếm"
4. Kết quả được load từ database

### 🔍 **Kiểm tra dữ liệu trong MySQL:**

```sql
-- Xem tất cả nhân viên
SELECT * FROM employees;

-- Xem tất cả user accounts
SELECT * FROM users;

-- Xem thông tin chi tiết
SELECT e.name, e.position, e.salary, u.username, u.role 
FROM employees e 
JOIN users u ON e.id = u.employee_id;
```

### ⚠️ **Lưu ý quan trọng:**

1. **Backup dữ liệu:** Luôn backup database trước khi thực hiện thay đổi lớn
2. **Kiểm tra kết nối:** Đảm bảo MySQL server đang chạy
3. **Thông báo lỗi:** Ứng dụng sẽ hiển thị thông báo chi tiết khi có lỗi
4. **Dữ liệu đồng bộ:** Mọi thay đổi đều được lưu vào database ngay lập tức

### 🚀 **Tính năng nâng cao có thể thêm:**

1. **Validation dữ liệu:** Kiểm tra email hợp lệ, số điện thoại đúng format
2. **Upload ảnh:** Thêm ảnh đại diện cho nhân viên
3. **Export/Import:** Xuất/nhập dữ liệu từ Excel
4. **Phân quyền chi tiết:** Quản lý quyền truy cập theo từng chức năng
5. **Audit log:** Ghi lại lịch sử thay đổi dữ liệu

### 📞 **Hỗ trợ:**

Nếu gặp lỗi, hãy kiểm tra:
1. MySQL server có đang chạy không
2. Database `coffee_shop_db` đã được tạo chưa
3. Thông tin kết nối trong `DatabaseConfig.java` có đúng không
4. Xem thông báo lỗi chi tiết trong console
