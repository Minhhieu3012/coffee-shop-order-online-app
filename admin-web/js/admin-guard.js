// admin-web/js/admin-guard.js
import { auth, db } from "./firebase-config.js";
import { onAuthStateChanged } from "https://www.gstatic.com/firebasejs/10.8.0/firebase-auth.js";
import { doc, getDoc } from "https://www.gstatic.com/firebasejs/10.8.0/firebase-firestore.js";

/**
 * 🔒 BẢO VỆ TRANG ADMIN
 * Kiểm tra user đã đăng nhập và có role = "admin" không
 * Nếu không -> Chuyển về trang login
 */
async function checkAdminAccess() {
  return new Promise((resolve, reject) => {
    onAuthStateChanged(auth, async (user) => {
      if (!user) {
        // ❌ Chưa đăng nhập
        console.log("❌ Chưa đăng nhập, chuyển về login...");
        window.location.href = "login.html";
        reject("Not authenticated");
        return;
      }

      try {
        // ✅ Đã đăng nhập -> Kiểm tra role
        const userDocRef = doc(db, "users", user.uid);
        const userDoc = await getDoc(userDocRef);

        if (!userDoc.exists()) {
          console.log("❌ Không tìm thấy user data");
          await auth.signOut();
          window.location.href = "login.html";
          reject("User data not found");
          return;
        }

        const userData = userDoc.data();
        console.log("📦 User role:", userData.role);

        if (userData.role !== "admin") {
          // ❌ Không phải admin
          console.log("❌ Không có quyền admin, đăng xuất...");
          alert("Bạn không có quyền truy cập trang quản trị!");
          await auth.signOut();
          window.location.href = "login.html";
          reject("Access denied");
          return;
        }

        // ✅ Là admin -> Cho phép truy cập
        console.log("✅ Admin access granted");
        resolve(userData);

      } catch (error) {
        console.error("❌ Lỗi kiểm tra quyền:", error);
        await auth.signOut();
        window.location.href = "login.html";
        reject(error);
      }
    });
  });
}

// Export để dùng trong các file khác
export { checkAdminAccess };