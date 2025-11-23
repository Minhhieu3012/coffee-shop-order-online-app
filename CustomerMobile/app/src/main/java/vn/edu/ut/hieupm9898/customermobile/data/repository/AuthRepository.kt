package vn.edu.ut.hieupm9898.customermobile.data.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FieldValue
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import vn.edu.ut.hieupm9898.customermobile.data.model.User

class AuthRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) {

    // ============================================
    // 1. ĐĂNG KÝ - TẠO TÀI KHOẢN + LƯU FIRESTORE
    // ============================================
    suspend fun register(
        userName: String,
        email: String,
        phoneNumber: String,
        password: String,
        referralCode: String = ""
    ): Result<String> {
        return try {
            Log.d("AuthRepository", "📄 Đang đăng ký user: $userName, $email, $phoneNumber")

            // Bước 1: Tạo tài khoản trên Firebase Auth
            val authResult = firebaseAuth
                .createUserWithEmailAndPassword(email, password)
                .await()

            val uid = authResult.user?.uid ?: return Result.failure(
                Exception("Không thể lấy UID")
            )

            Log.d("AuthRepository", "✅ Tạo Firebase Auth thành công, UID: $uid")

            // Bước 2: Tạo User object với role = "user" (MẶC ĐỊNH)
            val newUser = User(
                uid = uid,
                email = email,
                displayName = userName,
                phoneNumber = phoneNumber,
                authProvider = "email",
                role = "user", // 👈 MẶC ĐỊNH LÀ USER
                isProfileCompleted = true,
                referralCode = generateReferralCode(),
                referredBy = referralCode,
                isEmailVerified = false
            )

            Log.d("AuthRepository", "📦 User object: displayName=${newUser.displayName}, phone=${newUser.phoneNumber}, role=${newUser.role}")

            // Bước 3: Lưu lên Firestore collection "users"
            firestore.collection("users")
                .document(uid)
                .set(newUser)
                .await()

            Log.d("AuthRepository", "✅ Lưu Firestore thành công!")

            Result.success(uid)

        } catch (e: Exception) {
            Log.e("AuthRepository", "❌ Lỗi đăng ký: ${e.message}", e)
            e.printStackTrace()
            Result.failure(e)
        }
    }

    // ============================================
    // 2. ĐĂNG NHẬP - XÁC THỰC + LẤY THÔNG TIN USER
    // ============================================
    suspend fun login(email: String, password: String): Result<User> {
        return try {
            // Bước 1: Xác thực với Firebase Auth
            val authResult = firebaseAuth
                .signInWithEmailAndPassword(email, password)
                .await()

            val uid = authResult.user?.uid ?: return Result.failure(
                Exception("Không thể lấy UID")
            )

            // Bước 2: Lấy thông tin User từ Firestore
            val userDoc = firestore.collection("users")
                .document(uid)
                .get()
                .await()

            val user = if (userDoc.exists()) {
                userDoc.toObject(User::class.java)
                    ?: return Result.failure(Exception("Không thể parse user data"))
            } else {
                // User chưa có trong Firestore
                val newUser = User(
                    uid = uid,
                    email = email,
                    authProvider = "email",
                    role = "user", // 👈 MẶC ĐỊNH LÀ USER
                    isProfileCompleted = false,
                    referralCode = generateReferralCode(),
                    isEmailVerified = authResult.user?.isEmailVerified ?: false
                )

                firestore.collection("users")
                    .document(uid)
                    .set(newUser)
                    .await()

                newUser
            }

            // ✅ CHO PHÉP CẢ USER VÀ ADMIN ĐĂNG NHẬP VÀO APP
            Log.d("AuthRepository", "✅ Login thành công: email=${user.email}, role=${user.role}")
            Result.success(user)

        } catch (e: Exception) {
            e.printStackTrace()

            val errorMessage = when {
                e.message?.contains("network", ignoreCase = true) == true ->
                    "Không có kết nối internet. Vui lòng kiểm tra lại."
                e.message?.contains("password", ignoreCase = true) == true ->
                    "Email hoặc mật khẩu không đúng"
                e.message?.contains("user-not-found", ignoreCase = true) == true ->
                    "Tài khoản không tồn tại"
                e.message?.contains("user-disabled", ignoreCase = true) == true ->
                    "Tài khoản đã bị khóa"
                else ->
                    e.message ?: "Lỗi đăng nhập"
            }

            Result.failure(Exception(errorMessage))
        }
    }

