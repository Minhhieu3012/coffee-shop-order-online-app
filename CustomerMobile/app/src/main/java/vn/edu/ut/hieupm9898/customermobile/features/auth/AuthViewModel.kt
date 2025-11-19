package vn.edu.ut.hieupm9898.customermobile.features.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject



// Giả định: Cần tạo file Repository này trong package data/repository/
interface AuthRepository {
    suspend fun login(email: String, password: String): Boolean
    suspend fun register(data: RegisterData): Boolean
    suspend fun sendOtp(phoneNumber: String): Boolean
    suspend fun verifyOtp(code: String): Boolean
    suspend fun resetPassword(password: String): Boolean
}

// --- IMPLEMENTATION CỦA VIEW MODEL ---

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository // Cần Mock hoặc Implement Repository thực tế
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState = _uiState.asStateFlow()

    private val _navEvent = MutableSharedFlow<AuthNavEvent>()
    val navEvent = _navEvent.asSharedFlow()

    // ====================================================================
    // 1. CẬP NHẬT TRƯỜNG DỮ LIỆU
    // ====================================================================

    fun updateField(field: String, value: String) {
        _uiState.update { currentState ->
            currentState.copy(
                email = if (field == "email") value else currentState.email,
                password = if (field == "password") value else currentState.password,
                phoneNumber = if (field == "phoneNumber") value else currentState.phoneNumber,
                newPassword = if (field == "newPassword") value else currentState.newPassword,
                confirmPassword = if (field == "confirmPassword") value else currentState.confirmPassword,
                otpCode = if (field == "otpCode") value else currentState.otpCode,
            )
        }
    }

    // ====================================================================
    // 2. LOGIC ĐĂNG NHẬP VÀ ĐĂNG KÝ
    // ====================================================================

    fun onLoginClicked() = viewModelScope.launch {
        if (_uiState.value.email.isBlank() || _uiState.value.password.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Vui lòng nhập email và mật khẩu.") }
            return@launch
        }

        _uiState.update { it.copy(isLoading = true, errorMessage = null) }

        delay(1500)
        val success = authRepository.login(_uiState.value.email, _uiState.value.password)

        _uiState.update { it.copy(isLoading = false) }

        if (success) {
            _navEvent.emit(AuthNavEvent.NavigateToHome)
        } else {
            _uiState.update { it.copy(errorMessage = "Tên đăng nhập hoặc mật khẩu không đúng.") }
        }
    }

    fun onSignUpClicked() = viewModelScope.launch {
        // Logic kiểm tra form đăng ký
        _navEvent.emit(AuthNavEvent.NavigateToOtp(OtpTargets.COMPLETE_REGISTRATION))
    }

    // ====================================================================
    // 3. LOGIC QUÊN MẬT KHẨU VÀ OTP
    // ====================================================================

    fun onProceedForgotPassword() = viewModelScope.launch {
        if (_uiState.value.phoneNumber.length < 10) {
            _uiState.update { it.copy(errorMessage = "Số điện thoại không hợp lệ.") }
            return@launch
        }

        _uiState.update { it.copy(isLoading = true, errorMessage = null) }

        val sent = authRepository.sendOtp(_uiState.value.phoneNumber)

        _uiState.update { it.copy(isLoading = false) }

        if (sent) {
            _uiState.update { it.copy(isOtpSent = true) }
            _navEvent.emit(AuthNavEvent.NavigateToOtp(OtpTargets.RESET_PASSWORD))
        } else {
            _uiState.update { it.copy(errorMessage = "Lỗi gửi mã OTP.") }
        }
    }

    fun onVerifyOtpClicked(target: String) = viewModelScope.launch {
        if (_uiState.value.otpCode.length != 6) {
            _uiState.update { it.copy(errorMessage = "Mã OTP phải có 6 chữ số.") }
            return@launch
        }

        _uiState.update { it.copy(isLoading = true) }
        delay(1000)
        val verified = authRepository.verifyOtp(_uiState.value.otpCode)
        _uiState.update { it.copy(isLoading = false) }

        if (verified) {
            when (target) {
                OtpTargets.RESET_PASSWORD -> _navEvent.emit(AuthNavEvent.NavigateToLogin) // Thay bằng NavigateToResetPasswordScreen
                OtpTargets.COMPLETE_REGISTRATION -> _navEvent.emit(AuthNavEvent.NavigateToRegisterSuccess)
            }
        } else {
            _uiState.update { it.copy(errorMessage = "Mã OTP không chính xác.") }
        }
    }

    fun onResetPasswordClicked() = viewModelScope.launch {
        if (_uiState.value.newPassword != _uiState.value.confirmPassword) {
            _uiState.update { it.copy(errorMessage = "Mật khẩu xác nhận không khớp.") }
            return@launch
        }

        _uiState.update { it.copy(isLoading = true) }
        delay(1500)
        val success = authRepository.resetPassword(_uiState.value.newPassword)
        _uiState.update { it.copy(isLoading = false) }

        if (success) {
            _navEvent.emit(AuthNavEvent.NavigateToResetSuccess)
        } else {
            _uiState.update { it.copy(errorMessage = "Không thể đặt lại mật khẩu.") }
        }
    }
}