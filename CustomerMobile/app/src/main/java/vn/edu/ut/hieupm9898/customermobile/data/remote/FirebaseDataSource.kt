package vn.edu.ut.hieupm9898.customermobile.data.remote

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source
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
        private const val USERS_COLLECTION = "users"
        private const val FAVORITES_FIELD = "favorites"
        private const val TAG = "FirebaseDataSource"
    }

    /**
     * Lấy tất cả sản phẩm từ Firestore (One-time fetch)
     */
    suspend fun getAllProducts(): NetworkResult<List<Product>> {
        return try {
            val snapshot = firestore.collection(PRODUCTS_COLLECTION)
                .get()
                .await()

            val products = snapshot.documents.mapNotNull { doc ->
                try {
                    // ✅ Parse thủ công thay vì dùng toObject()
                    Product(
                        id = doc.id,
                        name = doc.getString("name") ?: "",
                        description = doc.getString("description") ?: "",
                        price = doc.getDouble("price") ?: 0.0,
                        imageUrl = doc.getString("imageUrl") ?: "",
                        category = doc.getString("category") ?: "",
                        isAvailable = doc.getBoolean("isAvailable") ?: true,  // ✅ Parse trực tiếp
                        discount = doc.getDouble("discount") ?: 0.0
                    )
                } catch (e: Exception) {
                    Log.e(TAG, "Error parsing product ${doc.id}: ${e.message}")
                    null
                }
            }

            Log.d(TAG, "✅ Loaded ${products.size} products from Firestore")
            NetworkResult.Success(products)
        } catch (e: Exception) {
            Log.e(TAG, "❌ Error loading products: ${e.message}", e)
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
            Log.e(TAG, "❌ Error loading product: ${e.message}", e)
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
     */
    fun observeProducts(): Flow<NetworkResult<List<Product>>> = callbackFlow {
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

        awaitClose { listener.remove() }
    }

    /**
     * Tìm kiếm sản phẩm theo tên
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

    // ============================================
    // FAVORITE METHODS - ✅ SỬA ĐỂ XỬ LÝ ARRAY ĐÚNG
    // ============================================

    /**
     * ✅ Lấy danh sách productId yêu thích của user
     * QUAN TRỌNG: Phải cast đúng kiểu List<String>
     */
    suspend fun getUserFavorites(userId: String): NetworkResult<List<String>> {
        return try {
            Log.d(TAG, "🔍 Getting favorites for user: $userId")

            val userDoc = firestore.collection(USERS_COLLECTION)
                .document(userId)
                .get()
                .await()

            if (!userDoc.exists()) {
                Log.w(TAG, "⚠️ User document not found, creating empty favorites")
                // Tạo document mới với favorites rỗng
                firestore.collection(USERS_COLLECTION)
                    .document(userId)
                    .set(mapOf(FAVORITES_FIELD to emptyList<String>()))
                    .await()
                return NetworkResult.Success(emptyList())
            }

            // ✅ CRITICAL: Phải cast đúng kiểu để tránh crash
            val favoritesData = userDoc.get(FAVORITES_FIELD)

            val favorites = when (favoritesData) {
                null -> {
                    Log.d(TAG, "📝 Favorites field is null, returning empty list")
                    emptyList()
                }
                is List<*> -> {
                    // Cast an toàn từ List<*> sang List<String>
                    favoritesData.filterIsInstance<String>()
                }
                else -> {
                    Log.e(TAG, "❌ Unexpected favorites type: ${favoritesData::class.java}")
                    emptyList()
                }
            }

            Log.d(TAG, "✅ Loaded ${favorites.size} favorites: $favorites")
            NetworkResult.Success(favorites)

        } catch (e: Exception) {
            Log.e(TAG, "❌ Error loading favorites: ${e.message}", e)
            NetworkResult.Error(e.message ?: "Lỗi tải danh sách yêu thích")
        }
    }

    /**
     * ✅ Thêm sản phẩm vào danh sách yêu thích
     */
    suspend fun addToFavorites(userId: String, productId: String): NetworkResult<Unit> {
        return try {
            Log.d(TAG, "➕ Adding product $productId to favorites for user $userId")

            val userRef = firestore.collection(USERS_COLLECTION).document(userId)
            val userDoc = userRef.get().await()

            if (!userDoc.exists()) {
                // Tạo document mới nếu chưa tồn tại
                userRef.set(mapOf(FAVORITES_FIELD to listOf(productId))).await()
                Log.d(TAG, "✅ Created new user document with favorite")
            } else {
                // Sử dụng FieldValue.arrayUnion để thêm không trùng lặp
                userRef.update(
                    FAVORITES_FIELD,
                    com.google.firebase.firestore.FieldValue.arrayUnion(productId)
                ).await()
                Log.d(TAG, "✅ Added to favorites successfully")
            }

            NetworkResult.Success(Unit)

        } catch (e: Exception) {
            Log.e(TAG, "❌ Error adding to favorites: ${e.message}", e)
            NetworkResult.Error(e.message ?: "Lỗi thêm vào yêu thích")
        }
    }

    /**
     * ✅ Xóa sản phẩm khỏi danh sách yêu thích
     */
    suspend fun removeFromFavorites(userId: String, productId: String): NetworkResult<Unit> {
        return try {
            Log.d(TAG, "➖ Removing product $productId from favorites for user $userId")

            val userRef = firestore.collection(USERS_COLLECTION).document(userId)

            userRef.update(
                FAVORITES_FIELD,
                com.google.firebase.firestore.FieldValue.arrayRemove(productId)
            ).await()

            Log.d(TAG, "✅ Removed from favorites successfully")
            NetworkResult.Success(Unit)

        } catch (e: Exception) {
            Log.e(TAG, "❌ Error removing from favorites: ${e.message}", e)
            NetworkResult.Error(e.message ?: "Lỗi xóa khỏi yêu thích")
        }
    }

    /**
     * Lắng nghe thay đổi danh sách favorite real-time
     */
    fun observeUserFavorites(userId: String): Flow<NetworkResult<List<String>>> = callbackFlow {
        trySend(NetworkResult.Loading)

        val listener = firestore.collection(USERS_COLLECTION)
            .document(userId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(NetworkResult.Error(error.message ?: "Lỗi lắng nghe favorites"))
                    return@addSnapshotListener
                }

                if (snapshot != null && snapshot.exists()) {
                    val favoritesData = snapshot.get(FAVORITES_FIELD)
                    val favorites = when (favoritesData) {
                        is List<*> -> favoritesData.filterIsInstance<String>()
                        else -> emptyList()
                    }
                    trySend(NetworkResult.Success(favorites))
                } else {
                    trySend(NetworkResult.Success(emptyList()))
                }
            }

        awaitClose { listener.remove() }
    }
}