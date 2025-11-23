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
import javax.inject.Inject

/**
 * UI State cho Product Detail Screen
 */
data class ProductDetailUiState(
    val product: Product? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val selectedSize: String = "Trung bình",
    val selectedDairy: String = "Whole Milk",
    val quantity: Int = 1,
    val isFavorite: Boolean = false,
    val relatedProducts: List<Product> = emptyList()
)

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val cartRepository: CartRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductDetailUiState())
    val uiState: StateFlow<ProductDetailUiState> = _uiState.asStateFlow()

    /**
     * Load product từ Firebase theo ID
     */
    fun loadProduct(productId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            Log.d("ProductDetailVM", "🔍 Loading product: $productId")

            when (val result = productRepository.getProductById(productId)) {
                is NetworkResult.Success -> {
                    val product = result.data
                    Log.d("ProductDetailVM", "✅ Loaded product: ${product.name}")

                    // Load related products (cùng category)
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
                    Log.e("ProductDetailVM", "❌ Error loading product: ${result.message}")
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

    /**
     * Load sản phẩm liên quan (cùng category)
     */
    private suspend fun loadRelatedProducts(category: String, currentProductId: String) {
        when (val result = productRepository.getProductsByCategory(category)) {
            is NetworkResult.Success -> {
                val related = result.data
                    .filter { it.id != currentProductId } // Loại bỏ sản phẩm hiện tại
                    .take(5) // Chỉ lấy 5 sản phẩm

                _uiState.update { it.copy(relatedProducts = related) }
            }
            else -> {
                Log.d("ProductDetailVM", "⚠️ Could not load related products")
            }
        }
    }

    /**
     * Chọn size
     */
    fun selectSize(size: String) {
        _uiState.update { it.copy(selectedSize = size) }
    }

    /**
     * Chọn dairy
     */
    fun selectDairy(dairy: String) {
        _uiState.update { it.copy(selectedDairy = dairy) }
    }

    /**
     * Tăng số lượng
     */
    fun increaseQuantity() {
        _uiState.update { it.copy(quantity = it.quantity + 1) }
    }

    /**
     * Giảm số lượng
     */
    fun decreaseQuantity() {
        val currentQuantity = _uiState.value.quantity
        if (currentQuantity > 1) {
            _uiState.update { it.copy(quantity = currentQuantity - 1) }
        }
    }

    /**
     * Toggle favorite
     */
    fun toggleFavorite() {
        _uiState.update { it.copy(isFavorite = !it.isFavorite) }

        // TODO: Cập nhật lên Firebase
        val product = _uiState.value.product
        if (product != null) {
            Log.d("ProductDetailVM", "❤️ Toggle favorite: ${product.name}")
        }
    }

    /**
     * Tính tổng tiền
     */
    fun calculateTotalPrice(): Double {
        val state = _uiState.value
        val product = state.product ?: return 0.0

        val basePrice = product.price

        // Giá theo size
        val sizePrice = when (state.selectedSize) {
            "Nhỏ" -> 0.0
            "Trung bình" -> 5000.0
            "Lớn" -> 10000.0
            else -> 0.0
        }

        // Giá theo dairy (nếu cần)
        val dairyPrice = when (state.selectedDairy) {
            "Almond Milk" -> 5000.0
            "Oat Milk" -> 7000.0
            else -> 0.0
        }

        return (basePrice + sizePrice + dairyPrice) * state.quantity
    }

    /**
     * Thêm vào giỏ hàng
     */
    fun addToCart() {
        viewModelScope.launch {
            val state = _uiState.value
            val product = state.product

            if (product == null) {
                Log.e("ProductDetailVM", "❌ Cannot add to cart: product is null")
                return@launch
            }

            // Tính giá điều chỉnh
            val sizePrice = when (state.selectedSize) {
                "Trung bình" -> 5000.0
                "Lớn" -> 10000.0
                else -> 0.0
            }

            val dairyPrice = when (state.selectedDairy) {
                "Almond Milk" -> 5000.0
                "Oat Milk" -> 7000.0
                else -> 0.0
            }

            // Tạo product với giá mới
            val productToAdd = product.copy(
                id = "${product.id}_${state.selectedSize}_${state.selectedDairy}",
                name = "${product.name} (${state.selectedSize}, ${state.selectedDairy})",
                price = product.price + sizePrice + dairyPrice
            )

            cartRepository.addToCart(productToAdd, state.quantity)

            Log.d("ProductDetailVM", "✅ Added to cart: ${productToAdd.name} x${state.quantity}")
        }
    }

    /**
     * Retry khi có lỗi
     */
    fun retry(productId: String) {
        loadProduct(productId)
    }
}