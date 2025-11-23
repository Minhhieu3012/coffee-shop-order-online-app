package vn.edu.ut.hieupm9898.customermobile.features.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import vn.edu.ut.hieupm9898.customermobile.data.local.CartEntity
import vn.edu.ut.hieupm9898.customermobile.data.repository.CartRepository
import javax.inject.Inject

/**
 * UI State cho Cart Screen
 */
data class CartUiState(
    val items: List<CartEntity> = emptyList(),
    val totalPrice: Double = 0.0,
    val itemCount: Int = 0,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class CartViewModel @Inject constructor(
    private val cartRepository: CartRepository
) : ViewModel() {

    // ✅ Lấy trực tiếp từ Repository (Flow tự động update UI)
    val cartItems: StateFlow<List<CartEntity>> = cartRepository.cartItems
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // ✅ Tự động tính tổng tiền khi giỏ hàng thay đổi
    val totalPrice: StateFlow<Double> = cartRepository.totalPrice
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0.0
        )

    // ✅ Đếm số lượng item
    val itemCount: StateFlow<Int> = cartRepository.cartItemCount
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0
        )

    // ✅ UI State tổng hợp
    val uiState: StateFlow<CartUiState> = cartItems.map { items ->
        CartUiState(
            items = items,
            totalPrice = items.sumOf { it.lineTotal },
            itemCount = items.sumOf { it.quantity }
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = CartUiState()
    )

    /**
     * XÓA MỘT ITEM
     */
    fun removeItem(item: CartEntity) {
        viewModelScope.launch {
            cartRepository.deleteCartItem(item)
        }
    }

    /**
     * TĂNG SỐ LƯỢNG
     */
    fun increaseQuantity(item: CartEntity) {
        viewModelScope.launch {
            cartRepository.updateQuantity(item, item.quantity + 1)
        }
    }

    /**
     * GIẢM SỐ LƯỢNG
     */
    fun decreaseQuantity(item: CartEntity) {
        viewModelScope.launch {
            if (item.quantity > 1) {
                cartRepository.updateQuantity(item, item.quantity - 1)
            } else {
                // Nếu quantity = 1 -> Xóa item
                removeItem(item)
            }
        }
    }

    /**
     * XÓA TOÀN BỘ GIỎ HÀNG
     */
    fun clearCart() {
        viewModelScope.launch {
            cartRepository.clearCart()
        }
    }
}