# Kế Hoạch Triển Khai Website Đặt Phòng Homestay

Dự án này sẽ tạo ra một trang web đặt phòng homestay, sử dụng cấu trúc chuẩn của một ứng dụng web Java Spring Boot.

## 1. Tổng quan chức năng (Sát với thực tế)

Hệ thống sẽ được xây dựng với các chức năng tương tự như các nền tảng Airbnb/Agoda:

### Khách hàng (Customer) 
- **Tìm kiếm nâng cao**: Tìm kiếm homestay theo thành phố/địa điểm, khoảng giá, và sức chứa (số người).
- **Xem chi tiết & Tiện ích**: Xem chi tiết homestay kèm danh sách tiện ích (Wifi, Bếp, Hồ bơi,...), số giường, số phòng tắm.
- **Đặt phòng & Thanh toán**: Đặt phòng với thuật toán chống trùng lịch. Trạng thái thanh toán mặc định là Chưa thanh toán.
- **Đánh giá & Nhận xét**: Cho phép khách hàng để lại rating (1-5 sao) và nhận xét (Review) sau khi dùng dịch vụ.
- **Quản lý cá nhân**: Xem lịch sử, cập nhật thông tin cá nhân.

### Quản trị viên (Admin)
- **Dashboard**: Thống kê doanh thu, tổng số đặt phòng, số lượng user.
- **Quản trị danh mục & Tiện ích**: Quản lý các loại phòng (Villa, Căn hộ, Nhà riêng) và các Tiện ích (Amenities).
- **Quản lý Homestay**: Đăng ảnh, thiết lập giá, mô tả, sức chứa, tùy chọn thêm tiện ích cho từng homestay.
- **Xử lý Đặt phòng**: Xác nhận, Hủy, Cập nhật trạng thái "Đã thanh toán", "Đã trả phòng".

## 2. Công nghệ sử dụng
- **Backend:** Java 17, Spring Boot (Spring Web, Spring Data JPA), Spring Security (Phân quyền bảo mật Admin & Customer).
- **Frontend:** Thymeleaf (Server-side rendering), HTML/CSS/JS thuần, sử dụng Flexbox/Grid thiết kế UI/UX hiện đại, có slide ảnh, glassmorphism.
- **Database:** MySQL.
- **Build Tool:** Gradle.
- **IDE:** IntelliJ IDEA.

## 3. Cấu trúc Cơ Sở Dữ Liệu SQL (7 Bảng)

1. **`users`**: ID, Username, Password, Role (ADMIN/CUSTOMER), FullName, Phone, Email, Avatar.
2. **`categories`** (Loại chỗ ở): ID, Name (VD: Villa, Nhà gỗ, Căn hộ).
3. **`amenities`** (Tiện ích): ID, Name, Icon (VD: Wifi, Điều hòa, Bếp, Lò nướng).
4. **`homestays`**: ID, CategoryID, Name, Description, PricePerNight, Address, City, Status, ImageUrl, Capacity (người đa), BedCount, BathCount.
5. **`homestay_amenities`**: Bảng trung gian n-n nối homestay và tiện ích.
6. **`bookings`**: ID, UserID, HomestayID, CheckInDate, CheckOutDate, TotalPrice, PaymentStatus (UNPAID, PAID), Status (PENDING, APPROVED, CHECKED_IN, COMPLETED, CANCELED).
7. **`reviews`**: ID, UserID, HomestayID, Rating(1-5 sao), Comment, CreatedDate.

## 4. Các bước xây dựng

**Bước 1: Project Setup (Gradle)**
- Tạo project sử dụng Spring Initializr với Gradle, Java 17 vào thư mục `d:\homstay`.
- Cấu hình file `application.properties` để trỏ vào MySQL.

**Bước 2: Chuẩn bị Database**
- Tạo file `database.sql` bên trong source code. File này chứa toàn bộ các script tạo bảng (DDL) cùng với dữ liệu mẫu (DML) như tài khoản admin, khách hàng, 5 homestay mẫu thuộc các danh mục khác nhau.

**Bước 3: Lập trình Backend (Java)**
- Định nghĩa các cột Entity kết nối với bảng trong DB.
- Định nghĩa Repository Interface.
- Định nghĩa Services xử lý nghiệp vụ (Ví dụ: Service kiểm tra phòng trống).
- Viết Controllers nhận Request.
- Cấu hình file `SecurityConfig.java` để bảo mật.

**Bước 4: Thiết kế Frontend (HTML/CSS/Thymeleaf)**
- Viết Layout chuẩn (Header mờ thả nổi, Footer thông tin).
- Code Trang chủ tìm kiếm Homestay, có icon bắt mắt.
- Code Trang chi tiết Homestay và form cho phép chọn ngày, nhập số người.
- Code Giao diện Quản trị viên (Admin Dashboard) tiện lợi cho việc theo dõi doanh thu.
