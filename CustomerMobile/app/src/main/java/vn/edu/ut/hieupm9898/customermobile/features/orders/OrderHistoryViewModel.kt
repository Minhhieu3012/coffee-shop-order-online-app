package vn.edu.ut.hieupm9898.customermobile.features.orders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import vn.edu.ut.hieupm9898.customermobile.data.model.Order
import vn.edu.ut.hieupm9898.customermobile.data.repository.CartRepository
import vn.edu.ut.hieupm9898.customermobile.data.repository.OrderRepository
import javax.inject.Inject

data class OrderHistoryUiState(
    val ongoingOrders: List<Order> = emptyList(),
    val historyOrders: List<Order> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val selectedTab: Int = 0
)

// ✅ Sự kiện điều hướng
sealed class OrderHistoryEvent {
    object NavigateToCart : OrderHistoryEvent()
}

@HiltViewModel
class OrderHistoryViewModel @Inject constructor(
    private val orderRepository: OrderRepository,
    private val cartRepository: CartRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(OrderHistoryUiState())
    val uiState: StateFlow<OrderHistoryUiState> = _uiState.asStateFlow()

    // ✅ Channel để gửi sự kiện điều hướng (chỉ nhận 1 lần)
    private val _navigationChannel = Channel<OrderHistoryEvent>()
    val navigationChannel = _navigationChannel.receiveAsFlow()

    init {
        loadOrders()
    }

    fun loadOrders() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            val ongoingResult = orderRepository.getOngoingOrders()
            val ongoing = ongoingResult.getOrNull() ?: emptyList()

            val historyResult = orderRepository.getOrderHistory()
            val history = historyResult.getOrNull() ?: emptyList()

            val error = when {
                ongoingResult.isFailure -> ongoingResult.exceptionOrNull()?.message
                historyResult.isFailure -> historyResult.exceptionOrNull()?.message
                else -> null
            }

            _uiState.update {
                it.copy(
                    ongoingOrders = ongoing,
                    historyOrders = history,
                    isLoading = false,
                    errorMessage = error
                )
            }
        }
    }

    fun selectTab(tabIndex: Int) {
        _uiState.update { it.copy(selectedTab = tabIndex) }
    }

    fun cancelOrder(orderId: String) {
        viewModelScope.launch {
            val result = orderRepository.cancelOrder(orderId)
            if (result.isSuccess) {
                loadOrders()
            } else {
                _uiState.update { it.copy(errorMessage = result.exceptionOrNull()?.message) }
            }
        }
    }

    // ✅ IMPLEMENTED RE-ORDER FUNCTION
    fun reorderOrder(order: Order) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            // 1. Thêm items vào giỏ hàng
            val result = cartRepository.addOrderItemsToCart(order.items)

            if (result.isSuccess) {
                // 2. Nếu thành công, gửi sự kiện điều hướng
                _uiState.update { it.copy(isLoading = false) }
                _navigationChannel.send(OrderHistoryEvent.NavigateToCart)
            } else {
                // 3. Nếu thất bại, hiện lỗi
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Không thể đặt lại đơn hàng: ${result.exceptionOrNull()?.message}"
                    )
                }
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}