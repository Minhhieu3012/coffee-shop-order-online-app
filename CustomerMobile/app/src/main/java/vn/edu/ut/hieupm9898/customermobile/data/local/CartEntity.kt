package vn.edu.ut.hieupm9898.customermobile.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart_items")
data class CartEntity(
    @PrimaryKey
    val productId: String, // Dùng ID sản phẩm làm khóa chính (mỗi món chỉ 1 dòng, nếu thêm nữa thì tăng số lượng)

    val name: String,
    val price: Double,
    val imageUrl: String,

    var quantity: Int = 1,
    val size: String = "Medium", // Ví dụ lưu size đã chọn
    val notes: String = ""       // Ví dụ lưu ghi chú (ít đường...)
) {
    // Hàm tính tổng tiền
    val totalPrice: Double
        get() = price * quantity
}