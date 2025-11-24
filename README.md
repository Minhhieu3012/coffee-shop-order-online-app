# ☕ Bros Coffee Shop Order Online App

> Một giải pháp đặt đồ uống và quản lý cửa hàng cà phê trực tuyến toàn diện.

Ứng dụng đặt đồ uống trực tuyến dành cho chuỗi cửa hàng cà phê **Bros Coffee**, cho phép khách hàng đặt hàng nhanh chóng, theo dõi đơn hàng, và tích điểm thưởng. Đồng thời, hệ thống có giao diện quản trị (Admin Web) giúp quản lý sản phẩm, đơn hàng, doanh thu và khách hàng một cách hiệu quả.

---

## 🚀 Tổng quan dự án

### Mục tiêu

- Xây dựng hệ thống đặt hàng trực tuyến cho cửa hàng cà phê.
- Cung cấp trải nghiệm người dùng thân thiện, trực quan trên nền tảng di động.
- Quản lý tập trung sản phẩm, đơn hàng và doanh thu qua giao diện web dành cho admin.

## 🖥️ Công nghệ sử dụng

| Thành phần                | Công nghệ                                                                     |
| ------------------------- | ----------------------------------------------------------------------------- |
| **Backend**               | Firebase (Authentication, Firestore Database, Cloud Storage, Cloud Messaging) |
| **Mobile (Customer App)** | Kotlin, Jetpack Compose                                                       |
| **Admin (Web)**           | HTML, CSS, JavaScript                                                         |
| **Design**                | Figma (UI/UX Design)                                                          |
| **Version Control**       | Git                                                                           |
| **Testing & Debugging**   | Firebase Emulator Suite (optional), Android Emulator                          |

---

## 🧱 Kiến trúc hệ thống

### 1. Admin (Web)

Giao diện quản trị cho phép:

- Quản lý **sản phẩm**, **danh mục**, **đơn hàng**, **khách hàng**.
- Theo dõi **doanh thu**, **báo cáo bán hàng**, **thống kê phân tích**.
- Xem chi tiết **hóa đơn**, **hoàn tiền**, và **tình trạng đơn hàng**.

### 2. Customer (Mobile App)

Ứng dụng khách hàng hỗ trợ:

- Đăng ký, đăng nhập, xác thực OTP.
- Xem menu, lọc và tìm kiếm đồ uống.
- Thêm sản phẩm vào giỏ hàng và thanh toán trực tuyến.
- Theo dõi đơn hàng, đánh giá, tích điểm và nhận thông báo.

---

## ⚙️ Cấu trúc thư mục

```plaintext
BrosCoffeeShop/
├── admin-web/           # Giao diện quản trị (HTML, CSS, JS)
├── customer-mobile/     # Ứng dụng khách hàng (Jetpack Compose)
├── backend-firebase/    # Cấu hình & dịch vụ Firebase
├── design/              # File thiết kế Figma, tài nguyên UI
└── docs/                # Tài liệu dự án
```

---

## 👨‍💻 Nhóm phát triển

- **Backend Developer:** Phan Minh Hiếu – Firebase integration & API logic
- **Frontend Developer (Admin Web):** Nguyễn Hà Gia Huy – Web interface & dashboard
- **Mobile UI/UX Designer:** Lại Đức Thành – Thiết kế & phát triển giao diện Customer App

---

## 📜 Giấy phép

Dự án được phát triển phục vụ mục đích học tập và nội bộ.

© 2025 Bros Coffee ☕
