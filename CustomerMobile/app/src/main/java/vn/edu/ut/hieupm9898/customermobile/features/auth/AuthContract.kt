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
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null, // ✅ THÊM MỚI: Thông báo thành công
    val isEmailSent: Boolean = false    // ✅ THÊM MỚI: Trạng thái đã gửi email
)

// --- SỰ KIỆN ĐIỀU HƯỚNG (SharedFlow) ---
sealed class AuthNavEvent {
    data object NavigateToHome : AuthNavEvent()
    data object NavigateToLogin : AuthNavEvent()
    data object NavigateToRegisterSuccess : AuthNavEvent()
    data object NavigateToEmailSent : AuthNavEvent() // ✅ THÊM MỚI: Thông báo email đã gửi
}