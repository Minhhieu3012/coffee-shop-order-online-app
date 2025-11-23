package vn.edu.ut.hieupm9898.customermobile.data.repository

import android.util.Log
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
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
            Log.d("AuthRepository", "🔄 Đang đăng ký user: $userName, $email, $phoneNumber")

            val authResult = firebaseAuth
                .createUserWithEmailAndPassword(email, password)
                .await()

            val uid = authResult.user?.uid ?: return Result.failure(
                Exception("Không thể lấy UID")
            )

            Log.d("AuthRepository", "✅ Tạo Firebase Auth thành công, UID: $uid")

            val newUser = User(
                uid = uid,
                email = email,
                displayName = userName,
                phoneNumber = phoneNumber,
                authProvider = "email",
                isProfileCompleted = true,
                referralCode = generateReferralCode(),
                referredBy = referralCode,
                isEmailVerified = false
            )

            firestore.collection("users")
                .document(uid)
                .set(newUser)
                .await()

            Log.d("AuthRepository", "✅ Lưu Firestore thành công!")
            Result.success(uid)

        } catch (e: Exception) {
            Log.e("AuthRepository", "❌ Lỗi đăng ký: ${e.message}", e)
            Result.failure(e)
        }
    }

    // ============================================
    // 2. ĐĂNG NHẬP
    // ============================================
    suspend fun login(email: String, password: String): Result<User> {
        return try {
            val authResult = firebaseAuth
                .signInWithEmailAndPassword(email, password)
                .await()

            val uid = authResult.user?.uid ?: return Result.failure(
                Exception("Không thể lấy UID")
            )

            val userDoc = firestore.collection("users")
                .document(uid)
                .get()
                .await()

            val user = if (userDoc.exists()) {
                userDoc.toObject(User::class.java)
                    ?: return Result.failure(Exception("Không thể parse user data"))
            } else {
                val newUser = User(
                    uid = uid,
                    email = email,
                    authProvider = "email",
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

            Result.success(user)

        } catch (e: Exception) {
            val errorMessage = when {
                e.message?.contains("network", ignoreCase = true) == true ->
                    "Không có kết nối internet"
                e.message?.contains("password", ignoreCase = true) == true ->
                    "Email hoặc mật khẩu không đúng"
                else -> "Lỗi đăng nhập: ${e.message}"
            }
            Result.failure(Exception(errorMessage))
        }
    }

    // ============================================
    // 3. ĐĂNG NHẬP GOOGLE
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

            val userDoc = firestore.collection("users").document(uid).get().await()

            val user = if (userDoc.exists()) {
                userDoc.toObject(User::class.java)!!
            } else {
                val newUser = User(
                    uid = uid,
                    email = email,
                    displayName = displayName,
                    avatarUrl = photoUrl,
                    authProvider = "google",
                    isEmailVerified = true,
                    isProfileCompleted = displayName.isNotEmpty(),
                    referralCode = generateReferralCode()
                )

                firestore.collection("users").document(uid).set(newUser).await()
                newUser
            }

            Result.success(user)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // ============================================
    // 4. CẬP NHẬT PROFILE
    // ============================================
    suspend fun updateProfile(
        uid: String,
        displayName: String,
        phoneNumber: String,
        dateOfBirth: String = "",
        avatarUrl: String = ""
    ): Result<Unit> {
        return try {
            val updates = hashMapOf<String, Any>(
                "displayName" to displayName,
                "phoneNumber" to phoneNumber,
                "isProfileCompleted" to true,
                "updatedAt" to FieldValue.serverTimestamp()
            )

            if (dateOfBirth.isNotEmpty()) {
                updates["dateOfBirth"] = dateOfBirth
            }

            if (avatarUrl.isNotEmpty()) {
                updates["avatarUrl"] = avatarUrl
            }

            firestore.collection("users")
                .document(uid)
                .update(updates)
                .await()

            Result.success(Unit)

        } catch (e: Exception) {
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
            Result.failure(e)
        }
    }

    // ============================================
    // 6. ĐỔI MẬT KHẨU
    // ============================================
    suspend fun changePassword(
        oldPassword: String,
        newPassword: String
    ): Result<Unit> {
        return try {
            val user = firebaseAuth.currentUser
                ?: return Result.failure(Exception("Chưa đăng nhập"))

            val email = user.email
                ?: return Result.failure(Exception("Không tìm thấy email"))

            // Re-authenticate trước khi đổi mật khẩu
            val credential = EmailAuthProvider.getCredential(email, oldPassword)
            user.reauthenticate(credential).await()

            // Đổi mật khẩu
            user.updatePassword(newPassword).await()

            Result.success(Unit)

        } catch (e: Exception) {
            val errorMessage = when {
                e.message?.contains("password", ignoreCase = true) == true ->
                    "Mật khẩu cũ không đúng"
                e.message?.contains("weak-password", ignoreCase = true) == true ->
                    "Mật khẩu mới quá yếu"
                else -> "Lỗi: ${e.message}"
            }
            Result.failure(Exception(errorMessage))
        }
    }

    // ============================================
    // 7. XÓA TÀI KHOẢN
    // ============================================
    suspend fun deleteAccount(): Result<Unit> {
        return try {
            val user = firebaseAuth.currentUser
                ?: return Result.failure(Exception("Chưa đăng nhập"))

            val uid = user.uid

            // Xóa document trên Firestore
            firestore.collection("users")
                .document(uid)
                .delete()
                .await()

            // Xóa tài khoản Firebase Auth
            user.delete().await()

            Result.success(Unit)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // ============================================
    // 8. ĐĂNG XUẤT
    // ============================================
    fun logout() {
        firebaseAuth.signOut()
    }

    // ============================================
    // HELPER
    // ============================================
    private fun generateReferralCode(): String {
        val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
        return (1..6).map { chars.random() }.joinToString("")
    }

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