-- ==========================================
-- HOMESTAY BOOKING SYSTEM - SQL SERVER SCRIPT
-- ==========================================

-- 1. Table users
CREATE TABLE users (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL DEFAULT 'CUSTOMER', -- ADMIN, CUSTOMER
    full_name NVARCHAR(100) NOT NULL,
    phone VARCHAR(20),
    email VARCHAR(100),
    avatar VARCHAR(255),
    created_at DATETIME2 DEFAULT GETDATE()
);

-- 2. Table categories
CREATE TABLE categories (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    name NVARCHAR(100) NOT NULL UNIQUE,
    description NVARCHAR(MAX),
    created_at DATETIME2 DEFAULT GETDATE()
);

-- 3. Table amenities
CREATE TABLE amenities (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    name NVARCHAR(100) NOT NULL UNIQUE,
    icon VARCHAR(50), -- Tên class của thẻ i (vd: fa-solid fa-wifi)
    created_at DATETIME2 DEFAULT GETDATE()
);

-- 4. Table homestays
CREATE TABLE homestays (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    category_id BIGINT NOT NULL,
    name NVARCHAR(255) NOT NULL,
    description NVARCHAR(MAX),
    price_per_night DECIMAL(18,2) NOT NULL,
    address NVARCHAR(255) NOT NULL,
    city NVARCHAR(100) NOT NULL,
    status VARCHAR(20) DEFAULT 'AVAILABLE', -- AVAILABLE, MAINTENANCE
    image_url VARCHAR(255),
    capacity INT NOT NULL DEFAULT 1,
    bed_count INT NOT NULL DEFAULT 1,
    bath_count INT NOT NULL DEFAULT 1,
    created_at DATETIME2 DEFAULT GETDATE(),
    FOREIGN KEY (category_id) REFERENCES categories(id)
);

-- 5. Table homestay_amenities
CREATE TABLE homestay_amenities (
    homestay_id BIGINT NOT NULL,
    amenity_id BIGINT NOT NULL,
    PRIMARY KEY (homestay_id, amenity_id),
    FOREIGN KEY (homestay_id) REFERENCES homestays(id) ON DELETE CASCADE,
    FOREIGN KEY (amenity_id) REFERENCES amenities(id) ON DELETE CASCADE
);

-- 6. Table bookings
CREATE TABLE bookings (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    user_id BIGINT NOT NULL,
    homestay_id BIGINT NOT NULL,
    check_in_date DATE NOT NULL,
    check_out_date DATE NOT NULL,
    guest_count INT NOT NULL DEFAULT 1,
    total_price DECIMAL(18,2) NOT NULL,
    coupon_code VARCHAR(50),
    discount_amount DECIMAL(18,2) DEFAULT 0,
    payment_status VARCHAR(20) DEFAULT 'UNPAID', -- UNPAID, PAID, REFUNDED
    status VARCHAR(20) DEFAULT 'PENDING', -- PENDING, APPROVED, CHECKED_IN, COMPLETED, CANCELED
    note NVARCHAR(MAX),
    created_at DATETIME2 DEFAULT GETDATE(),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (homestay_id) REFERENCES homestays(id)
);

-- 7. Table reviews
CREATE TABLE reviews (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    user_id BIGINT NOT NULL,
    homestay_id BIGINT NOT NULL,
    booking_id BIGINT NOT NULL,
    rating INT NOT NULL CHECK (rating >= 1 AND rating <= 5),
    comment NVARCHAR(MAX),
    created_date DATETIME2 DEFAULT GETDATE(),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (homestay_id) REFERENCES homestays(id),
    FOREIGN KEY (booking_id) REFERENCES bookings(id)
);


-- ==========================================
-- DỮ LIỆU MẪU (SEED DATA)
-- ==========================================

-- Mật khẩu mặc định là '123456'. Phải mã hóa bằng BCrypt để Spring Security đọc được.
-- Mã hóa BCrypt của '123456' là: $2a$12$K1r6t30l/Yf.bQ.i6S1BvuaT2n.lS.Qp0v/p4iK12E6m.S3aP0lJq

INSERT INTO users (username, password, role, full_name, phone, email) VALUES
('admin', '$2a$12$K1r6t30l/Yf.bQ.i6S1BvuaT2n.lS.Qp0v/p4iK12E6m.S3aP0lJq', 'ADMIN', N'Quản Trị Viên', '0901234567', 'admin@homestay.com'),
('khachhang', '$2a$12$K1r6t30l/Yf.bQ.i6S1BvuaT2n.lS.Qp0v/p4iK12E6m.S3aP0lJq', 'CUSTOMER', N'Nguyen Van Khach', '0909876543', 'khachhang@gmail.com');

