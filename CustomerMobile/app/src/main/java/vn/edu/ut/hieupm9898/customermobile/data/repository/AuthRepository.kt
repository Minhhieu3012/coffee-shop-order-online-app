package vn.edu.ut.hieupm9898.customermobile.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import vn.edu.ut.hieupm9898.customermobile.features.auth.RegisterData

class AuthRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) {
    // 1. Đăng nhập email/password
    suspend fun login(email: String, pass: String): Boolean {
        return try {
            firebaseAuth.signInWithEmailAndPassword(email, pass).await()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    // 2. Đăng ký
    suspend fun register(data: RegisterData): Boolean {
        return try {
            firebaseAuth.createUserWithEmailAndPassword(data.email, data.password).await()
            true
        } catch (e: Exception) {
            false
        }
    }

    // **MỚI: Đăng nhập với Google**
    suspend fun signInWithGoogle(idToken: String): Boolean {
        return try {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            firebaseAuth.signInWithCredential(credential).await()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    // 3. Gửi OTP
    suspend fun sendOtp(phone: String): Boolean {
        return true
    }

    // 4. Xác thực OTP
    suspend fun verifyOtp(code: String): Boolean {
        return code == "123456"
    }

    // 5. Reset mật khẩu
    suspend fun resetPassword(email: String): Boolean {
        return try {
            firebaseAuth.sendPasswordResetEmail(email).await()
            true
        } catch (e: Exception) {
            false
        }
    }
}