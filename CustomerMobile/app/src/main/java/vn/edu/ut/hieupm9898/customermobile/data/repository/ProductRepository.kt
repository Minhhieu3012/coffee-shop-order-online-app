package vn.edu.ut.hieupm9898.customermobile.data.repository

import kotlinx.coroutines.flow.Flow
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

    /**
     * Lấy tất cả sản phẩm từ Firebase
     * Thay thế hàm getAllProducts() cũ (hardcode)
     */
    suspend fun getAllProducts(): NetworkResult<List<Product>> {
        return firebaseDataSource.getAllProducts()
    }

    /**
     * Lấy sản phẩm theo ID từ Firebase
     */
    suspend fun getProductById(productId: String): NetworkResult<Product> {
        return firebaseDataSource.getProductById(productId)
    }

    /**
     * Lấy sản phẩm theo category
     */
    suspend fun getProductsByCategory(category: String): NetworkResult<List<Product>> {
        return firebaseDataSource.getProductsByCategory(category)
    }

    /**
     * Lắng nghe thay đổi real-time (Flow)
     * Dùng khi muốn UI tự động cập nhật
     */
    fun observeProducts(): Flow<NetworkResult<List<Product>>> {
        return firebaseDataSource.observeProducts()
    }

    /**
     * Tìm kiếm sản phẩm
     */
    suspend fun searchProducts(query: String): NetworkResult<List<Product>> {
        return firebaseDataSource.searchProducts(query)
    }

    /**
     * HÀM DỰ PHÒNG: Lấy dữ liệu local (offline mode)
     * Dùng khi không có kết nối mạng
     */
    fun getOfflineProducts(): List<Product> {
        return listOf(
            Product(
                id = "coffee_01",
                name = "Caffe Mocha",
                description = "Sự kết hợp hoàn hảo giữa Espresso, sữa nóng và sốt sô cô la.",
                price = 45000.0,
                category = "Coffee",
                imageRes = R.drawable.mocha_latte_recipe
            ),
            Product(
                id = "coffee_02",
                name = "Flat White",
                description = "Cà phê sữa kiểu Úc với lớp bọt sữa mỏng mịn.",
                price = 40000.0,
                category = "Coffee",
                imageRes = R.drawable.flat_white
            ),
            Product(
                id = "tea_01",
                name = "Trà Đào Cam Sả",
                description = "Thanh mát, giải nhiệt ngày hè.",
                price = 35000.0,
                category = "Tea",
                imageRes = R.drawable.peace_tea
            )
        )
    }
}