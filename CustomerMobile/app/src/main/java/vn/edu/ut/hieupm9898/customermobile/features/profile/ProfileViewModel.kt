package vn.edu.ut.hieupm9898.customermobile.features.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import vn.edu.ut.hieupm9898.customermobile.data.model.UserProfile
import vn.edu.ut.hieupm9898.customermobile.data.repository.ProfileRepository
import javax.inject.Inject

// Trạng thái của màn hình Edit Profile
data class ProfileUiState(
    // [FIXED] Khởi tạo UserProfile chỉ với 3 tham số
    val user: UserProfile = UserProfile("", "", ""),
    val isLoading: Boolean = false,
    val saveSuccess: Boolean? = null
)

class ProfileViewModel @Inject constructor(
    private val repository: ProfileRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState(isLoading = true))
    val uiState: StateFlow<ProfileUiState> = _uiState

    init {
        loadProfile()
    }

    // Tải dữ liệu ban đầu
    private fun loadProfile() {
        viewModelScope.launch {
            val user = repository.loadUserProfile()
            _uiState.update { it.copy(user = user, isLoading = false) }
        }
    }

    // [FIXED] Hàm saveProfile chỉ nhận 3 tham số
    fun saveProfile(newName: String, newPhone: String, newEmail: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, saveSuccess = null) }

            val success = repository.saveUserProfile(newName, newPhone, newEmail)

            if (success) {
                // Cập nhật State với 3 tham số
                _uiState.update {
                    it.copy(
                        user = it.user.copy(name = newName, phone = newPhone, email = newEmail),
                        isLoading = false,
                        saveSuccess = true
                    )
                }
            } else {
                _uiState.update { it.copy(isLoading = false, saveSuccess = false) }
            }
        }
    }

    // Hàm reset trạng thái sau khi hiển thị Toast Success/Error
    fun resetSaveStatus() {
        _uiState.update { it.copy(saveSuccess = null) }
    }
}