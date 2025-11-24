package vn.edu.ut.hieupm9898.customermobile.features.auth

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import vn.edu.ut.hieupm9898.customermobile.data.model.User
import vn.edu.ut.hieupm9898.customermobile.data.repository.AuthRepository
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState = _uiState.asStateFlow()

    private val _navEvent = MutableSharedFlow<AuthNavEvent>()
    val navEvent = _navEvent.asSharedFlow()

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser = _currentUser.asStateFlow()

    /**
     * Cập nhật các trường trong form
     */
    fun updateField(field: String, value: String) {
        _uiState.update { currentState ->
            when (field) {
                "email" -> currentState.copy(email = value)
                "password" -> currentState.copy(password = value)
                "phoneNumber" -> currentState.copy(phoneNumber = value)
                "newPassword" -> currentState.copy(newPassword = value)
                "confirmPassword" -> currentState.copy(confirmPassword = value)
                else -> currentState
            }
        }
    }

    /**
     * Xóa thông báo lỗi
     */
    fun clearError() {
        _uiState.update { it.copy(errorMessage = null, successMessage = null) }
    }

    /**
     * 🔥 KIỂM TRA XEM USER ĐÃ ĐĂNG NHẬP VÀ CHỌN "GHI NHỚ" CHƯA
     */
    fun isUserLoggedIn(): Boolean {
        return authRepository.isUserLoggedIn()
    }

    /**
     * ĐĂNG NHẬP
     */
    fun onLoginClicked(isRememberMe: Boolean) = viewModelScope.launch {
        val email = _uiState.value.email.trim()
        val password = _uiState.value.password

        if (email.isBlank() || password.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Vui lòng nhập đầy đủ thông tin") }
            return@launch
        }

        _uiState.update { it.copy(isLoading = true, errorMessage = null) }

        try {
            val result = authRepository.login(email, password)

            _uiState.update { it.copy(isLoading = false) }

            result.fold(
                onSuccess = { user ->
                    authRepository.saveRememberMeState(isRememberMe)
                    _currentUser.value = user
                    _navEvent.emit(AuthNavEvent.NavigateToHome)
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(
                            errorMessage = when {
                                error.message?.contains("network") == true ->
                                    "Không có kết nối internet"
                                error.message?.contains("password") == true ->
                                    "Email hoặc mật khẩu không đúng"
                                error.message?.contains("user-not-found") == true ->
                                    "Tài khoản không tồn tại"
                                else ->
                                    "Đăng nhập thất bại: ${error.message}"
                            }
                        )
                    }
                }
            )
        } catch (e: Exception) {
            _uiState.update {
                it.copy(
                    isLoading = false,
                    errorMessage = "Lỗi hệ thống: ${e.message}"
                )
            }
        }
    }

    /**
     * ĐĂNG NHẬP GOOGLE
     */
    fun onGoogleSignInClicked(idToken: String) = viewModelScope.launch {
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }

        val result = authRepository.signInWithGoogle(idToken)

        _uiState.update { it.copy(isLoading = false) }

        result.fold(
            onSuccess = { user ->
                authRepository.saveRememberMeState(true)
                _currentUser.value = user
                _navEvent.emit(AuthNavEvent.NavigateToHome)
            },
            onFailure = { error ->
                _uiState.update {
                    it.copy(errorMessage = "Đăng nhập Google thất bại: ${error.message}")
                }
            }
        )
    }

    /**
     * ĐĂNG KÝ
     */
    fun onRegisterClicked(
        userName: String,
        email: String,
        phoneNumber: String,
        password: String,
        referralCode: String = ""
    ) = viewModelScope.launch {
        Log.d("AuthViewModel", "📝 Đăng ký với: userName=$userName, email=$email, phone=$phoneNumber")

        _uiState.update { it.copy(isLoading = true, errorMessage = null) }

        val result = authRepository.register(
            userName = userName,
            email = email,
            phoneNumber = phoneNumber,
            password = password,
            referralCode = referralCode
        )

        _uiState.update { it.copy(isLoading = false) }

        result.fold(
            onSuccess = { uid ->
                Log.d("AuthViewModel", "✅ Đăng ký thành công với UID: $uid")
                _navEvent.emit(AuthNavEvent.NavigateToLogin)
            },
            onFailure = { error ->
                Log.e("AuthViewModel", "❌ Đăng ký thất bại: ${error.message}")
                _uiState.update {
                    it.copy(errorMessage = "Đăng ký thất bại: ${error.message}")
                }
            }
        )
    }

    /**
     * TẢI THÔNG TIN USER HIỆN TẠI
     */
    fun loadCurrentUser() = viewModelScope.launch {
        val result = authRepository.getCurrentUser()

        result.fold(
            onSuccess = { user ->
                Log.d("AuthViewModel", "✅ Loaded user: ${user?.displayName}")
                _currentUser.value = user
            },
            onFailure = { error ->
                Log.e("AuthViewModel", "❌ Failed to load user: ${error.message}")
                _currentUser.value = null
            }
        )
    }

    /**
     * 🔥 ĐĂNG XUẤT - XÓA TRẠNG THÁI GHI NHỚ VÀ FIREBASE AUTH
     */
    fun logout() {
        authRepository.logout()
        _currentUser.value = null
        _uiState.value = AuthUiState()
    }

    // ============================================
    // ✅ CHỨC NĂNG RESET PASSWORD QUA EMAIL
    // ============================================

    /**
     * ✅ GỬI EMAIL RESET PASSWORD
     */
    fun sendResetPasswordEmail(email: String) = viewModelScope.launch {
        if (email.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Vui lòng nhập email") }
            return@launch
        }

        // Validate email format
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _uiState.update { it.copy(errorMessage = "Email không hợp lệ") }
            return@launch
        }

        _uiState.update { it.copy(isLoading = true, errorMessage = null, successMessage = null) }

        try {
            val success = authRepository.resetPassword(email)

            _uiState.update { it.copy(isLoading = false) }

            if (success) {
                Log.d("AuthViewModel", "✅ Email reset password đã được gửi đến: $email")
                _uiState.update {
                    it.copy(
                        isEmailSent = true,
                        successMessage = "Email đặt lại mật khẩu đã được gửi!\nVui lòng kiểm tra hộp thư của bạn."
                    )
                }
            } else {
                Log.e("AuthViewModel", "❌ Không thể gửi email reset password")
                _uiState.update {
                    it.copy(errorMessage = "Không thể gửi email. Vui lòng thử lại sau.")
                }
            }
        } catch (e: Exception) {
            Log.e("AuthViewModel", "❌ Lỗi gửi email: ${e.message}")
            _uiState.update {
                it.copy(
                    isLoading = false,
                    errorMessage = when {
                        e.message?.contains("network") == true -> "Không có kết nối internet"
                        e.message?.contains("user-not-found") == true -> "Email không tồn tại trong hệ thống"
                        else -> "Lỗi: ${e.message}"
                    }
                )
            }
        }
    }

    /**
     * ✅ RESET STATE SAU KHI GỬI EMAIL THÀNH CÔNG
     */
    fun resetEmailSentState() {
        _uiState.update { it.copy(isEmailSent = false, successMessage = null) }
    }
}