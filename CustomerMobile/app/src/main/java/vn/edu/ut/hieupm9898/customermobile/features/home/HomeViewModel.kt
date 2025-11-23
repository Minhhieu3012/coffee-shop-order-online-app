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
    val allProducts: List<Product> = emptyList(), // 👈 LƯU TẤT CẢ SẢN PHẨM
    val searchResults: List<Product> = emptyList(),
    val isLoading: Boolean = false,
    val isSearching: Boolean = false,
    val errorMessage: String? = null,
    val selectedCategory: String = "Tất cả",
    val searchQuery: String = "",
    val showSuggestions: Boolean = false
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val productRepository: ProductRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private var searchJob: Job? = null

    init {
        loadProducts()
    }

    /**
     * Load sản phẩm từ Firebase
     */
    fun loadProducts() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            delay(2000)

            when (val result = productRepository.getAllProducts()) {
                is NetworkResult.Success -> {
                    // 🔍 LOG ĐỂ XEM DỮ LIỆU TRẢ VỀ
                    android.util.Log.d("HomeViewModel", "📦 Total products from Firebase: ${result.data.size}")

                    result.data.forEachIndexed { index, product ->
                        android.util.Log.d("HomeViewModel",
                            "[$index] ${product.name} | Category: '${product.category}' | ImageURL: '${product.imageUrl}'"
                        )
                    }

                    _uiState.update {
                        it.copy(
                            allProducts = result.data,
                            isLoading = false,
                            errorMessage = null
                        )
                    }
                }
                is NetworkResult.Error -> {
                    android.util.Log.e("HomeViewModel", "❌ Error loading products: ${result.message}")

                    // Dùng offline data
                    val offlineProducts = productRepository.getOfflineProducts()
                    android.util.Log.d("HomeViewModel", "📦 Using offline products: ${offlineProducts.size}")

                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = result.message,
                            allProducts = offlineProducts
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
        val currentState = _uiState.value

        return currentState.allProducts.filter { product ->
            product.name.containsVietnamese(query) ||
                    product.description.containsVietnamese(query) ||
                    product.category.containsVietnamese(query)
        }.sortedByDescending { product ->
            when {
                product.name.normalizeForSearch().startsWith(normalizedQuery) -> 3
                product.name.containsVietnamese(query) -> 2
                product.description.containsVietnamese(query) -> 1
                else -> 0
            }
        }.take(5)
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
     * Lọc sản phẩm theo category - CHỈ THAY ĐỔI selectedCategory
     */
    fun filterByCategory(category: String) {
        _uiState.update { it.copy(selectedCategory = category) }
    }

    /**
     * Lấy danh sách sản phẩm đã lọc theo category và search query
     * 🔥 HÀM NÀY QUAN TRỌNG - DÙNG TRONG HomeScreen
     */
    fun getFilteredProducts(): List<Product> {
        val state = _uiState.value
        var filtered = state.allProducts

        android.util.Log.d("HomeViewModel", "🔍 getFilteredProducts() called")
        android.util.Log.d("HomeViewModel", "📦 Total allProducts: ${state.allProducts.size}")
        android.util.Log.d("HomeViewModel", "📂 Selected category: ${state.selectedCategory}")

        // 1️⃣ Filter theo category (nếu không phải "Tất cả")
        if (state.selectedCategory != "Tất cả") {
            val categoryMap = mapOf(
                "Cà phê" to listOf("coffee", "cà phê"),
                "Trà" to listOf("tea", "trà"),
                "Đá xay" to listOf("đá xay", "smoothie", "frappe"), // 👈 THÊM CATEGORY MỚI
                "Đồ ăn" to listOf("food", "đồ ăn")
            )

            val targetCategories = categoryMap[state.selectedCategory] ?: listOf(state.selectedCategory.lowercase())

            android.util.Log.d("HomeViewModel", "🎯 Target categories: $targetCategories")

            filtered = filtered.filter { product ->
                val matches = targetCategories.any { cat ->
                    product.category.lowercase().contains(cat.lowercase())
                }

                android.util.Log.d("HomeViewModel",
                    "Product: ${product.name} | Category: '${product.category}' | Match: $matches"
                )

                matches
            }

            android.util.Log.d("HomeViewModel", "✅ Filtered result: ${filtered.size} products")
        }

        // 2️⃣ Filter theo search query (nếu có)
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
            val updatedProducts = state.allProducts.map { product ->
                if (product.id == productId) {
                    product.copy(isFavorite = !product.isFavorite)
                } else {
                    product
                }
            }
            state.copy(allProducts = updatedProducts) // 👈 CẬP NHẬT allProducts
        }
    }

    /**
     * Đếm số lượng sản phẩm theo category
     */
    fun getProductCountByCategory(category: String): Int {
        val state = _uiState.value

        if (category == "Tất cả") return state.allProducts.size

        val categoryMap = mapOf(
            "Cà phê" to "Coffee",
            "Trà" to "Tea",
            "Đồ ăn" to "Food"
        )

        val englishCategory = categoryMap[category] ?: category

        return state.allProducts.count { product ->
            product.category.equals(englishCategory, ignoreCase = true)
        }
    }

    /**
     * Retry khi có lỗi
     */
    fun retry() {
        loadProducts()
    }
}