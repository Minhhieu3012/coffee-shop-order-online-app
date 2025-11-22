package vn.edu.ut.hieupm9898.customermobile.data.repository

import vn.edu.ut.hieupm9898.customermobile.data.model.UserProfile
import javax.inject.Inject
import kotlinx.coroutines.delay

interface ProfileRepository {
    suspend fun loadUserProfile(): UserProfile

    // [FIXED] Chỉ còn 3 tham số
    suspend fun saveUserProfile(name: String, phone: String, email: String): Boolean
}

class ProfileRepositoryImpl @Inject constructor() : ProfileRepository {

    private var currentUserData = UserProfile(
        name = "Khách hàng PM",
        phone = "0901234567",
        email = "customer@ut.edu.vn"
    )

    override suspend fun loadUserProfile(): UserProfile {
        delay(500)
        return currentUserData
    }

    // [FIXED] Implementation chỉ sử dụng 3 tham số
    override suspend fun saveUserProfile(name: String, phone: String, email: String): Boolean {
        delay(700)
        currentUserData = UserProfile(name, phone, email)
        return true
    }
}