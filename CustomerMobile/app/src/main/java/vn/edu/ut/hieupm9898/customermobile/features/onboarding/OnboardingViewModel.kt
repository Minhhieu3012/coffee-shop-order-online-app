package vn.edu.ut.hieupm9898.customermobile.features.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class OnboardingViewModel : ViewModel() {

    private val _onboardingCompleted = MutableStateFlow(false)
    val onboardingCompleted: StateFlow<Boolean> = _onboardingCompleted

    init {

        viewModelScope.launch {
            // Logic đọc dữ liệu ở đây
            _onboardingCompleted.value = false
        }
    }


    fun setOnboardingCompleted() {
        viewModelScope.launch {

            _onboardingCompleted.value = true
        }
    }
}