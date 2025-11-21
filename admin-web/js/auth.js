// admin-web/js/auth.js
import { auth } from "./firebase-config.js";
import { signInWithEmailAndPassword } from "https://www.gstatic.com/firebasejs/10.8.0/firebase-auth.js";

// 1. XỬ LÝ ĐĂNG NHẬP
const loginForm = document.getElementById("loginForm");
const loginError = document.getElementById("loginError");
const btnLogin = document.getElementById("btnLogin");

if (loginForm) {
  loginForm.addEventListener("submit", async (e) => {
    e.preventDefault(); // Chặn load lại trang

    const email = loginForm.email.value;
    const password = loginForm.password.value;

    // Hiệu ứng đang tải
    const originalText = btnLogin.innerText;
    btnLogin.innerText = "Đang xử lý...";
    btnLogin.disabled = true;
    if (loginError) loginError.style.display = "none";

    try {
      // --- GỌI FIREBASE AUTH ---
      const userCredential = await signInWithEmailAndPassword(auth, email, password);
      const user = userCredential.user;
      
      console.log("Đăng nhập thành công:", user.email);
      
      // Chuyển hướng sang trang Admin
      window.location.href = "admin.html"; 

    } catch (error) {
      console.error("Lỗi đăng nhập:", error);
      
      // Hiển thị thông báo lỗi thân thiện
      if (loginError) {
        loginError.style.display = "block";
        let msg = "Đăng nhập thất bại!";
        
        if (error.code === "auth/invalid-credential" || error.code === "auth/user-not-found" || error.code === "auth/wrong-password") {
          msg = "Sai email hoặc mật khẩu.";
        } else if (error.code === "auth/too-many-requests") {
          msg = "Thử lại quá nhiều lần. Hãy đợi một lát.";
        }
        
        loginError.textContent = msg;
      }
    } finally {
      // Trả lại nút bấm
      btnLogin.innerText = originalText;
      btnLogin.disabled = false;
    }
  });
}

// 2. XỬ LÝ ẨN/HIỆN MẬT KHẨU (Giữ nguyên logic cũ)
const togglePasswordBtn = document.getElementById("togglePassword");
const passwordInput = document.getElementById("password");

if (togglePasswordBtn && passwordInput) {
  togglePasswordBtn.addEventListener("click", () => {
    const type = passwordInput.getAttribute("type") === "password" ? "text" : "password";
    passwordInput.setAttribute("type", type);

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