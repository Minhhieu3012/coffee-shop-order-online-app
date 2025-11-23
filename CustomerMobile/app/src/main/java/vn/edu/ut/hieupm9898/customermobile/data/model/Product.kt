package vn.edu.ut.hieupm9898.customermobile.data.model

import androidx.annotation.DrawableRes
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.Exclude

/**
 * Model sản phẩm - Đã cập nhật
 */
data class Product(
    @DocumentId
    val id: String = "",

    val name: String = "",
    val description: String = "",
    val price: Double = 0.0,
    val imageUrl: String = "",    // Link ảnh từ Server
    val category: String = "",    // "Coffee", "Tea", "Food"

    // ⚠️ QUAN TRỌNG: Thêm @get:Exclude để Firebase KHÔNG đọc/ghi trường này
    // Trường này chỉ dùng để hiển thị trạng thái tim đỏ/trắng trên UI
    @get:Exclude
    var isFavorite: Boolean = false,

    val isAvailable: Boolean = true,

    // ⚠️ QUAN TRỌNG: Thêm @get:Exclude vì đây là ID ảnh trong app (R.drawable...)
    @get:Exclude
    @DrawableRes val imageRes: Int? = null,

    val stock: Int = 0,
    val discount: Double = 0.0
) {
    fun getFormattedPrice(): String = "${price.toInt()}đ"

    fun hasDiscount(): Boolean = discount > 0.0

    fun getFinalPrice(): Double {
        return if (hasDiscount()) {
            price * (1 - discount / 100)
        } else {
            price
        }
    }

    fun hasRemoteImage(): Boolean = imageUrl.isNotEmpty()

    fun hasLocalImage(): Boolean = imageRes != null
}