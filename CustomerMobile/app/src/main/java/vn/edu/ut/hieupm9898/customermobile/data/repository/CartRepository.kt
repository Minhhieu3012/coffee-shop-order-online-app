package vn.edu.ut.hieupm9898.customermobile.data.repository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import vn.edu.ut.hieupm9898.customermobile.data.model.CartItem
import vn.edu.ut.hieupm9898.customermobile.data.model.Product
import javax.inject.Inject
import javax.inject.Singleton

@Singleton // Giữ dữ liệu xuyên suốt App
class CartRepository @Inject constructor() {

    // Khởi tạo danh sách rỗng
    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems.asStateFlow()

    // Hàm thêm món (Đã sửa logic cập nhật UI)
    fun addToCart(product: Product, quantity: Int) {
        val currentList = _cartItems.value.toMutableList()

        // 1. Tìm vị trí (index) của món hàng thay vì tìm object
        val index = currentList.indexOfFirst { it.product.id == product.id }

        if (index != -1) {
            // 2. Nếu món đã có -> TẠO BẢN SAO MỚI (Copy) với số lượng mới
            // Lý do: Để Jetpack Compose nhận biết object đã thay đổi và vẽ lại số lượng
            val existingItem = currentList[index]
            currentList[index] = existingItem.copy(quantity = existingItem.quantity + quantity)
        } else {
            // 3. Nếu chưa có -> Thêm mới
            currentList.add(CartItem(product, quantity))
        }

        // Cập nhật StateFlow
        _cartItems.value = currentList
    }

    // Hàm xóa món
    fun removeFromCart(item: CartItem) {
        val currentList = _cartItems.value.toMutableList()
        currentList.remove(item)
        _cartItems.value = currentList
    }

    // Hàm xóa sạch giỏ (Logout / Thanh toán xong)
    fun clearCart() {
        _cartItems.value = emptyList()
    }

    // Tính tổng tiền (Optional helper)
    fun getTotalPrice(): Double {
        return _cartItems.value.sumOf { it.totalPrice }
    }
}