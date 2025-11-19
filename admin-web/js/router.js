// js/router.js

// --- 1. KIỂM TRA ĐĂNG NHẬP (AUTH GUARD) ---
// Logic: Nếu không tìm thấy 'isLoggedIn' trong bộ nhớ -> Đá về trang login
const isLoggedIn = localStorage.getItem("isLoggedIn");
const currentPath = window.location.pathname;

// Nếu đang ở trang admin.html mà chưa đăng nhập
if (currentPath.includes("admin.html") && !isLoggedIn) {
  window.location.href = "login.html";
}

// --- 2. XỬ LÝ MENU CHUYỂN TAB (GIỮ NGUYÊN CỦA BẠN) ---
const routeTitleMap = {
  dashboard: "Dashboard",
  users: "Quản lý users",
  orders: "Quản lý đơn hàng",
  products: "Quản lý sản phẩm",
};

const sidebarMenuItems = document.querySelectorAll(".menu-item[data-route]");
const pages = document.querySelectorAll(".page");
const pageTitleEl = document.querySelector(".page-title");
const btnLogout = document.getElementById("btnLogout");

function setRoute(route) {
  if (!route || !document.getElementById(`page-${route}`)) {
    route = "dashboard";
  }

  // Active menu
  sidebarMenuItems.forEach((btn) => {
    btn.classList.toggle("active", btn.dataset.route === route);
  });

  // Active page content
  pages.forEach((page) => {
    page.classList.toggle("active", page.id === `page-${route}`);
  });

  // Đổi tên tiêu đề
  if (pageTitleEl) {
    pageTitleEl.textContent = routeTitleMap[route] || "Dashboard";
  }

  // Lưu vào hash (#)
  if (window.location.hash !== `#${route}`) {
    window.location.hash = route;
  }
}

// Bắt sự kiện click menu
sidebarMenuItems.forEach((btn) => {
  btn.addEventListener("click", () => {
    setRoute(btn.dataset.route);
  });
});

// Load lại trang vẫn giữ đúng tab
const initialRoute = window.location.hash.replace("#", "") || "dashboard";
if (sidebarMenuItems.length > 0) { // Chỉ chạy logic này nếu đang ở trang admin
    setRoute(initialRoute);
}

// --- 3. XỬ LÝ ĐĂNG XUẤT ---
if (btnLogout) {
  btnLogout.addEventListener("click", () => {
    // Xóa trạng thái đăng nhập
    localStorage.removeItem("isLoggedIn");
    // Quay về login
    window.location.href = "login.html";
  });
}