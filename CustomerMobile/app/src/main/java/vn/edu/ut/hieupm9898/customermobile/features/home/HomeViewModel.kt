package vn.edu.ut.hieupm9898.customermobile.features.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import vn.edu.ut.hieupm9898.customermobile.data.model.Product
import vn.edu.ut.hieupm9898.customermobile.data.remote.NetworkResult
import vn.edu.ut.hieupm9898.customermobile.data.repository.ProductRepository
import vn.edu.ut.hieupm9898.customermobile.utils.StringUtils.containsVietnamese
import vn.edu.ut.hieupm9898.customermobile.utils.StringUtils.normalizeForSearch
import javax.inject.Inject

/**
 * UI State cho HomeScreen
 */
data class HomeUiState(
    val products: List<Product> = emptyList(),
    val searchResults: List<Product> = emptyList(), // Kết quả gợi ý
    val isLoading: Boolean = false,
    val isSearching: Boolean = false, // Loading riêng cho search
    val errorMessage: String? = null,
    val selectedCategory: String = "Tất cả",
    val searchQuery: String = "",
    val showSuggestions: Boolean = false // Hiển thị dropdown gợi ý
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val productRepository: ProductRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private var searchJob: Job? = null
    private val allProducts = mutableListOf<Product>()

    init {
        loadProducts()
    }

    /**
     * Load sản phẩm từ Firebase
     */
    fun loadProducts() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            delay(2000) // Delay để hiển thị skeleton

            when (val result = productRepository.getAllProducts()) {
                is NetworkResult.Success -> {
                    allProducts.clear()
                    allProducts.addAll(result.data)

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
                            products = productRepository.getOfflineProducts()
                        )
                    }
                }
                is NetworkResult.Loading -> {}
            }
        }
    }

    /**
     * Tìm kiếm với debounce và bỏ dấu tiếng Việt
     */
    fun onSearchQueryChange(query: String) {
        _uiState.update {
            it.copy(
                searchQuery = query,
                showSuggestions = query.isNotEmpty()
            )
        }

        // Cancel search job cũ
        searchJob?.cancel()

        if (query.isEmpty()) {
            _uiState.update {
                it.copy(
                    searchResults = emptyList(),
                    showSuggestions = false
                )
            }
            return
        }

        // Debounce 300ms
        searchJob = viewModelScope.launch {
            _uiState.update { it.copy(isSearching = true) }
            delay(300)

            val results = searchProductsLocal(query)

            _uiState.update {
                it.copy(
                    searchResults = results,
                    isSearching = false
                )
            }
        }
    }

    /**
     * Tìm kiếm local với bỏ dấu tiếng Việt
     */
    private fun searchProductsLocal(query: String): List<Product> {
        if (query.isEmpty()) return emptyList()

        val normalizedQuery = query.normalizeForSearch()

        return allProducts.filter { product ->
            // Tìm kiếm trong tên
            product.name.containsVietnamese(query) ||
                    // Tìm kiếm trong mô tả
                    product.description.containsVietnamese(query) ||
                    // Tìm kiếm trong category
                    product.category.containsVietnamese(query)
        }.sortedByDescending { product ->
            // Ưu tiên kết quả match với tên
            when {
                product.name.normalizeForSearch().startsWith(normalizedQuery) -> 3
                product.name.containsVietnamese(query) -> 2
                product.description.containsVietnamese(query) -> 1
                else -> 0
            }
        }.take(5) // Chỉ hiển thị top 5 gợi ý
    }

    /**
     * Chọn sản phẩm từ gợi ý
     */
    fun selectSuggestion(product: Product) {
        _uiState.update {
            it.copy(
                searchQuery = product.name,
                showSuggestions = false
            )
        }
    }

    /**
     * Ẩn gợi ý
     */
    fun hideSuggestions() {
        _uiState.update { it.copy(showSuggestions = false) }
    }

    /**
     * Clear search
     */
    fun clearSearch() {
        _uiState.update {
            it.copy(
                searchQuery = "",
                searchResults = emptyList(),
                showSuggestions = false
            )
        }
    }

    /**
     * Lọc sản phẩm theo category
     */
    fun filterByCategory(category: String) {
        _uiState.update { it.copy(selectedCategory = category) }
    }

    /**
     * Lấy danh sách sản phẩm đã lọc
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

        // Filter by search query
        if (state.searchQuery.isNotEmpty()) {
            filtered = filtered.filter { product ->
                product.name.containsVietnamese(state.searchQuery) ||
                        product.description.containsVietnamese(state.searchQuery)
            }
        }

        return filtered
    }

    /**
     * Toggle favorite
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

        // Update trong allProducts
        val index = allProducts.indexOfFirst { it.id == productId }
        if (index != -1) {
            allProducts[index] = allProducts[index].copy(
                isFavorite = !allProducts[index].isFavorite
            )
        }
    }

    /**
     * Retry khi có lỗi
     */
    fun retry() {
        loadProducts()
    }
}