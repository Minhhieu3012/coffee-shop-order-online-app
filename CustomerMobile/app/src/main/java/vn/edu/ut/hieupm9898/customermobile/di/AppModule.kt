package vn.edu.ut.hieupm9898.customermobile.di

import android.content.Context
import androidx.room.Room
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore // 👈 THÊM
import com.google.firebase.firestore.ktx.firestore // 👈 THÊM
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import vn.edu.ut.hieupm9898.customermobile.data.local.AppDatabase
import vn.edu.ut.hieupm9898.customermobile.data.local.CartDao
import vn.edu.ut.hieupm9898.customermobile.data.repository.AuthRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

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

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return Firebase.auth
    }

    // 👇 THÊM MỚI: Cung cấp Firestore
    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore {
        return Firebase.firestore
    }

    // 👇 CẬP NHẬT: Thêm Firestore vào AuthRepository
    @Provides
    @Singleton
    fun provideAuthRepository(
        firebaseAuth: FirebaseAuth,
        firestore: FirebaseFirestore // 👈 Thêm tham số này
    ): AuthRepository {
        return AuthRepository(firebaseAuth, firestore)
    }
}