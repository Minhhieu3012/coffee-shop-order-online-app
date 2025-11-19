// js/router.js
// ===============================
// 1. Import Firebase Auth
// ===============================
import { auth } from "./firebase-config.js";
import {
  onAuthStateChanged,
  signOut,
} from "https://www.gstatic.com/firebasejs/10.12.0/firebase-auth.js";

// ===============================
// 2. AUTH GUARD cho admin.html
//    - Nếu chưa login: đá về login.html
//    - TODO: Sau này check role 'admin' ở đây
// ===============================
onAuthStateChanged(auth, (user) => {
  if (!user) {
    // Chưa đăng nhập → quay lại trang login
    window.location.href = "login.html";
  } else {
    console.log("Admin đang đăng nhập:", user.email);
    // TODO:
    //  - Lấy thêm thông tin user từ Firestore (role, displayName, avatar...)
    //  - Nếu role !== 'admin' thì signOut + redirect về trang khác
  }
});

// ===============================
// 3. SPA router đơn giản cho admin
// ===============================
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

// Đổi tab
function setRoute(route) {
  // Nếu route không hợp lệ thì về dashboard
  if (!route || !document.getElementById(`page-${route}`)) {
    route = "dashboard";
  }

  // Active menu
  sidebarMenuItems.forEach((btn) => {
    btn.classList.toggle("active", btn.dataset.route === route);
  });

  // Active page
  pages.forEach((page) => {
    page.classList.toggle("active", page.id === `page-${route}`);
  });

  // Đổi title trên topbar
  if (pageTitleEl) {
    pageTitleEl.textContent = routeTitleMap[route] || "Dashboard";
  }

  // Lưu route vào hash để reload lại vẫn giữ tab
  if (window.location.hash !== `#${route}`) {
    window.location.hash = route;
  }
}

// Gắn event click cho menu
sidebarMenuItems.forEach((btn) => {
  btn.addEventListener("click", () => {
    const route = btn.dataset.route;
    setRoute(route);
  });
});

// Khi load trang lần đầu → đọc hash
const initialRoute = window.location.hash.replace("#", "") || "dashboard";
setRoute(initialRoute);

// ===============================
// 4. Logout
// ===============================
if (btnLogout) {
  btnLogout.addEventListener("click", async () => {
    try {
      await signOut(auth);
      // Sau khi signOut, onAuthStateChanged sẽ tự redirect về login.html
    } catch (err) {
      console.error("Logout error:", err);
      alert("Không thể đăng xuất, thử lại sau.");
    }
  });
}
