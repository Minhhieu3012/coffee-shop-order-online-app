package vn.edu.ut.hieupm9898.customermobile.di

import android.content.Context
import androidx.room.Room // <-- Import quan trọng này phải có
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import vn.edu.ut.hieupm9898.customermobile.data.local.AppDatabase
import vn.edu.ut.hieupm9898.customermobile.data.local.CartDao
import vn.edu.ut.hieupm9898.customermobile.data.repository.AuthRepository
import vn.edu.ut.hieupm9898.customermobile.data.repository.CartRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // 🟢 ĐÃ SỬA: Khởi tạo Database trực tiếp bằng Room.databaseBuilder
    // Không gọi AppDatabase.getDatabase(context) nữa vì hàm đó đã xóa
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "bros_coffee_database" // Tên file DB
        )
            .fallbackToDestructiveMigration() // Reset DB nếu đổi version
            .build()
    }

    // Cung cấp DAO từ Database
    @Provides
    @Singleton
    fun provideCartDao(database: AppDatabase): CartDao {
        return database.cartDao()
    }

    // --- CÁC PHẦN KHÁC GIỮ NGUYÊN ---
/*
    @Provides
    @Singleton
    fun provideCartRepository(cartDao: CartDao): CartRepository {
        return CartRepository(cartDao)
    }
*/
    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return Firebase.auth
    }

    @Provides
    @Singleton
    fun provideAuthRepository(firebaseAuth: FirebaseAuth): AuthRepository {
        return AuthRepository(firebaseAuth)
    }
}