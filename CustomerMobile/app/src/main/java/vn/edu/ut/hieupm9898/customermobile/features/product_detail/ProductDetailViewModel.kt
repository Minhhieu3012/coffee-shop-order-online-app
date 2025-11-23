package vn.edu.ut.hieupm9898.customermobile.features.product_detail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import vn.edu.ut.hieupm9898.customermobile.data.model.Product
import vn.edu.ut.hieupm9898.customermobile.data.remote.NetworkResult
import vn.edu.ut.hieupm9898.customermobile.data.repository.CartRepository
import vn.edu.ut.hieupm9898.customermobile.data.repository.ProductRepository
import vn.edu.ut.hieupm9898.customermobile.ui.components.SnackbarController
import javax.inject.Inject

data class ProductDetailUiState(
    val product: Product? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val selectedSize: String = "Trung bình",
    val selectedDairy: String = "Whole Milk",
    val quantity: Int = 1,
    val isFavorite: Boolean = false,
    val relatedProducts: List<Product> = emptyList(),

    // ✅ Thêm state cho add to cart
    val isAddingToCart: Boolean = false
)

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val cartRepository: CartRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductDetailUiState())
    val uiState: StateFlow<ProductDetailUiState> = _uiState.asStateFlow()

    fun loadProduct(productId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            when (val result = productRepository.getProductById(productId)) {
                is NetworkResult.Success -> {
                    val product = result.data
                    loadRelatedProducts(product.category, product.id)

                    _uiState.update {
                        it.copy(
                            product = product,
                            isLoading = false,
                            isFavorite = product.isFavorite
                        )
                    }
                }
                is NetworkResult.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = result.message
                        )
                    }
                }
                is NetworkResult.Loading -> {}
            }
        }
    }

    private suspend fun loadRelatedProducts(category: String, currentProductId: String) {
        when (val result = productRepository.getProductsByCategory(category)) {
            is NetworkResult.Success -> {
                val related = result.data
                    .filter { it.id != currentProductId }
                    .take(5)

                _uiState.update { it.copy(relatedProducts = related) }
            }
            else -> {}
        }
    }

    fun selectSize(size: String) {
        _uiState.update { it.copy(selectedSize = size) }
    }

    fun selectDairy(dairy: String) {
        _uiState.update { it.copy(selectedDairy = dairy) }
    }

    fun increaseQuantity() {
        _uiState.update { it.copy(quantity = it.quantity + 1) }
    }

    fun decreaseQuantity() {
        val currentQuantity = _uiState.value.quantity
        if (currentQuantity > 1) {
            _uiState.update { it.copy(quantity = currentQuantity - 1) }
        }
    }

    fun toggleFavorite() {
        _uiState.update { it.copy(isFavorite = !it.isFavorite) }

        val product = _uiState.value.product
        if (product != null) {
            Log.d("ProductDetailVM", "❤️ Toggle favorite: ${product.name}")
        }
    }

    /**
     * ✅ THÊM VÀO GIỎ HÀNG - Cải tiến với thông báo
     */
    fun addToCart() {
        viewModelScope.launch {
            val state = _uiState.value
            val product = state.product

            if (product == null) {
                Log.e("ProductDetailVM", "❌ Cannot add to cart: product is null")
                SnackbarController.showError("Không thể thêm vào giỏ hàng")
                return@launch
            }

            // Bắt đầu loading
            _uiState.update { it.copy(isAddingToCart = true) }

            try {
                // Thêm vào giỏ
                val result = cartRepository.addToCart(
                    product = product,
                    quantity = state.quantity,
                    size = state.selectedSize,
                    dairy = state.selectedDairy,
                    notes = ""
                )

                if (result.isSuccess) {
                    Log.d("ProductDetailVM", "✅ Added to cart: ${product.name} x${state.quantity}")

                    // ✅ HIỂN THỊ THÔNG BÁO THÀNH CÔNG
                    SnackbarController.showSuccess(
                        message = "Đã thêm ${product.name} vào giỏ hàng",
                        actionLabel = "Xem giỏ"
                    )
                } else {
                    Log.e("ProductDetailVM", "❌ Failed to add to cart")
                    SnackbarController.showError("Không thể thêm vào giỏ hàng")
                }
            } catch (e: Exception) {
                Log.e("ProductDetailVM", "❌ Error adding to cart: ${e.message}", e)
                SnackbarController.showError("Lỗi: ${e.message}")
            } finally {
                _uiState.update { it.copy(isAddingToCart = false) }
            }
        }
    }

    fun retry(productId: String) {
        loadProduct(productId)
    }
}