INSERT INTO categories (name, description) VALUES
(N'Villa', N'Biệt thự sân vườn cao cấp với đầy đủ tiện nghi'),
(N'Nhà nguyên căn', N'Nhà riêng tư có không gian rộng rãi cho nhóm đông người'),
(N'Căn hộ Studio', N'Căn hộ nhỏ gọn, hiện đại dành cho 1-2 người');

INSERT INTO amenities (name, icon) VALUES
(N'Wifi miễn phí', 'fa-solid fa-wifi'),
(N'Hồ bơi', 'fa-solid fa-water-ladder'),
(N'Bếp nhỏ', 'fa-solid fa-kitchen-set'),
(N'Máy lạnh', 'fa-solid fa-snowflake'),
(N'Bãi đậu xe', 'fa-solid fa-square-parking'),
(N'BBQ ngoài trời', 'fa-solid fa-fire-burner');

-- Thêm một số homestay
INSERT INTO homestays (category_id, name, description, price_per_night, address, city, image_url, capacity, bed_count, bath_count) VALUES
(1, N'Villa Đà Lạt Mộng Mơ', N'Villa yên tĩnh giữa rừng thông, view thung lũng tuyệt đẹp.', 1500000, N'Khu phố Pháp, Phường 10', N'Đà Lạt', 'https://images.pexels.com/photos/106399/pexels-photo-106399.jpeg?auto=compress&cs=tinysrgb&w=800', 8, 4, 3),
(2, N'Nhà gỗ ven Hồ Tây', N'Nhà gỗ thiết kế ấm cúng, phù hợp cho tiệc nướng cuối tuần.', 800000, N'Trích Sài', N'Hà Nội', 'https://images.pexels.com/photos/1370704/pexels-photo-1370704.jpeg?auto=compress&cs=tinysrgb&w=800', 6, 3, 2),
(3, N'Căn hộ Studio Quận 1', N'Căn hộ cao cấp ngay trung tâm, đi bộ 5 phút ra phố đi bộ Nguyễn Huệ.', 650000, N'Đường Pastuer, Quận 1', N'TP HCM', 'https://images.pexels.com/photos/271618/pexels-photo-271618.jpeg?auto=compress&cs=tinysrgb&w=800', 2, 1, 1),
(1, N'Sapa Jade Hill Villa', N'Villa đậm chất Tây Bắc, nằm sát sườn đồi, săn mây cực đỉnh.', 2500000, N'Lao Chải, Tả Van', N'Sapa', 'https://images.pexels.com/photos/147411/italy-mountains-dawn-daybreak-147411.jpeg?auto=compress&cs=tinysrgb&w=800', 10, 5, 4);

-- Thêm tiện ích cho homestay (Villa ĐL: wifi, bếp, xe, BBQ) (id=1, 3, 5, 6)
INSERT INTO homestay_amenities (homestay_id, amenity_id) VALUES
(1, 1), (1, 3), (1, 5), (1, 6),
(2, 1), (2, 4), (2, 6),
(3, 1), (3, 4),
(4, 1), (4, 3), (4, 4), (4, 5);

-- Thêm mẫu 1 Booking đã hoàn thành và 1 Booking đang chờ duyệt
INSERT INTO bookings (user_id, homestay_id, check_in_date, check_out_date, guest_count, total_price, payment_status, status) VALUES
(2, 3, '2023-11-01', '2023-11-03', 2, 1300000, 'PAID', 'COMPLETED'),
(2, 1, '2024-12-24', '2024-12-26', 4, 3000000, 'UNPAID', 'PENDING');

-- Thêm mẫu review
INSERT INTO reviews (user_id, homestay_id, booking_id, rating, comment) VALUES
(2, 3, 1, 5, N'Phòng sạch sẽ, gọn gàng, chủ nhà rất nhiệt tình hỗ trợ lúc đi lạc.');

-- ==========================================
-- THÊM 5 CHỖ CHO MỖI NƠI (Đà Lạt, Hà Nội, TP HCM, Sapa, Vũng Tàu, Phú Quốc)
-- ==========================================

