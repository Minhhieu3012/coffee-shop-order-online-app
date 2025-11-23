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
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser = _currentUser.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    init {
        loadCurrentUser()
    }

    fun loadCurrentUser() = viewModelScope.launch {
        _isLoading.value = true
        val result = authRepository.getCurrentUser()

        result.fold(
            onSuccess = { user ->
                _currentUser.value = user
            },
            onFailure = { error ->
                _currentUser.value = null
            }
        )
        _isLoading.value = false
    }

    fun refreshUser() {
        loadCurrentUser()
    }
}