package vn.edu.ut.hieupm9898.customermobile.features.auth

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

    // ĐĂNG NHẬP
    fun onLoginClicked() = viewModelScope.launch {
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
                    _currentUser.value = user

                    if (user.isProfileCompleted) {
                        _navEvent.emit(AuthNavEvent.NavigateToHome)
                    } else {
                        _navEvent.emit(AuthNavEvent.NavigateToCreateProfile)
                    }
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

    // ĐĂNG NHẬP GOOGLE
    fun onGoogleSignInClicked(idToken: String) = viewModelScope.launch {
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }

        val result = authRepository.signInWithGoogle(idToken)

        _uiState.update { it.copy(isLoading = false) }

        result.fold(
            onSuccess = { user ->
                _currentUser.value = user

                if (user.isProfileCompleted) {
                    _navEvent.emit(AuthNavEvent.NavigateToHome)
                } else {
                    _navEvent.emit(AuthNavEvent.NavigateToCreateProfile)
                }
            },
            onFailure = { error ->
                _uiState.update {
                    it.copy(errorMessage = "Đăng nhập Google thất bại: ${error.message}")
                }
            }
        )
    }

    // ĐĂNG KÝ
    fun onRegisterClicked(
        email: String,
        password: String,
        referralCode: String = ""
    ) = viewModelScope.launch {
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }

        val result = authRepository.register(email, password, referralCode)

        _uiState.update { it.copy(isLoading = false) }

        result.fold(
            onSuccess = {
                _navEvent.emit(AuthNavEvent.NavigateToLogin)
            },
            onFailure = { error ->
                _uiState.update {
                    it.copy(errorMessage = "Đăng ký thất bại: ${error.message}")
                }
            }
        )
    }

    // CẬP NHẬT PROFILE
    fun onCompleteProfile(
        displayName: String,
        phoneNumber: String,
        dateOfBirth: String,
        avatarUrl: String = ""
    ) = viewModelScope.launch {
        val uid = _currentUser.value?.uid ?: return@launch

        _uiState.update { it.copy(isLoading = true, errorMessage = null) }

        val result = authRepository.updateProfile(
            uid, displayName, phoneNumber, dateOfBirth, avatarUrl
        )

        _uiState.update { it.copy(isLoading = false) }

        result.fold(
            onSuccess = {
                _navEvent.emit(AuthNavEvent.NavigateToHome)
            },
            onFailure = { error ->
                _uiState.update {
                    it.copy(errorMessage = "Cập nhật profile thất bại: ${error.message}")
                }
            }
        )
    }

    // CÁC HÀM KHÁC (GIỮ NGUYÊN NẾU BẠN CÓ)
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