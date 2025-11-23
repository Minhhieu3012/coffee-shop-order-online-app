package vn.edu.ut.hieupm9898.customermobile.features.auth

// --- CÁC DATA CLASS CHO DỮ LIỆU FORM ---
data class RegisterData(
    val username: String = "",
    val email: String = "",
    val phoneNumber: String = "",
    val password: String = "",
    val referralCode: String = ""
)

// --- STATE CHO CÁC MÀN HÌNH ---
data class AuthUiState(
    val email: String = "",
    val password: String = "",
    val phoneNumber: String = "",
    val newPassword: String = "",
    val confirmPassword: String = "",
    val otpCode: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isOtpSent: Boolean = false
)

// --- SỰ KIỆN ĐIỀU HƯỚNG (SharedFlow) ---
sealed class AuthNavEvent {
    data object NavigateToHome : AuthNavEvent()
    data object NavigateToLogin : AuthNavEvent() // 👈 ĐÃ CÓ
    data object NavigateToRegisterSuccess : AuthNavEvent()
    data class NavigateToOtp(val targetRoute: String) : AuthNavEvent()
    data object NavigateToResetSuccess : AuthNavEvent()
}

// --- TARGET CHO OTP ---
object OtpTargets {
    const val COMPLETE_REGISTRATION = "complete_registration"
    const val RESET_PASSWORD = "reset_password"
}