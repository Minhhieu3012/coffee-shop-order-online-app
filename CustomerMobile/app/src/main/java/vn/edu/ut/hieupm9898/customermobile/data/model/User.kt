package vn.edu.ut.hieupm9898.customermobile.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp

data class User(
    @DocumentId
    val uid: String = "", // Firebase Auth UID

    // Thông tin cơ bản
    val email: String = "",
    val displayName: String = "", // Tên hiển thị
    val phoneNumber: String = "",
    val dateOfBirth: String = "", // Format: DD/MM/YYYY
    val avatarUrl: String = "", // URL ảnh đại diện

    // Thông tin xác thực
    val authProvider: String = "email", // "email", "google", "facebook"
    val isEmailVerified: Boolean = false,
    val isPhoneVerified: Boolean = false,

    // Thông tin profile
    val isProfileCompleted: Boolean = false, // Đã tạo profile chưa?

    // Địa chỉ mặc định
    val defaultAddress: Address? = null,

    // Hệ thống điểm thưởng
    val loyaltyPoints: Int = 0, // Điểm tích lũy
    val totalOrders: Int = 0, // Tổng số đơn hàng
    val totalSpent: Double = 0.0, // Tổng tiền đã chi

    // Mã giới thiệu
    val referralCode: String = "", // Mã giới thiệu của user này
    val referredBy: String = "", // Được giới thiệu bởi ai (referralCode của người khác)

    // Thời gian
    @ServerTimestamp
    val createdAt: Timestamp? = null,

    @ServerTimestamp
    val updatedAt: Timestamp? = null,

    // Trạng thái tài khoản
    val isActive: Boolean = true,
    val isBanned: Boolean = false
)

// Model địa chỉ (nested object)
data class Address(
    val fullAddress: String = "",
    val street: String = "",
    val ward: String = "", // Phường/Xã
    val district: String = "", // Quận/Huyện
    val city: String = "", // Thành phố
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val isDefault: Boolean = true
)
