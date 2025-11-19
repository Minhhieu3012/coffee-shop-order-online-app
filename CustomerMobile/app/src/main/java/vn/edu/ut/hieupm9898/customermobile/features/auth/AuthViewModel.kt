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
import vn.edu.ut.hieupm9898.customermobile.data.repository.AuthRepository // <-- IMPORT QUAN TRỌNG
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository // Sử dụng class thật, không phải interface giả
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState = _uiState.asStateFlow()

    private val _navEvent = MutableSharedFlow<AuthNavEvent>()
    val navEvent = _navEvent.asSharedFlow()

    // --- 1. CẬP NHẬT DỮ LIỆU NHẬP VÀO ---
    fun updateField(field: String, value: String) {
        _uiState.update { currentState ->
            when (field) {
                "email" -> currentState.copy(email = value)
                "password" -> currentState.copy(password = value)
                "phoneNumber" -> currentState.copy(phoneNumber = value)
                "newPassword" -> currentState.copy(newPassword = value)
                "confirmPassword" -> currentState.copy(confirmPassword = value)
                "otpCode" -> currentState.copy(otpCode = value)
                else -> currentState
            }
        }
    }

    // --- 2. LOGIC ĐĂNG NHẬP ---
    fun onLoginClicked() = viewModelScope.launch {
        val email = _uiState.value.email
        val password = _uiState.value.password

        if (email.isBlank() || password.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Vui lòng nhập đầy đủ thông tin") }
            return@launch
        }

        _uiState.update { it.copy(isLoading = true, errorMessage = null) }

        // Gọi xuống Repository thật (Firebase)
        val success = authRepository.login(email, password)

        _uiState.update { it.copy(isLoading = false) }

        if (success) {
            _navEvent.emit(AuthNavEvent.NavigateToHome)
        } else {
            _uiState.update { it.copy(errorMessage = "Đăng nhập thất bại. Kiểm tra lại email/mật khẩu.") }
        }
    }

    // --- 3. LOGIC ĐĂNG KÝ (Giả lập) ---
    fun onSignUpClicked() = viewModelScope.launch {
        // Trong thực tế, bạn gọi authRepository.register(...) ở đây
        _navEvent.emit(AuthNavEvent.NavigateToOtp(OtpTargets.COMPLETE_REGISTRATION))
    }

    // --- 4. LOGIC QUÊN MẬT KHẨU ---
    fun onProceedForgotPassword() = viewModelScope.launch {
        _uiState.update { it.copy(isLoading = true) }
        delay(1000) // Giả lập mạng

        // Logic gửi OTP (hoặc gửi email reset)
        val sent = authRepository.sendOtp(_uiState.value.phoneNumber)

        _uiState.update { it.copy(isLoading = false) }

        if (sent) {
            _navEvent.emit(AuthNavEvent.NavigateToOtp(OtpTargets.RESET_PASSWORD))
        }
    }
}