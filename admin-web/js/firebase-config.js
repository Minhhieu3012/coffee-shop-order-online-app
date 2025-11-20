// admin-web/js/firebase-config.js
import { initializeApp } from "https://www.gstatic.com/firebasejs/10.8.0/firebase-app.js";
import { getFirestore, connectFirestoreEmulator } from "https://www.gstatic.com/firebasejs/10.8.0/firebase-firestore.js";
import { getAuth, connectAuthEmulator } from "https://www.gstatic.com/firebasejs/10.8.0/firebase-auth.js";
import { getStorage, connectStorageEmulator } from "https://www.gstatic.com/firebasejs/10.8.0/firebase-storage.js";

// --- CẤU HÌNH THẬT (Lấy từ ảnh bạn gửi) ---
const firebaseConfig = {
  apiKey: "AIzaSyARH0EF4ryzpJajFU9jSXDOstNETOub4Cg",
  authDomain: "broscoffeeshop-94f07.firebaseapp.com",
  projectId: "broscoffeeshop-94f07",
  storageBucket: "broscoffeeshop-94f07.firebasestorage.app",
  messagingSenderId: "743926532574",
  appId: "1:743926532574:web:bc45683da83c161eba354f",
  measurementId: "G-R89NKV9CZC"
};

// 1. Khởi tạo App
const app = initializeApp(firebaseConfig);

// 2. Lấy các công cụ
const db = getFirestore(app);
const auth = getAuth(app);
const storage = getStorage(app);

// ==============================================================
// QUAN TRỌNG: CÔNG TẮC CHUYỂN ĐỔI (REAL vs EMULATOR)
// ==============================================================

// Hiện tại mình đang COMMENT (Vô hiệu hóa) đoạn dưới để chạy SERVER THẬT.
// Nếu bạn muốn quay lại test trên máy mình (Emulator) thì bỏ dấu // ở 3 dòng connect... đi nhé.

if (window.location.hostname === "localhost" || window.location.hostname === "127.0.0.1") {
  // console.log("🔥 Đang dùng EMULATOR (Database Giả lập)");
  // connectAuthEmulator(auth, "http://127.0.0.1:9099");
  // connectFirestoreEmulator(db, '127.0.0.1', 8080);
  // connectStorageEmulator(storage, "127.0.0.1", 9199);
}

export { db, auth, storage };