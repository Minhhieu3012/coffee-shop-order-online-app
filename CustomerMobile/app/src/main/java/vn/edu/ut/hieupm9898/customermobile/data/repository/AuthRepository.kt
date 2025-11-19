package vn.edu.ut.hieupm9898.customermobile.data.repository

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import vn.edu.ut.hieupm9898.customermobile.features.auth.RegisterData

class AuthRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) {
    // 1. Đăng nhập
    suspend fun login(email: String, pass: String): Boolean {
        return try {
            firebaseAuth.signInWithEmailAndPassword(email, pass).await()
            true // Thành công
        } catch (e: Exception) {
            e.printStackTrace()
            false // Thất bại
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

    // 3. Gửi OTP (Tạm thời trả về true để test UI, sau này tích hợp Firebase Phone Auth)
    suspend fun sendOtp(phone: String): Boolean {
        return true
    }

    // 4. Xác thực OTP (Tạm thời test)
    suspend fun verifyOtp(code: String): Boolean {
        return code == "123456" // Code giả để test
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