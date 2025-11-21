package vn.edu.ut.hieupm9898.customermobile.features.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import vn.edu.ut.hieupm9898.customermobile.data.model.CartItem
import vn.edu.ut.hieupm9898.customermobile.data.repository.CartRepository
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val cartRepository: CartRepository
) : ViewModel() {

    // Lấy danh sách trực tiếp từ Repository
    val cartItems: StateFlow<List<CartItem>> = cartRepository.cartItems

    // Tự động tính tổng tiền bất cứ khi nào giỏ hàng thay đổi
    val totalPrice: StateFlow<Double> = cartItems.map { items ->
        items.sumOf { it.totalPrice }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    fun removeItem(item: CartItem) {
        cartRepository.removeFromCart(item)
    }
}