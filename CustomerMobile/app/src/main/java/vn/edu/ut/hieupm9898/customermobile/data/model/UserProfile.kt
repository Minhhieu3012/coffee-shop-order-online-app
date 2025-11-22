package vn.edu.ut.hieupm9898.customermobile.data.model

// [ĐẢM BẢO] File User.kt (hoặc UserProfile.kt) của bạn chứa class này
data class UserProfile(
    val name: String,
    val phone: String,
    val email: String,
    val avatarUrl: String? = null

)