-- Đà Lạt
INSERT INTO homestays (category_id, name, description, price_per_night, address, city, image_url, capacity, bed_count, bath_count) VALUES
(1, N'Pine Hill Homestay', N'Nhà trên cây giữa rừng thông, không gian yên tĩnh và lãng mạn.', 1200000, N'Đường Triệu Việt Vương', N'Đà Lạt', 'https://images.unsplash.com/photo-1542718610-a1d656d1884c', 4, 2, 1),
(2, N'The Kupid Homestay', N'View thung lũng cực đẹp, có bồn tắm lộ thiên ngắm mây.', 850000, N'Đặng Thái Thân', N'Đà Lạt', 'https://images.unsplash.com/photo-1505691938895-1758d7eaa511', 2, 1, 1),
(1, N'Légume Homestay', N'Kiến trúc nhà khối màu sắc độc đáo, khu vườn rộng lớn.', 950000, N'Đường Bạch Đằng', N'Đà Lạt', 'https://images.unsplash.com/photo-1449156001935-d247926b64b9', 4, 2, 2),
(3, N'In The Pines Home', N'Căn nhà gỗ cổ điển mang đậm phong cách cao nguyên.', 750000, N'Vạn Hạnh', N'Đà Lạt', 'https://images.unsplash.com/photo-1510798831971-661eb04b3739', 2, 1, 1),
(2, N'Woodline Villa Dalat', N'Biệt thự ven suối, không gian trong lành chuẩn nghỉ dưỡng.', 1800000, N'Đường Khe Sanh', N'Đà Lạt', 'https://images.unsplash.com/photo-1493663284031-b7e3aefcae8e', 6, 3, 2);

-- Hà Nội
INSERT INTO homestays (category_id, name, description, price_per_night, address, city, image_url, capacity, bed_count, bath_count) VALUES
(3, N'Old Quarter Loft', N'Căn hộ tầng mái nhìn ra phố cổ nhộn nhịp.', 700000, N'Hàng Bè', N'Hà Nội', 'https://images.unsplash.com/photo-1522708323590-d24dbb6b0267', 2, 1, 1),
(2, N'Garden House West Lake', N'Nhà sân vườn rộng rãi cạnh Hồ Tây lộng gió.', 1300000, N'Quảng An', N'Hà Nội', 'https://images.unsplash.com/photo-1502672260266-1c1ef2d93688', 6, 3, 2),
(3, N'The Typography Studio', N'Studio thiết kế hiện đại ngay gần hồ Hoàn Kiếm.', 850000, N'Lý Quốc Sư', N'Hà Nội', 'https://images.unsplash.com/photo-1536376074432-bf63fa47b312', 2, 1, 1),
(2, N'French Colonial Villa', N'Biệt thự cổ phong cách Pháp giữa lòng Hà Nội.', 2200000, N'Trần Hưng Đạo', N'Hà Nội', 'https://images.unsplash.com/photo-1512917774080-9991f1c4c750', 8, 4, 3),
(3, N'Hidden Gem Studio', N'Studio nhỏ xinh trong ngõ nhỏ yên bình.', 600000, N'Ngọc Hà', N'Hà Nội', 'https://images.unsplash.com/photo-1493809842364-78817add7ffb', 2, 1, 1);

-- TP HCM
INSERT INTO homestays (category_id, name, description, price_per_night, address, city, image_url, capacity, bed_count, bath_count) VALUES
(3, N'Landmark 81 Luxury View', N'Căn hộ cao cấp view toàn cảnh thành phố từ Landmark 81.', 2500000, N'Vinhomes Central Park', N'TP HCM', 'https://images.unsplash.com/photo-1460317442147-3bd05b5ec63c', 4, 2, 1),
(3, N'Vintage Indo-China Flat', N'Căn hộ phong cách Đông Dương ngay chợ Bến Thành.', 1100000, N'Lý Tự Trọng', N'TP HCM', 'https://images.unsplash.com/photo-1501183307537-821228a6df23', 2, 1, 1),
(2, N'Thao Dien Garden Villa', N'Villa sân vườn sang trọng tại khu nhà giàu Thảo Điền.', 4500000, N'Thảo Điền, Quận 2', N'TP HCM', 'https://images.unsplash.com/photo-1512917774080-9991f1c4c750', 10, 5, 5),
(3, N'Saigon Signature Studio', N'Studio đầy đủ tiện nghi, hồ bơi sân thượng cực chill.', 950000, N'Bến Vân Đồn, Quận 4', N'TP HCM', 'https://images.unsplash.com/photo-1444201983204-c43cbd584d93', 2, 1, 1),
(3, N'Muji Style Minimalist', N'Căn hộ tối giản phong cách Nhật Bản.', 800000, N'Quận 7', N'TP HCM', 'https://images.unsplash.com/photo-1513694203232-719a280e022f', 2, 1, 1);

