package vn.edu.ut.hieupm9898.customermobile.features.cart

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import vn.edu.ut.hieupm9898.customermobile.data.local.CartEntity
import vn.edu.ut.hieupm9898.customermobile.data.model.*
import vn.edu.ut.hieupm9898.customermobile.data.repository.CartRepository
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val cartRepository: CartRepository,
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : ViewModel() {

    companion object {
        private const val TAG = "CartViewModel"
    }

    // ========== STATE FLOWS ==========
    val cartItems: StateFlow<List<CartEntity>> = cartRepository.cartItems
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val totalPrice: StateFlow<Double> = cartRepository.totalPrice
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0.0
        )

    val cartItemCount: StateFlow<Int> = cartRepository.cartItemCount
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0
        )

    // User info
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    init {
        loadUserInfo()
    }

    // ========== LOAD USER INFO ==========
    private fun loadUserInfo() {
        viewModelScope.launch {
            try {
                val userId = firebaseAuth.currentUser?.uid ?: return@launch
                val userDoc = firestore.collection("users")
                    .document(userId)
                    .get()
                    .await()

                _currentUser.value = userDoc.toObject(User::class.java)
                Log.d(TAG, "✅ User loaded: ${_currentUser.value?.displayName}")
            } catch (e: Exception) {
                Log.e(TAG, "❌ Error loading user info: ${e.message}", e)
            }
        }
    }

    // ========== CART OPERATIONS (KHỚP VỚI CARTSCREEN) ==========

    /**
     * ✅ Tăng số lượng
     */
    fun increaseQuantity(item: CartEntity) {
        viewModelScope.launch {
            val newQuantity = item.quantity + 1
            cartRepository.updateQuantity(item, newQuantity)
            Log.d(TAG, "✅ Increased: ${item.productName} -> $newQuantity")
        }
    }

    /**
     * ✅ Giảm số lượng (nếu = 0 thì xóa)
     */
    fun decreaseQuantity(item: CartEntity) {
        viewModelScope.launch {
            val newQuantity = item.quantity - 1
            if (newQuantity <= 0) {
                cartRepository.deleteCartItem(item)
                Log.d(TAG, "✅ Removed: ${item.productName}")
            } else {
                cartRepository.updateQuantity(item, newQuantity)
                Log.d(TAG, "✅ Decreased: ${item.productName} -> $newQuantity")
            }
        }
    }

    /**
     * ✅ Xóa item
     */
    fun removeItem(item: CartEntity) {
        viewModelScope.launch {
            cartRepository.deleteCartItem(item)
            Log.d(TAG, "✅ Deleted: ${item.productName}")
        }
    }

    /**
     * ✅ Xóa toàn bộ giỏ hàng
     */
    fun clearCart() {
        viewModelScope.launch {
            cartRepository.clearCart()
            Log.d(TAG, "✅ Cart cleared")
        }
    }

    // ========== TẠO ORDER TỪ GIỎ HÀNG (100% KHỚP MODEL) ==========
    suspend fun createOrderFromCart(): Result<Order> {
        return try {
            // 1. Kiểm tra user đăng nhập
            val userId = firebaseAuth.currentUser?.uid
                ?: return Result.failure(Exception("❌ Chưa đăng nhập"))

            val user = _currentUser.value
                ?: return Result.failure(Exception("❌ Không tìm thấy thông tin người dùng"))

            // 2. Kiểm tra giỏ hàng
            val items = cartItems.value
            if (items.isEmpty()) {
                return Result.failure(Exception("❌ Giỏ hàng trống"))
            }

            // 3. Chuyển đổi CartEntity -> OrderItem
            val orderItems = items.map { cartItem ->
                OrderItem(
                    productId = cartItem.productId,
                    productName = cartItem.productName,
                    productImage = cartItem.productImage,
                    price = cartItem.price,
                    quantity = cartItem.quantity,
                    size = cartItem.size,
                    dairy = "", // CartEntity không có dairy
                    notes = cartItem.notes
                )
            }

            // 4. Tính toán giá tiền
            val subtotal = totalPrice.value
            val deliveryFee = 15000.0
            val discount = 0.0
            val total = subtotal + deliveryFee - discount

            // 5. Lấy địa chỉ mặc định (nếu có)
            val defaultAddress = user.defaultAddress?.let { addr ->
                DeliveryAddress(
                    fullAddress = addr.fullAddress,
                    street = addr.street,
                    ward = addr.ward,
                    district = addr.district,
                    city = addr.city,
                    latitude = addr.latitude,
                    longitude = addr.longitude,
                    phoneNumber = user.phoneNumber,
                    recipientName = user.displayName
                )
            }

            // 6. Tạo Order object (KHỚP 100% VỚI MODEL)
            val order = Order(
                orderId = "", // Firebase sẽ tự gen ID
                userId = userId,
                userName = user.displayName,
                userPhone = user.phoneNumber,
                deliveryAddress = defaultAddress,
                deliveryType = DeliveryType.DELIVERY,
                items = orderItems,
                subtotal = subtotal,
                deliveryFee = deliveryFee,
                discount = discount,
                total = total,
                paymentMethod = PaymentMethod.BANK_TRANSFER, // QR = Chuyển khoản
                paymentStatus = PaymentStatus.PAID,
                status = OrderStatus.PENDING,
                note = "",
                createdAt = Timestamp.now(),
                updatedAt = Timestamp.now(),
                estimatedDeliveryTime = null,
                completedAt = null,
                driverInfo = null
            )

            // 7. Lưu lên Firebase Firestore
            val docRef = firestore.collection("orders")
                .add(order)
                .await()

            Log.d(TAG, "✅ Order created successfully with ID: ${docRef.id}")

            // 8. Trả về Order với ID đã được gán
            val savedOrder = order.copy(orderId = docRef.id)
            Result.success(savedOrder)

        } catch (e: Exception) {
            Log.e(TAG, "❌ Error creating order: ${e.message}", e)
            Result.failure(e)
        }
    }
}