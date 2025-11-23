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

    /**
     * 🔥 KIỂM TRA XEM USER ĐÃ ĐĂNG NHẬP VÀ CHỌN "GHI NHỚ" CHƯA
     */
    fun isUserLoggedIn(): Boolean {
        return authRepository.isUserLoggedIn()
    }

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
                    // 🔥 LƯU TRẠNG THÁI "GHI NHỚ"
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

    fun onGoogleSignInClicked(idToken: String) = viewModelScope.launch {
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }

        val result = authRepository.signInWithGoogle(idToken)

        _uiState.update { it.copy(isLoading = false) }

        result.fold(
            onSuccess = { user ->
                // 🔥 TỰ ĐỘNG LƯU TRẠNG THÁI "GHI NHỚ" KHI ĐĂNG NHẬP GOOGLE
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
        authRepository.logout() // Xóa SharedPreferences và Firebase signOut
        _currentUser.value = null
        _uiState.value = AuthUiState()
    }

    fun onProceedForgotPassword() = viewModelScope.launch {
        _uiState.update { it.copy(isLoading = true) }
        kotlinx.coroutines.delay(1000)

        val sent = authRepository.sendOtp(_uiState.value.phoneNumber)

        _uiState.update { it.copy(isLoading = false) }

        if (sent) {
            _navEvent.emit(AuthNavEvent.NavigateToOtp(OtpTargets.RESET_PASSWORD))
        }
    }
}