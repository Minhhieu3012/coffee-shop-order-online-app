package vn.edu.ut.hieupm9898.customermobile.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [CartEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    // Cung cấp DAO để bên ngoài sử dụng
    abstract fun cartDao(): CartDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        // Hàm lấy Database (Singleton - chỉ tạo 1 lần duy nhất)
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "bros_coffee_database" // Tên file DB trong máy
                )
                    .fallbackToDestructiveMigration() // Xóa data cũ nếu đổi cấu trúc bảng (chỉ dùng cho dev)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}