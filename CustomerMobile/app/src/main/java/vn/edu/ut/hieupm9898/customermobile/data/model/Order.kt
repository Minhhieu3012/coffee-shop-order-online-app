package vn.edu.ut.hieupm9898.customermobile.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp

/**
 * Model đơn hàng chính
 */
data class Order(
    @DocumentId
    val orderId: String = "",

    val userId: String = "",
    val userName: String = "",
    val userPhone: String = "",

    // Thông tin giao hàng
    val deliveryAddress: DeliveryAddress? = null,
    val deliveryType: DeliveryType = DeliveryType.DELIVERY,

    // Danh sách sản phẩm
    val items: List<OrderItem> = emptyList(),

    // Thanh toán
    val subtotal: Double = 0.0,
    val deliveryFee: Double = 0.0,
    val discount: Double = 0.0,
    val total: Double = 0.0,

    val paymentMethod: PaymentMethod = PaymentMethod.CASH,
    val paymentStatus: PaymentStatus = PaymentStatus.PENDING,

    // Trạng thái đơn hàng
    val status: OrderStatus = OrderStatus.PENDING,

    // Ghi chú
    val note: String = "",

    // Thời gian
    @ServerTimestamp
    val createdAt: Timestamp? = null,

    @ServerTimestamp
    val updatedAt: Timestamp? = null,

    val estimatedDeliveryTime: Timestamp? = null,
    val completedAt: Timestamp? = null,

    // Tracking
    val driverInfo: DriverInfo? = null
)

/**
 * Sản phẩm trong đơn hàng
 */
data class OrderItem(
    val productId: String = "",
    val productName: String = "",
    val productImage: String = "",
    val price: Double = 0.0,
    val quantity: Int = 1,
    val size: String = "",
    val dairy: String = "",
    val notes: String = ""
) {
    val lineTotal: Double
        get() = price * quantity
}

/**
 * Địa chỉ giao hàng
 */
data class DeliveryAddress(
    val fullAddress: String = "",
    val street: String = "",
    val ward: String = "",
    val district: String = "",
    val city: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val phoneNumber: String = "",
    val recipientName: String = ""
)

/**
 * Thông tin tài xế
 */
data class DriverInfo(
    val driverId: String = "",
    val driverName: String = "",
    val driverPhone: String = "",
    val driverPhoto: String = "",
    val vehicleNumber: String = ""
)

/**
 * Trạng thái đơn hàng
 */
enum class OrderStatus {
    PENDING,        // Chờ xác nhận
    CONFIRMED,      // Đã xác nhận
    PREPARING,      // Đang chuẩn bị
    READY,          // Sẵn sàng giao
    DELIVERING,     // Đang giao
    COMPLETED,      // Hoàn thành
    CANCELLED,      // Đã hủy
    FAILED;         // Thất bại

    fun getDisplayName(): String = when(this) {
        PENDING -> "Chờ xác nhận"
        CONFIRMED -> "Đã xác nhận"
        PREPARING -> "Đang chuẩn bị"
        READY -> "Sẵn sàng giao"
        DELIVERING -> "Đang giao hàng"
        COMPLETED -> "Hoàn thành"
        CANCELLED -> "Đã hủy"
        FAILED -> "Thất bại"
    }
}

/**
 * Loại giao hàng
 */
enum class DeliveryType {
    DELIVERY,   // Giao hàng
    PICKUP;     // Nhận tại cửa hàng

    fun getDisplayName(): String = when(this) {
        DELIVERY -> "Giao hàng"
        PICKUP -> "Nhận tại quầy"
    }
}

/**
 * Phương thức thanh toán
 */
enum class PaymentMethod {
    CASH,           // Tiền mặt
    BANK_TRANSFER,  // Chuyển khoản
    MOMO,           // Ví MoMo
    ZALOPAY,        // ZaloPay
    CREDIT_CARD;    // Thẻ tín dụng

    fun getDisplayName(): String = when(this) {
        CASH -> "Tiền mặt"
        BANK_TRANSFER -> "Chuyển khoản"
        MOMO -> "Ví MoMo"
        ZALOPAY -> "ZaloPay"
        CREDIT_CARD -> "Thẻ tín dụng"
    }
}

/**
 * Trạng thái thanh toán
 */
enum class PaymentStatus {
    PENDING,    // Chờ thanh toán
    PAID,       // Đã thanh toán
    FAILED,     // Thanh toán thất bại
    REFUNDED;   // Đã hoàn tiền

    fun getDisplayName(): String = when(this) {
        PENDING -> "Chờ thanh toán"
        PAID -> "Đã thanh toán"
        FAILED -> "Thất bại"
        REFUNDED -> "Đã hoàn tiền"
    }
}