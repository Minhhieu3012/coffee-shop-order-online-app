package vn.edu.ut.hieupm9898.customermobile.features.product_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import vn.edu.ut.hieupm9898.customermobile.data.model.Product
import vn.edu.ut.hieupm9898.customermobile.data.repository.CartRepository
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val cartRepository: CartRepository
) : ViewModel() {

    /**
     * Hàm tính tổng tiền hiển thị tạm tính trên UI
     * (Dùng để update số tiền ngay khi user bấm tăng giảm số lượng)
     */
    fun calculateTotalPrice(basePrice: Double, quantity: Int, size: String): Double {
        val sizePrice = when (size) {
            "S" -> 0.0
            "M" -> 5000.0   // Size M cộng thêm 5.000đ
            "L" -> 10000.0  // Size L cộng thêm 10.000đ
            else -> 0.0
        }
        return (basePrice + sizePrice) * quantity
    }

    /**
     * Logic thêm món vào giỏ hàng
     * Đã sửa đổi để dùng CartRepository chung
     */
    fun addToCart(product: Product, quantity: Int, size: String) {
        viewModelScope.launch {
            // 1. Tính giá chênh lệch theo Size
            val priceAdjustment = when (size) {
                "M" -> 5000.0
                "L" -> 10000.0
                else -> 0.0
            }

            // 2. Tạo ra một bản sao của sản phẩm với giá và tên mới
            // Ví dụ: "Cà phê sữa" -> "Cà phê sữa (Size M)"
            // Điều này giúp giỏ hàng phân biệt được ly Size S và ly Size M
            val productToAdd = product.copy(
                id = "${product.id}_$size", // Tạo ID ảo để không bị trùng: "cafe123_M"
                name = "${product.name} (Size $size)",
                price = product.price + priceAdjustment
            )

            // 3. Gọi Repository để thêm vào danh sách chung
            // (Không cần CartEntity nữa, dùng trực tiếp Product)
            cartRepository.addToCart(productToAdd, quantity)
        }
    }
}