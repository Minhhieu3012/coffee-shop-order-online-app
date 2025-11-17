// js/auth.js
import { auth } from "./firebase-config.js";
import {
  signInWithEmailAndPassword,
  onAuthStateChanged,
} from "https://www.gstatic.com/firebasejs/10.12.0/firebase-auth.js";

const loginForm = document.getElementById("loginForm");
const emailInput = document.getElementById("email");
const passwordInput = document.getElementById("password");
const loginError = document.getElementById("loginError");
const btnLogin = document.getElementById("btnLogin");
const togglePassword = document.getElementById("togglePassword");

// 1. Nếu đã đăng nhập rồi thì khỏi vào login nữa -> đá qua admin.html
onAuthStateChanged(auth, (user) => {
  if (user) {
    // Sau này có check role admin thì thêm bước check Firestore ở đây
    window.location.href = "admin.html";
  }
});

// 2. Show / hide password
if (togglePassword) {
  togglePassword.addEventListener("click", () => {
    const isHidden = passwordInput.type === "password";
    passwordInput.type = isHidden ? "text" : "password";
    togglePassword.innerHTML = isHidden
      ? '<i class="fa-regular fa-eye-slash"></i>'
      : '<i class="fa-regular fa-eye"></i>';
  });
}

// Helper: set trạng thái loading nút login
function setLoading(isLoading) {
  if (!btnLogin) return;
  if (isLoading) {
    btnLogin.disabled = true;
    btnLogin.textContent = "Logging in...";
  } else {
    btnLogin.disabled = false;
    btnLogin.textContent = "Login";
  }
}

// 3. Xử lý submit form login
if (loginForm) {
  loginForm.addEventListener("submit", async (e) => {
    e.preventDefault();
    loginError.style.display = "none";
    const email = emailInput.value.trim();
    const password = passwordInput.value;

    if (!email || !password) {
      loginError.textContent = "Vui lòng nhập đầy đủ email và mật khẩu.";
      loginError.style.display = "block";
      return;
    }

    try {
      setLoading(true);

      // Gọi Firebase Auth
      const cred = await signInWithEmailAndPassword(auth, email, password);

      // TODO: Sau này thêm bước check role = 'admin' trong Firestore
      console.log("Login success:", cred.user.uid);
      window.location.href = "admin.html";
    } catch (err) {
      console.error(err);
      loginError.textContent =
        err.code === "auth/invalid-credential"
          ? "Sai email hoặc mật khẩu."
          : "Không thể đăng nhập, thử lại sau.";
      loginError.style.display = "block";
    } finally {
      setLoading(false);
    }
  });
}
