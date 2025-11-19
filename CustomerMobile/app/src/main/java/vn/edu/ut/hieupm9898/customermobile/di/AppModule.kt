package vn.edu.ut.hieupm9898.customermobile.di

import android.content.Context
// --- CÁC IMPORT MỚI CẦN THÊM ---
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import vn.edu.ut.hieupm9898.customermobile.data.repository.AuthRepository
// --------------------------------
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import vn.edu.ut.hieupm9898.customermobile.data.local.AppDatabase
import vn.edu.ut.hieupm9898.customermobile.data.local.CartDao
import vn.edu.ut.hieupm9898.customermobile.data.repository.CartRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // ... (Giữ nguyên các hàm provide Database/Cart cũ của bạn) ...

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getDatabase(context)
    }

    @Provides
    @Singleton
    fun provideCartDao(database: AppDatabase): CartDao {
        return database.cartDao()
    }

    @Provides
    @Singleton
    fun provideCartRepository(cartDao: CartDao): CartRepository {
        return CartRepository(cartDao)
    }

    // --- THÊM PHẦN NÀY ĐỂ SỬA LỖI AUTH ---

    // 1. Dạy Hilt cách lấy FirebaseAuth từ Firebase SDK
    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return Firebase.auth
    }

    // 2. Dạy Hilt cách tạo AuthRepository (nó sẽ tự lấy firebaseAuth ở trên bỏ vào đây)
    @Provides
    @Singleton
    fun provideAuthRepository(firebaseAuth: FirebaseAuth): AuthRepository {
        return AuthRepository(firebaseAuth)
    }
}