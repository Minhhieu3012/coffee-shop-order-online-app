// js/auth.js

// 1. Xử lý Đăng nhập
const loginForm = document.getElementById("loginForm");
const loginError = document.getElementById("loginError");

if (loginForm) {
  loginForm.addEventListener("submit", (e) => {
    e.preventDefault(); // Chặn việc load lại trang

    const email = loginForm.email.value;
    const password = loginForm.password.value;

    // --- GIẢ LẬP CHECK TÀI KHOẢN (Sau này thay bằng Firebase) ---
    if (email === "admin@gmail.com" && password === "123123") {
      // Lưu trạng thái "đã đăng nhập" vào bộ nhớ trình duyệt
      localStorage.setItem("isLoggedIn", "true");
      
      // Chuyển sang trang admin
      window.location.href = "admin.html"; 
    } else {
      // Hiện thông báo lỗi
      if (loginError) {
        loginError.style.display = "block";
        loginError.textContent = "Sai email hoặc mật khẩu! (Thử: admin@gmail.com / 123123)";
      }
    }
  });
}

// 2. Xử lý ẩn/hiện mật khẩu (Cái icon con mắt)
const togglePasswordBtn = document.getElementById("togglePassword");
const passwordInput = document.getElementById("password");

if (togglePasswordBtn && passwordInput) {
  togglePasswordBtn.addEventListener("click", () => {
    // Kiểm tra loại hiện tại là password hay text
    const type = passwordInput.getAttribute("type") === "password" ? "text" : "password";
    passwordInput.setAttribute("type", type);

    // Đổi icon (mắt mở / mắt chéo)
    const icon = togglePasswordBtn.querySelector("i");
    if (type === "text") {
      icon.classList.remove("fa-eye");
      icon.classList.add("fa-eye-slash");
    } else {
      icon.classList.remove("fa-eye-slash");
      icon.classList.add("fa-eye");
    }
  });
}