-- Sapa
INSERT INTO homestays (category_id, name, description, price_per_night, address, city, image_url, capacity, bed_count, bath_count) VALUES
(2, N'Lee House Sapa', N'Nhà gỗ giữa bản Tả Van, trải nghiệm đời sống địa phương.', 550000, N'Bản Tả Van', N'Sapa', 'https://images.unsplash.com/photo-1582719508461-905c673771fd', 4, 1, 1),
(1, N'The Mong Village Resort', N'Khu nghỉ dưỡng đậm chất dân tộc Mông, view thung lũng Mường Hoa.', 3200000, N'Cầu Mây', N'Sapa', 'https://images.unsplash.com/photo-1470770841072-f978cf4d019e', 2, 1, 1),
(2, N'Rùa Home', N'Căn nhà nhỏ yên bình trên đỉnh đồi săn mây.', 800000, N'Ý Linh Hồ', N'Sapa', 'https://images.unsplash.com/photo-1500382017468-9049fee74a62', 2, 1, 1),
(2, N'Little Sapa Homestay', N'Trung tâm thị trấn, thuận tiện đi lại và tham quan.', 450000, N'Cầu Mây', N'Sapa', 'https://images.unsplash.com/photo-1464822759023-fed622ff2c3b', 2, 1, 1),
(2, N'Ta Phin Stone House', N'Nhà đá độc đáo của người Dao đỏ.', 1200000, N'Bản Tả Phìn', N'Sapa', 'https://images.unsplash.com/photo-1472214103451-9374bd1c798e', 6, 3, 2);

-- Vũng Tàu
INSERT INTO homestays (category_id, name, description, price_per_night, address, city, image_url, capacity, bed_count, bath_count) VALUES
(1, N'Seaside Luxury Villa', N'Villa sát biển với hồ bơi vô cực nhìn ra đại dương.', 5500000, N'Trần Phú', N'Vũng Tàu', 'https://images.unsplash.com/photo-1499793983690-e29da59ef1c2', 12, 6, 5),
(3, N'Ocean View Studio', N'Studio nhỏ gọn mặt biển, đón bình minh tại giường.', 1200000, N'Thùy Vân', N'Vũng Tàu', 'https://images.unsplash.com/photo-1520250497591-112f2f40a3f4', 2, 1, 1),
(2, N' Santorini House Vung Tau', N'Phong cách Hy Lạp rực rỡ, góc sống ảo cực chuẩn.', 1500000, N'Phan Chu Trinh', N'Vũng Tàu', 'https://images.unsplash.com/photo-1512917774080-9991f1c4c750', 6, 3, 2),
(2, N'Dreamy Chill Home', N'Homestay decor xinh xắn cho nhóm bạn trẻ.', 900000, N'Lê Hồng Phong', N'Vũng Tàu', 'https://images.unsplash.com/photo-1513694203232-719a280e022f', 4, 2, 1),
(1, N'The Wind Boutique Villa', N'Biệt thự đồi view biển, không gian biệt lập và yên tĩnh.', 3800000, N'Đường Vi Ba', N'Vũng Tàu', 'https://images.unsplash.com/photo-1432303491101-dc1697223e75', 8, 4, 4);

-- Phú Quốc
INSERT INTO homestays (category_id, name, description, price_per_night, address, city, image_url, capacity, bed_count, bath_count) VALUES
(2, N'Lotus Home Phu Quoc', N'Nhà gỗ trên mặt nước tại làng chài Hàm Ninh.', 1800000, N'Hàm Ninh', N'Phú Quốc', 'https://images.unsplash.com/photo-1540541338287-41700207dee6', 4, 2, 1),
(1, N'Sunset Sanato Villa', N'Nghỉ dưỡng tại bãi biển ngắm hoàng hôn đẹp nhất đảo.', 4200000, N'Bãi Trường', N'Phú Quốc', 'https://images.unsplash.com/photo-1520250497591-112f2f40a3f4', 6, 3, 3),
(2, N'The May Homestay', N'Nhà vườn xanh mát ngay trung tâm thị trấn Dương Đông.', 750000, N'Dương Đông', N'Phú Quốc', 'https://images.unsplash.com/photo-1445019980597-93fa8acb246c', 2, 1, 1),
(3, N'An Thoi Harbour Studio', N'Studio hiện đại nhìn ra ga cáp treo Hòn Thơm.', 1100000, N'An Thới', N'Phú Quốc', 'https://images.unsplash.com/photo-1533759413974-9e15f3b745ac', 2, 1, 1),
(1, N'Mango Bay Cottage', N'Trải nghiệm nghỉ dưỡng mộc mạc bên bờ biển hoang sơ.', 3500000, N'Ông Lang', N'Phú Quốc', 'https://images.unsplash.com/photo-1499793983690-e29da59ef1c2', 2, 1, 1);
