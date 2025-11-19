// admin-web/js/firebase-config.js
import { initializeApp } from "https://www.gstatic.com/firebasejs/10.12.0/firebase-app.js";
import { getAuth, connectAuthEmulator } from "https://www.gstatic.com/firebasejs/10.12.0/firebase-auth.js";
import { getFirestore, connectFirestoreEmulator } from "https://www.gstatic.com/firebasejs/10.12.0/firebase-firestore.js";
import { getStorage, connectStorageEmulator } from "https://www.gstatic.com/firebasejs/10.12.0/firebase-storage.js";

// ⚠️ QUAN TRỌNG: Bạn cần thay thế các dòng "..." dưới đây bằng thông tin thật
// Cách lấy: Vào Firebase Console -> Project Settings -> Kéo xuống dưới cùng chọn Web App
const firebaseConfig = {
  apiKey: "THAY_API_KEY ",
  authDomain: "broscoffeeshop-94f07.firebaseapp.com",
  projectId: "broscoffeeshop-94f07", // Tôi lấy ID này từ file .firebaserc của bạn
  storageBucket: "broscoffeeshop-94f07.appspot.com",
  messagingSenderId: "THAY_SENDER_ID ",
  appId: "THAY_APP_ID "
};

// 1. Khởi tạo App
const app = initializeApp(firebaseConfig);

// 2. Khởi tạo các dịch vụ
const auth = getAuth(app);
const db = getFirestore(app);
const storage = getStorage(app);

// 3. KẾT NỐI VỚI EMULATOR  
// Theo hướng dẫn trong file WorkWithDB.pdf trang 4
if (window.location.hostname === "localhost" || window.location.hostname === "127.0.0.1") {
  console.log("Đang chạy trên Localhost -> Kết nối tới Emulator...");
  
  // Cổng 9099 cho Authentication (Xem file firebase.json)
  connectAuthEmulator(auth, "http://127.0.0.1:9099");
  
  // Cổng 8080 cho Firestore
  connectFirestoreEmulator(db, "127.0.0.1", 8080);
  
  // Cổng 9199 cho Storage
  connectStorageEmulator(storage, "127.0.0.1", 9199);
}

export { auth, db, storage };