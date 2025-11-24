package vn.edu.ut.hieupm9898.customermobile.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

// Khai báo các bảng (entities) và version DB
@Database(entities = [CartEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    // Chỉ cần khai báo hàm abstract trả về DAO
    // Không cần companion object tạo Instance nữa (AppModule đã lo việc này)
    abstract fun cartDao(): CartDao
}