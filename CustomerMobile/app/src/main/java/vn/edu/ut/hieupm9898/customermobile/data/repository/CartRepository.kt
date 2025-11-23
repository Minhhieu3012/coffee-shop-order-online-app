package vn.edu.ut.hieupm9898.customermobile.data.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import vn.edu.ut.hieupm9898.customermobile.data.local.CartDao
import vn.edu.ut.hieupm9898.customermobile.data.local.CartEntity
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

    /**
     * Lấy toàn bộ giỏ hàng từ Room (Flow để tự động update UI)
     */
    val cartItems: Flow<List<CartEntity>> = cartDao.getAllCartItems()

    /**
     * Tính tổng tiền
     */
    val totalPrice: Flow<Double> = cartItems.map { items ->
        items.sumOf { it.lineTotal }
    }

    /**
     * Đếm số lượng sản phẩm trong giỏ
     */
    val cartItemCount: Flow<Int> = cartItems.map { items ->
        items.sumOf { it.quantity }
    }

    /**
     * ✅ THÊM SẢN PHẨM VÀO GIỎ HÀNG
     * Logic: Nếu đã tồn tại cùng size/dairy -> cập nhật quantity
     * Nếu chưa có -> thêm mới
     */
    suspend fun addToCart(
        product: Product,
        quantity: Int = 1,
        size: String = "Medium",
        dairy: String = "Whole Milk",
        notes: String = ""
    ): Result<Unit> {
        return try {
            // Tính giá điều chỉnh theo size và dairy
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

            // Kiểm tra xem sản phẩm đã tồn tại với cùng config chưa
            val existingItem = cartDao.getCartItemById(product.id)

            if (existingItem != null &&
                existingItem.size == size &&
                existingItem.notes == notes) {
                // Cập nhật quantity
                val updatedItem = existingItem.copy(
                    quantity = existingItem.quantity + quantity
                )
                cartDao.updateCartItem(updatedItem)
                Log.d(TAG, "✅ Updated cart item: ${product.name}, new qty: ${updatedItem.quantity}")
            } else {
                // Thêm mới
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

    /**
     * CẬP NHẬT SỐ LƯỢNG
     */
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

    /**
     * XÓA MỘT ITEM
     */
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

    /**
     * XÓA TOÀN BỘ GIỎ HÀNG (Sau khi đặt hàng thành công)
     */
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

    /**
     * KIỂM TRA GIỎ HÀNG CÓ TRỐNG KHÔNG
     */
    suspend fun isCartEmpty(): Boolean {
        return try {
            val items = cartDao.getAllCartItems()
            // Chuyển Flow thành List để check
            false // Tạm thời return false, cần xử lý với Flow
        } catch (e: Exception) {
            Log.e(TAG, "❌ Error checking cart: ${e.message}", e)
            true
        }
    }

    /**
     * LẤY TỔNG SỐ LƯỢNG SẢN PHẨM (cho badge icon)
     */
    fun getCartItemCountSync(): Flow<Int> = cartItemCount
}