package vn.edu.ut.hieupm9898.customermobile.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {
    // 1. Lấy toàn bộ giỏ hàng (Sắp xếp món mới nhất lên đầu)
    // Lưu ý: 'cart_table' phải trùng với tableName trong CartEntity
    @Query("SELECT * FROM cart_table ORDER BY id DESC")
    fun getAllCartItems(): Flow<List<CartEntity>>

    // 2. Lấy 1 món cụ thể (bạn có thể dùng để check trùng món sau này)
    @Query("SELECT * FROM cart_table WHERE productId = :id")
    suspend fun getCartItemById(id: String): CartEntity?

    // 3. Thêm món vào giỏ
    // Đổi tên thành 'addToCart' để khớp với CartRepository
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToCart(item: CartEntity)

    // 4. Cập nhật (ví dụ tăng giảm số lượng)
    @Update
    suspend fun updateCartItem(item: CartEntity)

    // 5. Xóa 1 món (Swipe to delete)
    @Delete
    suspend fun deleteCartItem(item: CartEntity)

    // 6. Xóa sạch giỏ hàng (khi đặt hàng thành công)
    @Query("DELETE FROM cart_table")
    suspend fun clearCart()
}