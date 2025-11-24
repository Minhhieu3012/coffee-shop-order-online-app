package vn.edu.ut.hieupm9898.customermobile.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.PropertyName
import com.google.firebase.firestore.ServerTimestamp

/**
 * Model đơn hàng - KHỚP 100% VỚI FIRESTORE
 */
data class Order(
    @DocumentId
    @get:PropertyName("orderId")
    @set:PropertyName("orderId")
    var orderId: String = "",

    @get:PropertyName("userId")
    @set:PropertyName("userId")
    var userId: String = "",

    @get:PropertyName("userName")
    @set:PropertyName("userName")
    var userName: String = "",

    @get:PropertyName("userPhone")
    @set:PropertyName("userPhone")
    var userPhone: String = "",

    // Thông tin giao hàng
    @get:PropertyName("deliveryAddress")
    @set:PropertyName("deliveryAddress")
    var deliveryAddress: DeliveryAddress? = null,

    @get:PropertyName("deliveryType")
    @set:PropertyName("deliveryType")
    var deliveryType: String = "DELIVERY", // "DELIVERY" hoặc "PICKUP"

    // Danh sách sản phẩm
    @get:PropertyName("items")
    @set:PropertyName("items")
    var items: List<OrderItem> = emptyList(),

    // Thanh toán
    @get:PropertyName("subtotal")
    @set:PropertyName("subtotal")
    var subtotal: Double = 0.0,

    @get:PropertyName("deliveryFee")
    @set:PropertyName("deliveryFee")
    var deliveryFee: Double = 0.0,

    @get:PropertyName("discount")
    @set:PropertyName("discount")
    var discount: Double = 0.0,

    @get:PropertyName("total")
    @set:PropertyName("total")
    var total: Double = 0.0,

    @get:PropertyName("paymentMethod")
    @set:PropertyName("paymentMethod")
    var paymentMethod: String = "CASH", // "CASH", "BANK_TRANSFER", "MOMO"

    @get:PropertyName("paymentStatus")
    @set:PropertyName("paymentStatus")
    var paymentStatus: String = "PENDING", // "PENDING", "PAID", "FAILED"

    // Trạng thái đơn hàng
    @get:PropertyName("status")
    @set:PropertyName("status")
    var status: String = "PENDING", // "PENDING", "CONFIRMED", "CANCELLED", etc.

    // Ghi chú
    @get:PropertyName("note")
    @set:PropertyName("note")
    var note: String = "",

    // Thời gian
    @ServerTimestamp
    @get:PropertyName("createdAt")
    @set:PropertyName("createdAt")
    var createdAt: Timestamp? = null,

    @ServerTimestamp
    @get:PropertyName("updatedAt")
    @set:PropertyName("updatedAt")
    var updatedAt: Timestamp? = null,

    @get:PropertyName("estimatedDeliveryTime")
    @set:PropertyName("estimatedDeliveryTime")
    var estimatedDeliveryTime: Timestamp? = null,

    @get:PropertyName("completedAt")
    @set:PropertyName("completedAt")
    var completedAt: Timestamp? = null,

    // Tracking
    @get:PropertyName("driverInfo")
    @set:PropertyName("driverInfo")
    var driverInfo: DriverInfo? = null
) {
    // ✅ FIXED: Đổi tên để tránh conflict với auto-generated getters

    /**
     * Convert status String to OrderStatus enum
     */
    fun getStatusEnum(): OrderStatus {
        return try {
            OrderStatus.valueOf(status)
        } catch (e: Exception) {
            OrderStatus.PENDING
        }
    }

    /**
     * Convert paymentMethod String to PaymentMethod enum
     */
    fun getPaymentMethodEnum(): PaymentMethod {
        return try {
            PaymentMethod.valueOf(paymentMethod)
        } catch (e: Exception) {
            PaymentMethod.CASH
        }
    }

    /**
     * Convert paymentStatus String to PaymentStatus enum
     */
    fun getPaymentStatusEnum(): PaymentStatus {
        return try {
            PaymentStatus.valueOf(paymentStatus)
        } catch (e: Exception) {
            PaymentStatus.PENDING
        }
    }

    /**
     * Convert deliveryType String to DeliveryType enum
     */
    fun getDeliveryTypeEnum(): DeliveryType {
        return try {
            DeliveryType.valueOf(deliveryType)
        } catch (e: Exception) {
            DeliveryType.DELIVERY
        }
    }
}

/**
 * Sản phẩm trong đơn hàng - KHỚP FIRESTORE
 */
data class OrderItem(
    @get:PropertyName("productId")
    @set:PropertyName("productId")
    var productId: String = "",

    @get:PropertyName("productName")
    @set:PropertyName("productName")
    var productName: String = "",

    @get:PropertyName("productImage")
    @set:PropertyName("productImage")
    var productImage: String = "",

    @get:PropertyName("price")
    @set:PropertyName("price")
    var price: Double = 0.0,

    @get:PropertyName("quantity")
    @set:PropertyName("quantity")
    var quantity: Int = 1,

    @get:PropertyName("size")
    @set:PropertyName("size")
    var size: String = "",

    @get:PropertyName("dairy")
    @set:PropertyName("dairy")
    var dairy: String = "",

    @get:PropertyName("notes")
    @set:PropertyName("notes")
    var notes: String = "",

    @get:PropertyName("lineTotal")
    @set:PropertyName("lineTotal")
    var lineTotal: Double = 0.0
)

/**
 * Địa chỉ giao hàng
 */
data class DeliveryAddress(
    @get:PropertyName("fullAddress")
    @set:PropertyName("fullAddress")
    var fullAddress: String = "",

    @get:PropertyName("street")
    @set:PropertyName("street")
    var street: String = "",

    @get:PropertyName("ward")
    @set:PropertyName("ward")
    var ward: String = "",

    @get:PropertyName("district")
    @set:PropertyName("district")
    var district: String = "",

    @get:PropertyName("city")
    @set:PropertyName("city")
    var city: String = "",

    @get:PropertyName("latitude")
    @set:PropertyName("latitude")
    var latitude: Double = 0.0,

    @get:PropertyName("longitude")
    @set:PropertyName("longitude")
    var longitude: Double = 0.0,

    @get:PropertyName("phoneNumber")
    @set:PropertyName("phoneNumber")
    var phoneNumber: String = "",

    @get:PropertyName("recipientName")
    @set:PropertyName("recipientName")
    var recipientName: String = ""
)

/**
 * Thông tin tài xế
 */
data class DriverInfo(
    @get:PropertyName("driverId")
    @set:PropertyName("driverId")
    var driverId: String = "",

    @get:PropertyName("driverName")
    @set:PropertyName("driverName")
    var driverName: String = "",

    @get:PropertyName("driverPhone")
    @set:PropertyName("driverPhone")
    var driverPhone: String = "",

    @get:PropertyName("driverPhoto")
    @set:PropertyName("driverPhoto")
    var driverPhoto: String = "",

    @get:PropertyName("vehicleNumber")
    @set:PropertyName("vehicleNumber")
    var vehicleNumber: String = ""
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