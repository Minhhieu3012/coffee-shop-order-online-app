package vn.edu.ut.hieupm9898.customermobile.features.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import vn.edu.ut.hieupm9898.customermobile.data.model.User
import vn.edu.ut.hieupm9898.customermobile.data.repository.AuthRepository
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser = _currentUser.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    private val _isSuccess = MutableStateFlow(false)
    val isSuccess = _isSuccess.asStateFlow()

    init {
        loadCurrentUser()
    }

    private fun loadCurrentUser() = viewModelScope.launch {
        _isLoading.value = true
        val result = authRepository.getCurrentUser()

        result.fold(
            onSuccess = { user ->
                _currentUser.value = user
            },
            onFailure = { error ->
                _errorMessage.value = error.message
            }
        )
        _isLoading.value = false
    }

    fun updateProfile(
        displayName: String,
        phoneNumber: String,
        dateOfBirth: String = "",
        avatarUrl: String = ""
    ) = viewModelScope.launch {
        val uid = _currentUser.value?.uid ?: run {
            _errorMessage.value = "Không tìm thấy thông tin người dùng"
            return@launch
        }

        // Validation
        if (displayName.isBlank()) {
            _errorMessage.value = "Tên không được để trống"
            return@launch
        }

        if (phoneNumber.isBlank()) {
            _errorMessage.value = "Số điện thoại không được để trống"
            return@launch
        }

        _isLoading.value = true
        _errorMessage.value = null

        val result = authRepository.updateProfile(
            uid = uid,
            displayName = displayName,
            phoneNumber = phoneNumber,
            dateOfBirth = dateOfBirth,
            avatarUrl = avatarUrl
        )

        result.fold(
            onSuccess = {
                _isSuccess.value = true
                loadCurrentUser() // Reload user data
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