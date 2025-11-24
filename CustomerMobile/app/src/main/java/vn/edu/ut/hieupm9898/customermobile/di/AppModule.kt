package vn.edu.ut.hieupm9898.customermobile.di

import android.content.Context
import android.content.SharedPreferences
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
import vn.edu.ut.hieupm9898.customermobile.data.local.UserPreferencesManager
import vn.edu.ut.hieupm9898.customermobile.data.remote.FirebaseDataSource
import vn.edu.ut.hieupm9898.customermobile.data.repository.CartRepository
import vn.edu.ut.hieupm9898.customermobile.data.repository.OrderRepository
import vn.edu.ut.hieupm9898.customermobile.data.repository.ProductRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // =============================================
    // FIREBASE PROVIDERS
    // =============================================

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore {
        val firestore = FirebaseFirestore.getInstance()

        val settings = FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(true)
            .setCacheSizeBytes(FirebaseFirestoreSettings.CACHE_SIZE_UNLIMITED)
            .build()

        firestore.firestoreSettings = settings

        return firestore
    }

    @Provides
    @Singleton
    fun provideFirebaseStorage(): FirebaseStorage {
        return FirebaseStorage.getInstance()
    }

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

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "bros_coffee_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideCartDao(database: AppDatabase): CartDao {
        return database.cartDao()
    }

    // =============================================
    // REPOSITORY PROVIDERS (✅ UPDATED)
    // =============================================

    @Provides
    @Singleton
    fun provideProductRepository(
        firebaseDataSource: FirebaseDataSource
    ): ProductRepository {
        return ProductRepository(firebaseDataSource)
    }

    // ✅ CẬP NHẬT: Inject CartDao và FirebaseAuth vào CartRepository
    @Provides
    @Singleton
    fun provideCartRepository(
        cartDao: CartDao,
        firebaseAuth: FirebaseAuth
    ): CartRepository {
        return CartRepository(cartDao, firebaseAuth)
    }

    // =============================================
    // SHARED PREFERENCES & USER MANAGER
    // =============================================

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("bros_coffee_prefs", Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideUserPreferencesManager(sharedPreferences: SharedPreferences): UserPreferencesManager {
        return UserPreferencesManager(sharedPreferences)
    }

    @Provides
    @Singleton
    fun provideOrderRepository(
        firestore: FirebaseFirestore,
        firebaseAuth: FirebaseAuth
    ): OrderRepository {
        return OrderRepository(firestore, firebaseAuth)
    }
}