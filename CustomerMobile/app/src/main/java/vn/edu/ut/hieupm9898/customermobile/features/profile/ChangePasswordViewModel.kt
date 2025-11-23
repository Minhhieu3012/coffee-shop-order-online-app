package vn.edu.ut.hieupm9898.customermobile.features.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import vn.edu.ut.hieupm9898.customermobile.data.repository.AuthRepository
import javax.inject.Inject

@HiltViewModel
class ChangePasswordViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    private val _isSuccess = MutableStateFlow(false)
    val isSuccess = _isSuccess.asStateFlow()

    fun changePassword(
        oldPassword: String,
        newPassword: String,
        confirmPassword: String
    ) = viewModelScope.launch {
        // Validation
        if (oldPassword.isBlank()) {
            _errorMessage.value = "Vui lòng nhập mật khẩu cũ"
            return@launch
        }

        if (newPassword.isBlank()) {
            _errorMessage.value = "Vui lòng nhập mật khẩu mới"
            return@launch
        }

        if (newPassword.length < 6) {
            _errorMessage.value = "Mật khẩu mới phải có ít nhất 6 ký tự"
            return@launch
        }

        if (newPassword != confirmPassword) {
            _errorMessage.value = "Mật khẩu xác nhận không khớp"
            return@launch
        }

        if (oldPassword == newPassword) {
            _errorMessage.value = "Mật khẩu mới phải khác mật khẩu cũ"
            return@launch
        }

        _isLoading.value = true
        _errorMessage.value = null

        val result = authRepository.changePassword(oldPassword, newPassword)

        result.fold(
            onSuccess = {
                _isSuccess.value = true
            },
            onFailure = { error ->
                _errorMessage.value = error.message
            }
        )

        _isLoading.value = false
    }

    fun clearError() {
        _errorMessage.value = null
    }

    fun clearSuccess() {
        _isSuccess.value = false
    }
}