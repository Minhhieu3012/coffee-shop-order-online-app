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
    // Lấy toàn bộ giỏ hàng (trả về Flow để tự động cập nhật UI khi data đổi)
    @Query("SELECT * FROM cart_items")
    fun getAllCartItems(): Flow<List<CartEntity>>

    // Lấy 1 món cụ thể (để kiểm tra xem đã có trong giỏ chưa)
    @Query("SELECT * FROM cart_items WHERE productId = :id")
    suspend fun getCartItemById(id: String): CartEntity?

    // Thêm món vào giỏ (Nếu trùng ID thì thay thế/update)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCartItem(item: CartEntity)

    // Cập nhật (ví dụ tăng giảm số lượng)
    @Update
    suspend fun updateCartItem(item: CartEntity)

    // Xóa 1 món
    @Delete
    suspend fun deleteCartItem(item: CartEntity)

    // Xóa sạch giỏ hàng (khi đặt hàng xong)
    @Query("DELETE FROM cart_items")
    suspend fun clearCart()
}