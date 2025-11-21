// admin-web/js/router.js
import { auth } from "./firebase-config.js";
import { onAuthStateChanged, signOut } from "https://www.gstatic.com/firebasejs/10.8.0/firebase-auth.js";

// 1. AUTH GUARD (BẢO VỆ TRANG ADMIN)
// Lắng nghe trạng thái đăng nhập từ Firebase
onAuthStateChanged(auth, (user) => {
  const currentPath = window.location.pathname;

  if (user) {
    // Đã đăng nhập
    console.log("User đang online:", user.email);
    
    // Nếu đang ở trang login thì đẩy vào admin
    if (currentPath.includes("login.html")) {
      window.location.href = "admin.html";
    }
  } else {
    // Chưa đăng nhập (hoặc vừa đăng xuất)
    console.log("User chưa đăng nhập");
    
    // Nếu đang ở trang admin thì đá về login
    if (currentPath.includes("admin.html")) {
      window.location.href = "login.html";
    }
  }
});

// 2. XỬ LÝ ĐĂNG XUẤT
const btnLogout = document.getElementById("btnLogout");
if (btnLogout) {
  btnLogout.addEventListener("click", async () => {
    try {
      if (confirm("Bạn muốn đăng xuất?")) {
        await signOut(auth); // Gọi hàm đăng xuất của Firebase
        // onAuthStateChanged sẽ tự bắt sự kiện này và chuyển về login.html
      }
    } catch (error) {
      console.error("Lỗi đăng xuất:", error);
      alert("Không thể đăng xuất: " + error.message);
    }
  });
}

// 3. LOGIC CHUYỂN TAB (Giữ nguyên code cũ của bạn)
const routeTitleMap = {
  dashboard: "Dashboard",
  users: "Quản lý users",
  orders: "Quản lý đơn hàng",
  products: "Quản lý sản phẩm",
};

const sidebarMenuItems = document.querySelectorAll(".menu-item[data-route]");
const pages = document.querySelectorAll(".page");
const pageTitleEl = document.querySelector(".page-title");

function setRoute(route) {
  if (!route || !document.getElementById(`page-${route}`)) {
    route = "dashboard";
  }
  sidebarMenuItems.forEach((btn) => {
    btn.classList.toggle("active", btn.dataset.route === route);
  });
  pages.forEach((page) => {
    page.classList.toggle("active", page.id === `page-${route}`);
  });
  if (pageTitleEl) {
    pageTitleEl.textContent = routeTitleMap[route] || "Dashboard";
  }
  if (window.location.hash !== `#${route}`) {
    window.location.hash = route;
  }
}

sidebarMenuItems.forEach((btn) => {
  btn.addEventListener("click", () => {
    setRoute(btn.dataset.route);
  });
});

const initialRoute = window.location.hash.replace("#", "") || "dashboard";
if (sidebarMenuItems.length > 0) {
    setRoute(initialRoute);
}