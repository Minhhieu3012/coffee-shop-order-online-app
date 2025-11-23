package vn.edu.ut.hieupm9898.customermobile.features.favorite

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import vn.edu.ut.hieupm9898.customermobile.data.model.Product
import vn.edu.ut.hieupm9898.customermobile.data.remote.NetworkResult
import vn.edu.ut.hieupm9898.customermobile.data.repository.ProductRepository
import javax.inject.Inject

data class FavoriteUiState(
    val favoriteProducts: List<Product> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

    private val _uiState = MutableStateFlow(FavoriteUiState())
    val uiState: StateFlow<FavoriteUiState> = _uiState.asStateFlow()

    private fun requireUserIdOrSetError(): String? {
        val userId = firebaseAuth.currentUser?.uid
        if (userId == null) {
            Log.w("FavoriteViewModel", "User not logged in")
            _uiState.update {
                it.copy(
                    isLoading = false,
                    favoriteProducts = emptyList(),
                    errorMessage = "Vui lòng đăng nhập để xem danh sách yêu thích"
                )
            }
        }
        return userId
    }

    fun loadFavoriteProducts() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            try {
                val userId = requireUserIdOrSetError() ?: return@launch

                when (val result = productRepository.getUserFavoriteProducts(userId)) {
                    is NetworkResult.Success -> {
                        _uiState.update {
                            it.copy(
                                favoriteProducts = result.data,
                                isLoading = false,
                                errorMessage = null
                            )
                        }
                    }
                    is NetworkResult.Error -> {
                        _uiState.update {
                            it.copy(
                                favoriteProducts = emptyList(),
                                isLoading = false,
                                errorMessage = result.message ?: "Lỗi tải danh sách yêu thích"
                            )
                        }
                    }
                    is NetworkResult.Loading -> Unit
                }
            } catch (e: Exception) {
                Log.e("FavoriteViewModel", "Unexpected error: ${e.message}", e)
                _uiState.update {
                    it.copy(
                        favoriteProducts = emptyList(),
                        isLoading = false,
                        errorMessage = "Đã xảy ra lỗi khi tải danh sách yêu thích"
                    )
                }
            }
        }
    }

    fun toggleFavorite(productId: String) {
        val userId = requireUserIdOrSetError() ?: return

        viewModelScope.launch {
            try {
                val currentProduct =
                    _uiState.value.favoriteProducts.find { it.id == productId } ?: return@launch

                // Optimistic update
                _uiState.update { state ->
                    state.copy(
                        favoriteProducts = state.favoriteProducts.filter { it.id != productId }
                    )
                }

                when (val result =
                    productRepository.removeFromFavorites(userId, productId)) {
                    is NetworkResult.Success -> Unit
                    is NetworkResult.Error -> {
                        // rollback
                        _uiState.update { state ->
                            state.copy(
                                favoriteProducts = state.favoriteProducts + currentProduct,
                                errorMessage = "Lỗi khi xóa khỏi yêu thích, vui lòng thử lại"
                            )
                        }
                    }
                    is NetworkResult.Loading -> Unit
                }
            } catch (e: Exception) {
                Log.e("FavoriteViewModel", "Unexpected error toggleFavorite: ${e.message}", e)
                _uiState.update {
                    it.copy(errorMessage = "Đã xảy ra lỗi khi cập nhật yêu thích")
                }
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    fun retry() {
        loadFavoriteProducts()
    }
}