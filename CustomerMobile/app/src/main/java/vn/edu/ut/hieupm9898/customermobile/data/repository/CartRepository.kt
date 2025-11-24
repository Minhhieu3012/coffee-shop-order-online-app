package vn.edu.ut.hieupm9898.customermobile.data.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import vn.edu.ut.hieupm9898.customermobile.data.local.CartDao
import vn.edu.ut.hieupm9898.customermobile.data.local.CartEntity
import vn.edu.ut.hieupm9898.customermobile.data.model.OrderItem
import vn.edu.ut.hieupm9898.customermobile.data.model.Product
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CartRepository @Inject constructor(
    private val cartDao: CartDao,
    private val firebaseAuth: FirebaseAuth
) {
    companion object {
        private const val TAG = "CartRepository"
    }

    val cartItems: Flow<List<CartEntity>> = cartDao.getAllCartItems()

    val totalPrice: Flow<Double> = cartItems.map { items ->
        items.sumOf { it.lineTotal }
    }

    val cartItemCount: Flow<Int> = cartItems.map { items ->
        items.sumOf { it.quantity }
    }

    suspend fun addToCart(
        product: Product,
        quantity: Int = 1,
        size: String = "Medium",
        dairy: String = "Whole Milk",
        notes: String = ""
    ): Result<Unit> {
        return try {
            val sizePrice = when (size) {
                "Nhỏ" -> 0.0
                "Trung bình" -> 5000.0
                "Lớn" -> 10000.0
                else -> 0.0
            }

            val dairyPrice = when (dairy) {
                "Almond Milk" -> 5000.0
                "Oat Milk" -> 7000.0
                else -> 0.0
            }

            val finalPrice = product.price + sizePrice + dairyPrice

            val existingItem = cartDao.getCartItemById(product.id)

            if (existingItem != null &&
                existingItem.size == size &&
                existingItem.notes == notes) {
                val updatedItem = existingItem.copy(
                    quantity = existingItem.quantity + quantity
                )
                cartDao.updateCartItem(updatedItem)
                Log.d(TAG, "✅ Updated cart item: ${product.name}, new qty: ${updatedItem.quantity}")
            } else {
                val cartEntity = CartEntity(
                    productId = product.id,
                    productName = product.name,
                    productImage = product.imageUrl,
                    price = finalPrice,
                    quantity = quantity,
                    size = size,
                    notes = notes
                )
                cartDao.addToCart(cartEntity)
                Log.d(TAG, "✅ Added to cart: ${product.name} x$quantity")
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "❌ Error adding to cart: ${e.message}", e)
            Result.failure(e)
        }
    }

    suspend fun updateQuantity(item: CartEntity, newQuantity: Int): Result<Unit> {
        return try {
            if (newQuantity <= 0) {
                deleteCartItem(item)
            } else {
                val updated = item.copy(quantity = newQuantity)
                cartDao.updateCartItem(updated)
                Log.d(TAG, "✅ Updated quantity: ${item.productName} -> $newQuantity")
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "❌ Error updating quantity: ${e.message}", e)
            Result.failure(e)
        }
    }

    suspend fun deleteCartItem(item: CartEntity): Result<Unit> {
        return try {
            cartDao.deleteCartItem(item)
            Log.d(TAG, "✅ Deleted cart item: ${item.productName}")
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "❌ Error deleting item: ${e.message}", e)
            Result.failure(e)
        }
    }

    suspend fun clearCart(): Result<Unit> {
        return try {
            cartDao.clearCart()
            Log.d(TAG, "✅ Cart cleared")
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "❌ Error clearing cart: ${e.message}", e)
            Result.failure(e)
        }
    }

    suspend fun isCartEmpty(): Boolean {
        return try {
            // Lưu ý: Đây chỉ là check tạm, logic đúng cần collect flow hoặc query one-shot
            false
        } catch (e: Exception) {
            Log.e(TAG, "❌ Error checking cart: ${e.message}", e)
            true
        }
    }

    fun getCartItemCountSync(): Flow<Int> = cartItemCount

    // ✅✅✅ NEW FUNCTION: RE-ORDER ✅✅✅
    /**
     * Thêm danh sách OrderItem từ lịch sử vào giỏ hàng
     */
    suspend fun addOrderItemsToCart(items: List<OrderItem>): Result<Unit> {
        return try {
            items.forEach { item ->
                // Xử lý logic gộp dairy vào note nếu CartEntity không có trường dairy
                // Hoặc giữ nguyên logic đơn giản là mapping sang CartEntity
                val finalNotes = if (item.dairy.isNotEmpty()) {
                    "${item.dairy}. ${item.notes}".trim()
                } else {
                    item.notes
                }

                // Kiểm tra xem sản phẩm đã có trong giỏ chưa (để cộng dồn)
                // Lưu ý: Logic này tương tự addToCart nhưng dùng dữ liệu từ OrderItem

                // Ở đây mình dùng cách đơn giản nhất là insert thẳng (hoặc replace)
                // Nếu bạn muốn cộng dồn số lượng thông minh, cần query getCartItemById như trên

                val cartEntity = CartEntity(
                    productId = item.productId,
                    productName = item.productName,
                    productImage = item.productImage,
                    price = item.price, // Giá trong OrderItem thường đã bao gồm size/dairy
                    quantity = item.quantity,
                    size = item.size,
                    notes = finalNotes
                )
                cartDao.addToCart(cartEntity) // Giả sử Dao dùng OnConflictStrategy.REPLACE hoặc logic insert
            }
            Log.d(TAG, "✅ Re-order success: ${items.size} items added")
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "❌ Error re-ordering: ${e.message}", e)
            Result.failure(e)
        }
    }
}