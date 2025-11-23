package vn.edu.ut.hieupm9898.customermobile.di

import android.content.Context
import androidx.room.Room
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import vn.edu.ut.hieupm9898.customermobile.data.local.AppDatabase
import vn.edu.ut.hieupm9898.customermobile.data.local.CartDao
import vn.edu.ut.hieupm9898.customermobile.data.remote.FirebaseDataSource
import vn.edu.ut.hieupm9898.customermobile.data.repository.CartRepository
import vn.edu.ut.hieupm9898.customermobile.data.repository.ProductRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // =============================================
    // FIREBASE PROVIDERS
    // =============================================

    /**
     * Provide Firebase Authentication instance
     */
    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    /**
     * Provide Firebase Firestore instance với cấu hình tối ưu
     */
    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore {
        val firestore = FirebaseFirestore.getInstance()

        // Cấu hình Firestore settings
        val settings = FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(true) // Bật cache offline
            .setCacheSizeBytes(FirebaseFirestoreSettings.CACHE_SIZE_UNLIMITED) // Cache không giới hạn
            .build()

        firestore.firestoreSettings = settings

        return firestore
    }

    /**
     * Provide Firebase Storage instance (dùng để upload/download ảnh)
     */
    @Provides
    @Singleton
    fun provideFirebaseStorage(): FirebaseStorage {
        return FirebaseStorage.getInstance()
    }

    /**
     * Provide FirebaseDataSource
     */
    @Provides
    @Singleton
    fun provideFirebaseDataSource(
        firestore: FirebaseFirestore
    ): FirebaseDataSource {
        return FirebaseDataSource(firestore)
    }

    // =============================================
    // ROOM DATABASE PROVIDERS
    // =============================================

    /**
     * Provide Room Database instance
     */
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "bros_coffee_database" // Tên database
        )
            .fallbackToDestructiveMigration() // Xóa DB cũ khi upgrade (chỉ dùng trong dev)
            .build()
    }

    /**
     * Provide CartDao
     */
    @Provides
    @Singleton
    fun provideCartDao(database: AppDatabase): CartDao {
        return database.cartDao()
    }

    // =============================================
    // REPOSITORY PROVIDERS
    // =============================================

    /**
     * Provide ProductRepository
     */
    @Provides
    @Singleton
    fun provideProductRepository(
        firebaseDataSource: FirebaseDataSource
    ): ProductRepository {
        return ProductRepository(firebaseDataSource)
    }

    /**
     * Provide CartRepository
     * TODO: Tích hợp với Room Database sau
     */
    @Provides
    @Singleton
    fun provideCartRepository(): CartRepository {
        return CartRepository()
    }

    // =============================================
    // SHARED PREFERENCES (Optional)
    // =============================================

    /**
     * Provide SharedPreferences để lưu settings, theme, language...
     */
    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context) =
        context.getSharedPreferences("bros_coffee_prefs", Context.MODE_PRIVATE)
}