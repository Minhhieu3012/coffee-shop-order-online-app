package vn.edu.ut.hieupm9898.customermobile.features.orders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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
    val selectedTab: Int = 0 // 0: Ongoing, 1: History
)

@HiltViewModel
class OrderHistoryViewModel @Inject constructor(
    private val orderRepository: OrderRepository,
    private val cartRepository: CartRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(OrderHistoryUiState())
    val uiState: StateFlow<OrderHistoryUiState> = _uiState.asStateFlow()


    init {
        loadOrders()
    }

    /**
     * Load tất cả đơn hàng
     */
    fun loadOrders() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            // Load ongoing orders
            val ongoingResult = orderRepository.getOngoingOrders()
            val ongoing = ongoingResult.getOrNull() ?: emptyList()

            // Load history orders
            val historyResult = orderRepository.getOrderHistory()
            val history = historyResult.getOrNull() ?: emptyList()

            // Check for errors
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

    /**
     * Switch tab
     */
    fun selectTab(tabIndex: Int) {
        _uiState.update { it.copy(selectedTab = tabIndex) }
    }

    /**
     * Hủy đơn hàng
     */
    fun cancelOrder(orderId: String) {
        viewModelScope.launch {
            val result = orderRepository.cancelOrder(orderId)

            if (result.isSuccess) {
                // Reload orders after cancellation
                loadOrders()
            } else {
                _uiState.update {
                    it.copy(errorMessage = result.exceptionOrNull()?.message)
                }
            }
        }
    }

    /**
     * Re-order (TODO: implement)
     */
    fun reorderOrder(order: Order) {
        // TODO: Navigate to cart with order items
        android.util.Log.d("OrderHistory", "Re-ordering: ${order.orderId}")
    }

    /**
     * Clear error
     */
    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    /**
     * Observe orders real-time (optional)
     */
    fun observeOrders() {
        viewModelScope.launch {
            orderRepository.observeUserOrders().collect { result ->
                result.fold(
                    onSuccess = { orders ->
                        val ongoingStatuses = listOf(
                            "PENDING", "CONFIRMED", "PREPARING", "READY", "DELIVERING"
                        )

                        val historyStatuses = listOf(
                            "COMPLETED", "CANCELLED", "FAILED"
                        )

                        val ongoing = orders.filter { it.status in ongoingStatuses }
                        val history = orders.filter { it.status in historyStatuses }

                        _uiState.update {
                            it.copy(
                                ongoingOrders = ongoing,
                                historyOrders = history,
                                isLoading = false
                            )
                        }
                    },
                    onFailure = { error ->
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = error.message
                            )
                        }
                    }
                )
            }
        }
    }
}