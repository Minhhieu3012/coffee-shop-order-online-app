package vn.edu.ut.hieupm9898.customermobile.features.home

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
import vn.edu.ut.hieupm9898.customermobile.data.repository.ProductRepository
import javax.inject.Inject

/**
 * UI State cho HomeScreen
 */
data class HomeUiState(
    val products: List<Product> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val selectedCategory: String = "Tất cả",
    val searchQuery: String = ""
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val productRepository: ProductRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadProducts()
    }

    /**
     * Load sản phẩm từ Firebase (One-time fetch)
     */
    fun loadProducts() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            when (val result = productRepository.getAllProducts()) {
                is NetworkResult.Success -> {
                    _uiState.update {
                        it.copy(
                            products = result.data,
                            isLoading = false,
                            errorMessage = null
                        )
                    }
                }
                is NetworkResult.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = result.message,
                            // Fallback: dùng dữ liệu offline
                            products = productRepository.getOfflineProducts()
                        )
                    }
                }
                is NetworkResult.Loading -> {
                    // Đã set loading ở trên
                }
            }
        }
    }

    /**
     * Load sản phẩm real-time (Flow)
     * Dùng khi muốn UI tự động cập nhật
     */
    fun observeProducts() {
        viewModelScope.launch {
            productRepository.observeProducts().collect { result ->
                when (result) {
                    is NetworkResult.Success -> {
                        _uiState.update {
                            it.copy(
                                products = result.data,
                                isLoading = false,
                                errorMessage = null
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
                    is NetworkResult.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }
                }
            }
        }
    }

    /**
     * Lọc sản phẩm theo category
     */
    fun filterByCategory(category: String) {
        _uiState.update { it.copy(selectedCategory = category) }
    }

    /**
     * Tìm kiếm sản phẩm
     */
    fun searchProducts(query: String) {
        _uiState.update { it.copy(searchQuery = query) }

        if (query.isEmpty()) {
            loadProducts()
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            when (val result = productRepository.searchProducts(query)) {
                is NetworkResult.Success -> {
                    _uiState.update {
                        it.copy(
                            products = result.data,
                            isLoading = false
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

    /**
     * Lọc sản phẩm hiển thị dựa trên category và search
     */
    fun getFilteredProducts(): List<Product> {
        val state = _uiState.value
        var filtered = state.products

        // Filter by category
        if (state.selectedCategory != "Tất cả") {
            val categoryMap = mapOf(
                "Cà phê" to "Coffee",
                "Trà" to "Tea",
                "Đồ ăn" to "Food"
            )
            val englishCategory = categoryMap[state.selectedCategory]
            if (englishCategory != null) {
                filtered = filtered.filter { it.category == englishCategory }
            }
        }

        // Filter by search
        if (state.searchQuery.isNotEmpty()) {
            filtered = filtered.filter { product ->
                product.name.contains(state.searchQuery, ignoreCase = true) ||
                        product.description.contains(state.searchQuery, ignoreCase = true)
            }
        }

        return filtered
    }

    /**
     * Toggle favorite (tạm thời chỉ local, chưa sync Firebase)
     */
    fun toggleFavorite(productId: String) {
        _uiState.update { state ->
            val updatedProducts = state.products.map { product ->
                if (product.id == productId) {
                    product.copy(isFavorite = !product.isFavorite)
                } else {
                    product
                }
            }
            state.copy(products = updatedProducts)
        }
    }

    /**
     * Retry khi có lỗi
     */
    fun retry() {
        loadProducts()
    }
}