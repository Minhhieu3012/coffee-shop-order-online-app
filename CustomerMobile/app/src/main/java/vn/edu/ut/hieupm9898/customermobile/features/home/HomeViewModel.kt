package vn.edu.ut.hieupm9898.customermobile.features.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
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
import vn.edu.ut.hieupm9898.customermobile.data.repository.CartRepository
import vn.edu.ut.hieupm9898.customermobile.data.repository.ProductRepository
import vn.edu.ut.hieupm9898.customermobile.utils.StringUtils.containsVietnamese
import vn.edu.ut.hieupm9898.customermobile.utils.StringUtils.normalizeForSearch
import javax.inject.Inject

/**
 * UI State cho HomeScreen
 */
data class HomeUiState(
    val products: List<Product> = emptyList(),       // BACKWARD COMPAT
    val allProducts: List<Product> = emptyList(),    // NGUỒN GỐC DÙNG CHO FILTER & SEARCH
    val searchResults: List<Product> = emptyList(),
    val isLoading: Boolean = false,
    val isSearching: Boolean = false,
    val errorMessage: String? = null,
    val selectedCategory: String = "Tất cả",
    val searchQuery: String = "",
    val showSuggestions: Boolean = false,
    // ✅ THÊM STATE CHO CART NOTIFICATION
    val addToCartSuccess: Boolean = false,
    val addToCartMessage: String? = null
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val firebaseAuth: FirebaseAuth,
    private val cartRepository: CartRepository // ✅ INJECT CART REPOSITORY
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private var searchJob: Job? = null

    init {
        loadProducts()
    }

    /**
     * Load sản phẩm từ Firebase
     * - Nếu đã đăng nhập: load kèm trạng thái isFavorite cho user đó
     * - Nếu chưa đăng nhập: load list thường
     * - Nếu lỗi: dùng offlineProducts
     */
    fun loadProducts() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            delay(500)

            val userId = firebaseAuth.currentUser?.uid
            val result = if (userId != null) {
                productRepository.getAllProductsWithFavorites(userId)
            } else {
                productRepository.getAllProducts()
            }

            when (result) {
                is NetworkResult.Success -> {
                    Log.d(
                        "HomeViewModel",
                        "📦 Total products from Firebase: ${result.data.size}"
                    )

                    // ✅ LOG KIỂM TRA STOCK STATUS
                    result.data.forEach { product ->
                        Log.d("HomeViewModel", """
                        📝 Product: ${product.name}
                        - ID: ${product.id}
                        - isAvailable: ${product.isAvailable}
                        - isOutOfStock(): ${product.isOutOfStock()}
                    """.trimIndent())
                    }

                    _uiState.update {
                        it.copy(
                            allProducts = result.data,
                            products = result.data,
                            isLoading = false,
                            errorMessage = null
                        )
                    }
                }

                is NetworkResult.Error -> {
                    Log.e("HomeViewModel", "❌ Error loading products: ${result.message}")

                    val offlineProducts = productRepository.getOfflineProducts()
                    Log.d(
                        "HomeViewModel",
                        "📦 Using offline products: ${offlineProducts.size}"
                    )

                    _uiState.update {
                        it.copy(
                            allProducts = offlineProducts,
                            products = offlineProducts,
                            isLoading = false,
                            errorMessage = result.message
                        )
                    }
                }

                is NetworkResult.Loading -> {
                    // không dùng case này ở đây
                }
            }
        }
    }

    /**
     * Toggle favorite:
     * - Update UI trước (optimistic update)
     * - Gọi xuống repository.sync với Firestore
     * - Nếu lỗi -> rollback
     */
    fun toggleFavorite(productId: String) {
        val userId = firebaseAuth.currentUser?.uid
        if (userId == null) {
            _uiState.update {
                it.copy(
                    errorMessage = "Vui lòng đăng nhập để thêm yêu thích"
                )
            }
            return
        }

        viewModelScope.launch {
            val currentProduct = _uiState.value.allProducts.find { it.id == productId }

            if (currentProduct == null) {
                Log.e("HomeViewModel", "⚠️ Không tìm thấy sản phẩm id: $productId")
                return@launch
            }

            val currentStatus = currentProduct.isFavorite
            val newStatus = !currentStatus

            // 1. Optimistic update
            updateProductStatusInUi(productId, newStatus)

            // 2. Gọi xuống Repository để sync với Firestore
            when (val result =
                productRepository.toggleFavorite(userId, productId, currentStatus)) {
                is NetworkResult.Success -> {
                    Log.d(
                        "HomeViewModel",
                        "✅ Toggle favorite thành công cho productId=$productId, newStatus=$newStatus"
                    )
                }

                is NetworkResult.Error -> {
                    Log.e(
                        "HomeViewModel",
                        "❌ Lỗi toggle favorite: ${result.message}"
                    )
                    // 3. Rollback nếu lỗi
                    updateProductStatusInUi(productId, currentStatus)
                    _uiState.update { it.copy(errorMessage = "Không thể cập nhật yêu thích") }
                }

                is NetworkResult.Loading -> Unit
            }
        }
    }

    /**
     * ✅ THÊM NHANH VÀO GIỎ HÀNG (từ nút + trên card)
     */
    fun quickAddToCart(product: Product) {
        // ✅ KIỂM TRA HẾT HÀNG TRƯỚC KHI THÊM
        if (product.isOutOfStock()) {
            _uiState.update {
                it.copy(
                    addToCartSuccess = false,
                    addToCartMessage = "❌ ${product.name} hiện đã hết hàng"
                )
            }
            return
        }

        viewModelScope.launch {
            try {
                Log.d("HomeViewModel", "🛒 Quick adding to cart: ${product.name}")

                val result = cartRepository.addToCart(
                    product = product,
                    quantity = 1,
                    size = "Trung bình",
                    dairy = "Whole Milk",
                    notes = ""
                )

                if (result.isSuccess) {
                    Log.d("HomeViewModel", "✅ Quick added: ${product.name}")

                    // ✅ CẬP NHẬT STATE ĐỂ HIỂN THỊ THÔNG BÁO
                    _uiState.update {
                        it.copy(
                            addToCartSuccess = true,
                            addToCartMessage = "Đã thêm ${product.name} vào giỏ hàng"
                        )
                    }
                } else {
                    Log.e("HomeViewModel", "❌ Failed to add to cart")
                    _uiState.update {
                        it.copy(
                            addToCartSuccess = false,
                            addToCartMessage = "Không thể thêm vào giỏ hàng"
                        )
                    }
                }
            } catch (e: Exception) {
                Log.e("HomeViewModel", "❌ Error quick add: ${e.message}", e)
                _uiState.update {
                    it.copy(
                        addToCartSuccess = false,
                        addToCartMessage = "Lỗi: ${e.message}"
                    )
                }
            }
        }
    }

    /**
     * ✅ RESET CART NOTIFICATION STATE
     * Gọi sau khi đã hiển thị Snackbar
     */
    fun resetCartNotification() {
        _uiState.update {
            it.copy(
                addToCartSuccess = false,
                addToCartMessage = null
            )
        }
    }

    /**
     * Cập nhật trạng thái isFavorite cho tất cả list trong UI:
     * - allProducts: nguồn gốc
     * - products: list dùng cho code cũ
     * - searchResults: để UI search phản ánh đúng
     */
    private fun updateProductStatusInUi(productId: String, isFav: Boolean) {
        _uiState.update { state ->
            val updatedAll = state.allProducts.map { p ->
                if (p.id == productId) p.copy(isFavorite = isFav) else p
            }

            val updatedProducts = state.products.map { p ->
                if (p.id == productId) p.copy(isFavorite = isFav) else p
            }

            val updatedSearch = state.searchResults.map { p ->
                if (p.id == productId) p.copy(isFavorite = isFav) else p
            }

            state.copy(
                allProducts = updatedAll,
                products = updatedProducts,
                searchResults = updatedSearch
            )
        }
    }

    // =====================================================
    // SEARCH & SUGGESTION
    // =====================================================

    /**
     * Xử lý gõ search: debounce + bỏ dấu + show suggestion
     */
    fun onSearchQueryChange(query: String) {
        _uiState.update {
            it.copy(
                searchQuery = query,
                showSuggestions = query.isNotEmpty()
            )
        }

        // hủy job cũ (debounce)
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
     * Search local trên allProducts, bỏ dấu + ưu tiên match đầu từ
     */
    private fun searchProductsLocal(query: String): List<Product> {
        if (query.isEmpty()) return emptyList()

        val normalizedQuery = query.normalizeForSearch()
        val state = _uiState.value

        return state.allProducts
            .filter { product ->
                product.name.containsVietnamese(query) ||
                        product.description.containsVietnamese(query) ||
                        product.category.containsVietnamese(query)
            }
            .sortedByDescending { product ->
                when {
                    product.name.normalizeForSearch().startsWith(normalizedQuery) -> 3
                    product.name.containsVietnamese(query) -> 2
                    product.description.containsVietnamese(query) -> 1
                    else -> 0
                }
            }
            .take(5)
    }

    fun selectSuggestion(product: Product) {
        _uiState.update {
            it.copy(
                searchQuery = product.name,
                showSuggestions = false
            )
        }
    }

    fun hideSuggestions() {
        _uiState.update { it.copy(showSuggestions = false) }
    }

    fun clearSearch() {
        _uiState.update {
            it.copy(
                searchQuery = "",
                searchResults = emptyList(),
                showSuggestions = false
            )
        }
    }

    // =====================================================
    // FILTER & GET DATA CHO UI
    // =====================================================

    fun filterByCategory(category: String) {
        _uiState.update { it.copy(selectedCategory = category) }
    }

    /**
     * Hàm lấy danh sách hiển thị cuối cùng lên HomeScreen
     * - Kết hợp category filter + search query
     */
    fun getFilteredProducts(): List<Product> {
        val state = _uiState.value
        var filtered = state.allProducts

        Log.d("HomeViewModel", "🔍 getFilteredProducts() called")
        Log.d("HomeViewModel", "📦 Total allProducts: ${state.allProducts.size}")
        Log.d("HomeViewModel", "📂 Selected category: ${state.selectedCategory}")

        // 1️⃣ Filter theo category
        if (state.selectedCategory != "Tất cả") {
            val categoryMap = mapOf(
                "Cà phê" to listOf("coffee", "cà phê"),
                "Trà" to listOf("tea", "trà"),
                "Đá xay" to listOf("đá xay", "smoothie", "frappe"),
                "Đồ ăn" to listOf("food", "đồ ăn")
            )

            val targetCategories =
                categoryMap[state.selectedCategory] ?: listOf(state.selectedCategory.lowercase())

            Log.d("HomeViewModel", "🎯 Target categories: $targetCategories")

            filtered = filtered.filter { product ->
                val matches = targetCategories.any { cat ->
                    product.category.lowercase().contains(cat.lowercase())
                }

                Log.d(
                    "HomeViewModel",
                    "Product: ${product.name} | Category: '${product.category}' | Match: $matches"
                )

                matches
            }

            Log.d("HomeViewModel", "✅ Filtered result: ${filtered.size} products")
        }

        // 2️⃣ Filter theo search query (nếu user gõ)
        if (state.searchQuery.isNotEmpty()) {
            filtered = filtered.filter { product ->
                product.name.containsVietnamese(state.searchQuery) ||
                        product.description.containsVietnamese(state.searchQuery)
            }
        }

        return filtered
    }

    /**
     * Đếm số lượng sản phẩm theo category – dùng cho chip/badge
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
     * ✅ CLEAR ERROR MESSAGE
     */
    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    /**
     * Retry khi có lỗi
     */
    fun retry() {
        loadProducts()
    }
}