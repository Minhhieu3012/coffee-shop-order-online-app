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

// =============================================
// 1. AUTH STATE - Kiểm tra đăng nhập
// =============================================
// Nếu đã đăng nhập rồi thì khỏi vào login nữa -> đá qua admin.html
onAuthStateChanged(auth, (user) => {
  if (user && window.location.pathname.includes("login.html")) {
    // TODO: Sau này thêm check role admin từ Firestore
    // const userDoc = await getDoc(doc(db, 'users', user.uid));
    // if (userDoc.data().role === 'admin') {
    window.location.href = "admin.html";
    // }
  }
});

// =============================================
// 2. TOGGLE PASSWORD VISIBILITY
// =============================================
if (togglePassword) {
  togglePassword.addEventListener("click", () => {
    const isHidden = passwordInput.type === "password";
    passwordInput.type = isHidden ? "text" : "password";
    togglePassword.innerHTML = isHidden
      ? '<i class="fa-regular fa-eye-slash"></i>'
      : '<i class="fa-regular fa-eye"></i>';
  });
}

// =============================================
// 3. LOADING STATE
// =============================================
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

// =============================================
// 4. SUBMIT LOGIN FORM
// =============================================
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

      // Gọi Firebase Auth để đăng nhập
      const cred = await signInWithEmailAndPassword(auth, email, password);

      // TODO: Sau này thêm bước check role = 'admin' trong Firestore
      // import { getFirestore, doc, getDoc } from "firebase/firestore";
      // const db = getFirestore();
      // const userDoc = await getDoc(doc(db, "users", cred.user.uid));
      // const userData = userDoc.data();
      // 
      // if (userData.role !== "admin") {
      //   await signOut(auth);
      //   throw new Error("Bạn không có quyền truy cập Admin!");
      // }

      console.log("Login success:", cred.user.uid);
      window.location.href = "admin.html";
      
    } catch (err) {
      console.error("Login error:", err);
      
      // Xử lý lỗi
      if (err.code === "auth/invalid-credential") {
        loginError.textContent = "Sai email hoặc mật khẩu.";
      } else if (err.code === "auth/too-many-requests") {
        loginError.textContent = "Quá nhiều lần đăng nhập sai. Vui lòng thử lại sau.";
      } else if (err.message.includes("quyền truy cập")) {
        loginError.textContent = err.message;
      } else {
        loginError.textContent = "Không thể đăng nhập, thử lại sau.";
      }
      
      loginError.style.display = "block";
    } finally {
      setLoading(false);
    }
  });
}