    // ============================================
    // 3. ĐĂNG NHẬP GOOGLE - TẠO/CẬP NHẬT USER
    // ============================================
    suspend fun signInWithGoogle(idToken: String): Result<User> {
        return try {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            val authResult = firebaseAuth.signInWithCredential(credential).await()

            val uid = authResult.user?.uid ?: return Result.failure(
                Exception("Không thể lấy UID")
            )
            val email = authResult.user?.email ?: ""
            val displayName = authResult.user?.displayName ?: ""
            val photoUrl = authResult.user?.photoUrl?.toString() ?: ""

            // Kiểm tra user đã tồn tại chưa
            val userDoc = firestore.collection("users").document(uid).get().await()

            val user = if (userDoc.exists()) {
                userDoc.toObject(User::class.java)!!
            } else {
                // User mới -> Tạo mới với role = "user"
                val newUser = User(
                    uid = uid,
                    email = email,
                    displayName = displayName,
                    avatarUrl = photoUrl,
                    authProvider = "google",
                    role = "user", // 👈 MẶC ĐỊNH LÀ USER
                    isEmailVerified = true,
                    isProfileCompleted = displayName.isNotEmpty(),
                    referralCode = generateReferralCode()
                )

                firestore.collection("users").document(uid).set(newUser).await()
                newUser
            }

            // ✅ CHO PHÉP CẢ USER VÀ ADMIN
            Result.success(user)

        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    // ============================================
    // 4. CẬP NHẬT PROFILE - SAU KHI TẠO PROFILE
    // ============================================
    suspend fun updateProfile(
        uid: String,
        displayName: String,
        phoneNumber: String,
        dateOfBirth: String,
        avatarUrl: String = ""
    ): Result<Unit> {
        return try {
            val updates = hashMapOf<String, Any>(
                "displayName" to displayName,
                "phoneNumber" to phoneNumber,
                "dateOfBirth" to dateOfBirth,
                "isProfileCompleted" to true,
                "updatedAt" to FieldValue.serverTimestamp()
            )

            if (avatarUrl.isNotEmpty()) {
                updates["avatarUrl"] = avatarUrl
            }

            firestore.collection("users")
                .document(uid)
                .update(updates)
                .await()

            Result.success(Unit)

        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    // ============================================
    // 5. LẤY THÔNG TIN USER HIỆN TẠI
    // ============================================
    suspend fun getCurrentUser(): Result<User?> {
        return try {
            val uid = firebaseAuth.currentUser?.uid
                ?: return Result.success(null)

            val userDoc = firestore.collection("users")
                .document(uid)
                .get()
                .await()

            val user = userDoc.toObject(User::class.java)
            Result.success(user)

        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    // ============================================
    // 6. ĐĂNG XUẤT
    // ============================================
    fun logout() {
        firebaseAuth.signOut()
    }

    // ============================================
    // HELPER: TẠO MÃ GIỚI THIỆU NGẪU NHIÊN
    // ============================================
    private fun generateReferralCode(): String {
        val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
        return (1..6).map { chars.random() }.joinToString("")
    }

    // ============================================
    // CÁC HÀM CŨ
    // ============================================
    suspend fun sendOtp(phone: String): Boolean {
        return true
    }

    suspend fun verifyOtp(code: String): Boolean {
        return code == "123456"
    }

    suspend fun resetPassword(email: String): Boolean {
        return try {
            firebaseAuth.sendPasswordResetEmail(email).await()
            true
        } catch (e: Exception) {
            false
        }
    }
}