# 🎨 Diagram
➡️ Design: [View in Figma](https://www.figma.com/design/cvbcvBwDlz3Mzf9jEEeZG6/Group-LapTrinhThietBiDiDong?node-id=62-72&t=fh1oLhWss52ifzsV-1)




# ☕ Mô-đun: Admin Web – BrosCafe

## 1. Tổng quan

Mục tiêu:  
Xây dựng giao diện web quản trị cho ứng dụng BrosCafe, giúp Admin có thể quản lý toàn bộ hệ thống gồm sản phẩm, đơn hàng, nhân viên và thống kê doanh thu.

---

## 2. Vai trò & Chức năng

### 🔧 Vai trò chính
Admin chịu trách nhiệm quản lý toàn bộ hoạt động của hệ thống qua giao diện web:

- Quản lý sản phẩm
  - Thêm, sửa, xóa sản phẩm (CRUD)
  - Cập nhật giá, hình ảnh, trạng thái hàng tồn
  - Phân loại theo danh mục: cà phê, trà, bánh ngọt,...
  - Tìm kiếm, lọc theo từ khóa hoặc danh mục

- Quản lý đơn hàng
  - Xem danh sách và chi tiết từng đơn hàng
  - Cập nhật trạng thái đơn: `Pending → Processing → Completed`
  - Theo dõi lịch sử và doanh thu

- Dashboard & Thống kê
  - Hiển thị doanh thu theo ngày / tháng / năm
  - Thống kê top sản phẩm bán chạy
  - Biểu đồ trực quan bằng Chart.js

- Kết nối Firebase
  - Ghi / đọc dữ liệu từ Firestore
  - Upload ảnh lên Firebase Storage
  - Cập nhật realtime khi dữ liệu thay đổi

- Giao diện người dùng (UI/UX)
  - Giao diện thân thiện, dễ thao tác
  - Responsive trên các kích thước màn hình
  - Đảm bảo trải nghiệm mượt và trực quan

---

## 3. Công nghệ sử dụng

- HTML5, CSS3 (Bootstrap 5)
- JavaScript (ES6)
- Firebase SDK (Auth, Firestore, Storage)
- Chart.js – trực quan hóa dữ liệu
- Firebase Hosting – triển khai ứng dụng

---

## 4. Cấu trúc thư mục

```bash
admin-web/
├── assets/                 # Kho tài nguyên
│   └── logo.png            # Logo của quán
│
├── css/
│   ├── admin.css           # Style chính cho giao diện Dashboard
│   └── base.css            # Các thiết lập gốc (Root variables, Reset CSS)
│
├── js/
│   ├── auth.js             # Xử lý Đăng nhập & Authentication
│   ├── dashboard.js        # Logic thống kê Realtime
│   ├── firebase-config.js  # Cấu hình kết nối Firebase
│   ├── orders.js           # Quản lý Đơn hàng
│   ├── products.js         # Quản lý Sản phẩm
│   ├── router.js           # Bộ điều hướng & Bảo vệ trang
│   └── users.js            # Quản lý Khách hàng
│
├── admin.html              # Giao diện chính (Dashboard)
└── login.html              # Trang đăng nhập
