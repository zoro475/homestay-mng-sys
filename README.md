# homestay-mng-sys

Hệ thống quản lý khách sạn (Hotel Management System) đầy đủ tính năng, tập trung vào quản lý đặt phòng, phòng, dịch vụ, mã giảm giá và thanh toán.

Dự án bao gồm thiết kế cơ sở dữ liệu (Database Schema) được viết bằng **DBML** và hỗ trợ xây dựng backend (ASP.NET Core, Entity Framework Core...).

---

## ✨ Tính năng chính

### Quản lý phân quyền (RBAC)
- Hệ thống Role & Permission linh hoạt
- Hỗ trợ nhiều vai trò: Admin, Manager, Receptionist, Customer

### Quản lý người dùng
- Đăng ký, đăng nhập (PasswordHash)
- Blacklist khách hàng
- Chương trình khách hàng thân thiết (Loyalty Points)

### Quản lý phòng
- Loại phòng (RoomTypes) với giá cơ bản
- Quản lý phòng chi tiết (trạng thái: Available, Occupied, Dirty, Maintenance...)
- Hỗ trợ nhiều ảnh cho mỗi phòng

### Đặt phòng & Thanh toán
- Đặt phòng theo ngày (Check-in / Check-out)
- Áp dụng mã giảm giá (Coupon)
- Quản lý dịch vụ bổ sung (Spa, Giặt ủi, Ăn sáng...)
- Thanh toán linh hoạt (tiền mặt, chuyển khoản, ví điện tử...)

### Mã giảm giá (Coupon)
- Hỗ trợ giảm %, giảm cố định
- Giới hạn số lần sử dụng, giới hạn theo người dùng
- Theo dõi lịch sử sử dụng (`CouponUsages`)

### Khác
- Đánh giá & nhận xét từ khách hàng
- Audit trail (CreatedAt, UpdatedAt, CreatedBy)

---

## 📁 Cấu trúc dự án
Hotel-Management-System/
├── Database/               # Schema DBML và script SQL
├── src/                    # Source code backend (nếu có)
├── docs/                   # Tài liệu thiết kế
├── .gitignore
├── README.md
└── ...

- **Database Schema**: Được định nghĩa bằng ngôn ngữ **DBML** (dễ đọc, dễ chuyển sang SQL).
- Các bảng chính: Users, Roles, Rooms, Bookings, Payments, Services, Coupons...

---

## 🛠️ Công nghệ sử dụng

- **Database Design**: DBML (Database Markup Language)
- **Backend**: ASP.NET Core (khuyến nghị), Entity Framework Core
- **Database**: SQL Server / PostgreSQL / MySQL
- **Authentication**: JWT hoặc Identity
- **Frontend** (tương lai): React, Blazor, Angular...

---

## 🚀 Cách chạy dự án

### 1. Clone repository
```bash
git clone https://github.com/yourusername/hotel-management-system.git
cd hotel-management-system
```
### 2. Database

Cài đặt dbdiagram.io hoặc công cụ hỗ trợ DBML
Import file erd nhóm 4.dbml để xem ERD
Generate SQL script và chạy trên DBMS của bạn
### 3. Backend (sau này)

Chạy dự án: ...
