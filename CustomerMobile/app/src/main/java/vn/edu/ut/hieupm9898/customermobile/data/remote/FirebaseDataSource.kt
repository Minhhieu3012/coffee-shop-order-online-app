package vn.edu.ut.hieupm9898.customermobile.data.remote

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import vn.edu.ut.hieupm9898.customermobile.data.model.Product
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseDataSource @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    companion object {
        private const val PRODUCTS_COLLECTION = "products"
    }

    /**
     * Lấy tất cả sản phẩm từ Firestore (One-time fetch)
     * Dùng suspend function với await() để lấy data một lần
     */
    suspend fun getAllProducts(): NetworkResult<List<Product>> {
        return try {
            val snapshot = firestore.collection(PRODUCTS_COLLECTION)
                .get()
                .await()

            val products = snapshot.documents.mapNotNull { doc ->
                doc.toObject(Product::class.java)?.copy(id = doc.id)
            }

            NetworkResult.Success(products)
        } catch (e: Exception) {
            NetworkResult.Error(e.message ?: "Lỗi tải dữ liệu sản phẩm")
        }
    }

    /**
     * Lấy sản phẩm theo ID
     */
    suspend fun getProductById(productId: String): NetworkResult<Product> {
        return try {
            val snapshot = firestore.collection(PRODUCTS_COLLECTION)
                .document(productId)
                .get()
                .await()

            val product = snapshot.toObject(Product::class.java)?.copy(id = snapshot.id)

            if (product != null) {
                NetworkResult.Success(product)
            } else {
                NetworkResult.Error("Không tìm thấy sản phẩm")
            }
        } catch (e: Exception) {
            NetworkResult.Error(e.message ?: "Lỗi tải sản phẩm")
        }
    }

    /**
     * Lấy sản phẩm theo category
     */
    suspend fun getProductsByCategory(category: String): NetworkResult<List<Product>> {
        return try {
            val snapshot = firestore.collection(PRODUCTS_COLLECTION)
                .whereEqualTo("category", category)
                .get()
                .await()

            val products = snapshot.documents.mapNotNull { doc ->
                doc.toObject(Product::class.java)?.copy(id = doc.id)
            }

            NetworkResult.Success(products)
        } catch (e: Exception) {
            NetworkResult.Error(e.message ?: "Lỗi tải sản phẩm theo danh mục")
        }
    }

    /**
     * Lắng nghe thay đổi real-time từ Firestore (Flow)
     * Dùng khi muốn UI tự động update khi admin thêm/sửa/xóa sản phẩm
     */
    fun observeProducts(): Flow<NetworkResult<List<Product>>> = callbackFlow {
        // Gửi Loading state ngay lập tức
        trySend(NetworkResult.Loading)

        val listener = firestore.collection(PRODUCTS_COLLECTION)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(NetworkResult.Error(error.message ?: "Lỗi lắng nghe dữ liệu"))
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val products = snapshot.documents.mapNotNull { doc ->
                        doc.toObject(Product::class.java)?.copy(id = doc.id)
                    }
                    trySend(NetworkResult.Success(products))
                }
            }

        // Cleanup khi Flow bị hủy
        awaitClose { listener.remove() }
    }

    /**
     * Tìm kiếm sản phẩm theo tên (case-insensitive)
     * Lưu ý: Firestore không hỗ trợ LIKE query tốt,
     * nên ta lấy hết về rồi filter ở client
     */
    suspend fun searchProducts(query: String): NetworkResult<List<Product>> {
        return try {
            val allProductsResult = getAllProducts()

            if (allProductsResult is NetworkResult.Success) {
                val filteredProducts = allProductsResult.data.filter { product ->
                    product.name.contains(query, ignoreCase = true) ||
                            product.description.contains(query, ignoreCase = true)
                }
                NetworkResult.Success(filteredProducts)
            } else {
                allProductsResult as NetworkResult.Error
            }
        } catch (e: Exception) {
            NetworkResult.Error(e.message ?: "Lỗi tìm kiếm sản phẩm")
        }
    }
}