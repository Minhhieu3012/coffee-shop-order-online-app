package vn.edu.ut.hieupm9898.customermobile.data.repository

import kotlinx.coroutines.flow.Flow
import vn.edu.ut.hieupm9898.customermobile.data.local.CartDao
import vn.edu.ut.hieupm9898.customermobile.data.local.CartEntity

class CartRepository(private val cartDao: CartDao) {

    // 1. Lấy danh sách (cho màn hình Cart)
    val allCartItems: Flow<List<CartEntity>> = cartDao.getAllCartItems()

    // 2. Thêm vào giỏ (cho màn hình Detail)
    suspend fun addToCart(product: CartEntity) {
        // Kiểm tra xem món này đã có chưa
        val existingItem = cartDao.getCartItemById(product.productId)
        if (existingItem != null) {
            // Nếu có rồi thì tăng số lượng lên
            val updatedItem = existingItem.copy(quantity = existingItem.quantity + product.quantity)
            cartDao.updateCartItem(updatedItem)
        } else {
            // Chưa có thì thêm mới
            cartDao.insertCartItem(product)
        }
    }

    // 3. Tăng/Giảm số lượng (cho màn hình Cart/Checkout)
    suspend fun updateQuantity(cartItem: CartEntity, newQuantity: Int) {
        if (newQuantity > 0) {
            cartDao.updateCartItem(cartItem.copy(quantity = newQuantity))
        } else {
            // Nếu giảm về 0 thì xóa luôn
            cartDao.deleteCartItem(cartItem)
        }
    }

    // 4. Xóa món
    suspend fun removeFromCart(cartItem: CartEntity) {
        cartDao.deleteCartItem(cartItem)
    }

    // 5. Xóa hết (Sau khi thanh toán thành công)
    suspend fun clearCart() {
        cartDao.clearCart()
    }
}