package vn.edu.ut.hieupm9898.customermobile.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import vn.edu.ut.hieupm9898.customermobile.data.model.Product
import vn.edu.ut.hieupm9898.customermobile.data.remote.FirebaseDataSource
import vn.edu.ut.hieupm9898.customermobile.data.remote.NetworkResult
import vn.edu.ut.hieupm9898.customermobile.R
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductRepository @Inject constructor(
    private val firebaseDataSource: FirebaseDataSource
) {

    // --- Basic Methods ---
    suspend fun getAllProducts() = firebaseDataSource.getAllProducts()
    suspend fun getProductById(id: String) = firebaseDataSource.getProductById(id)
    suspend fun getProductsByCategory(cat: String) = firebaseDataSource.getProductsByCategory(cat)
    suspend fun searchProducts(query: String) = firebaseDataSource.searchProducts(query)
    fun observeProducts() = firebaseDataSource.observeProducts()

    // --- Favorite Logic (Fixed) ---

    /**
     * ✅ SỬA LỖI: Loại bỏ ép kiểu unsafe, xử lý logic an toàn
     */
    suspend fun getAllProductsWithFavorites(userId: String): NetworkResult<List<Product>> {
        val productsResult = firebaseDataSource.getAllProducts()

        // Nếu load product lỗi thì trả về lỗi luôn
        if (productsResult !is NetworkResult.Success) {
            return productsResult
        }

        // Lấy danh sách favorite, nếu lỗi thì coi như rỗng (không chặn hiển thị sản phẩm)
        val favoritesResult = firebaseDataSource.getUserFavorites(userId)
        val favoriteIds = if (favoritesResult is NetworkResult.Success) favoritesResult.data else emptyList()

        val productsWithFavorites = productsResult.data.map { product ->
            product.copy(isFavorite = product.id in favoriteIds)
        }

        return NetworkResult.Success(productsWithFavorites)
    }

    suspend fun getProductByIdWithFavorite(productId: String, userId: String): NetworkResult<Product> {
        val productResult = firebaseDataSource.getProductById(productId)
        if (productResult !is NetworkResult.Success) return productResult

        val favoritesResult = firebaseDataSource.getUserFavorites(userId)
        val favoriteIds = if (favoritesResult is NetworkResult.Success) favoritesResult.data else emptyList()

        val product = productResult.data
        return NetworkResult.Success(product.copy(isFavorite = product.id in favoriteIds))
    }

    suspend fun getUserFavoriteProducts(userId: String): NetworkResult<List<Product>> {
        val favoritesResult = firebaseDataSource.getUserFavorites(userId)

        if (favoritesResult is NetworkResult.Success) {
            val favoriteIds = favoritesResult.data
            if (favoriteIds.isEmpty()) return NetworkResult.Success(emptyList())

            val productsResult = firebaseDataSource.getAllProducts()
            return if (productsResult is NetworkResult.Success) {
                val favoriteProducts = productsResult.data
                    .filter { it.id in favoriteIds }
                    .map { it.copy(isFavorite = true) }
                NetworkResult.Success(favoriteProducts)
            } else {
                productsResult // Trả về lỗi load sản phẩm
            }
        }

        // Trả về lỗi load favorite, hoặc lỗi chung
        return if (favoritesResult is NetworkResult.Error) favoritesResult
        else NetworkResult.Error("Lỗi không xác định")
    }

    suspend fun toggleFavorite(userId: String, productId: String, isFavorite: Boolean): NetworkResult<Unit> {
        return if (isFavorite) {
            firebaseDataSource.removeFromFavorites(userId, productId)
        } else {
            firebaseDataSource.addToFavorites(userId, productId)
        }
    }

    fun observeProductsWithFavorites(userId: String): Flow<NetworkResult<List<Product>>> {
        return firebaseDataSource.observeProducts().map { result ->
            if (result is NetworkResult.Success) {
                val favResult = firebaseDataSource.getUserFavorites(userId)
                val favIds = if (favResult is NetworkResult.Success) favResult.data else emptyList()

                val mapped = result.data.map { it.copy(isFavorite = it.id in favIds) }
                NetworkResult.Success(mapped)
            } else {
                result
            }
        }
    }

    // Delegate methods
    suspend fun addToFavorites(u: String, p: String) = firebaseDataSource.addToFavorites(u, p)
    suspend fun removeFromFavorites(u: String, p: String) = firebaseDataSource.removeFromFavorites(u, p)
    suspend fun getUserFavoriteIds(u: String) = firebaseDataSource.getUserFavorites(u)
    fun observeUserFavorites(u: String) = firebaseDataSource.observeUserFavorites(u)

    // Offline Mock Data
    fun getOfflineProducts(): List<Product> {
        return listOf(
            Product(id = "coffee_01", name = "Caffe Mocha", description = "Sự kết hợp hoàn hảo...", price = 45000.0, category = "Coffee", imageRes = R.drawable.mocha_latte_recipe),
            Product(id = "coffee_02", name = "Flat White", description = "Kiểu Úc...", price = 40000.0, category = "Coffee", imageRes = R.drawable.flat_white),
            Product(id = "tea_01", name = "Trà Đào Cam Sả", description = "Thanh mát...", price = 35000.0, category = "Tea", imageRes = R.drawable.peace_tea)
        )
    }
}