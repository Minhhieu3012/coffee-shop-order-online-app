package vn.edu.ut.hieupm9898.customermobile.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart_table") // 1. Tên bảng phải khớp với CartDao
data class CartEntity(
    // 2. Dùng ID tự tăng (Auto Generate).
    // Giúp lưu được nhiều dòng cùng sản phẩm nhưng khác size/topping
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val productId: String,       // ID sản phẩm gốc (để tham chiếu)
    val productName: String,     // Đổi 'name' -> 'productName' cho khớp ViewModel
    val productImage: String,    // Đổi 'imageUrl' -> 'productImage' cho khớp ViewModel
    val price: Double,           // Giá tiền (đã cộng size/topping)
    val quantity: Int,
    val size: String,
    val notes: String = ""       // Giữ lại ghi chú (ít đường/đá...), mặc định là rỗng
) {
    // (Optional) Hàm tiện ích tính tổng tiền hiển thị nếu cần
    // Lưu ý: Room sẽ bỏ qua hàm này, chỉ lưu các biến trong constructor
    val lineTotal: Double
        get() = price * quantity
}