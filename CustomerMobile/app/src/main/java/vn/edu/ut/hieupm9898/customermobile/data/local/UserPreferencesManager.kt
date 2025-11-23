package vn.edu.ut.hieupm9898.customermobile.data.local

import android.content.SharedPreferences
import javax.inject.Inject

class UserPreferencesManager @Inject constructor(
    private val sharedPreferences: SharedPreferences
) {
    companion object {
        private const val KEY_IS_REMEMBER_ME = "key_is_remember_me"
    }

    /**
     * Lưu trạng thái "Ghi nhớ đăng nhập"
     */
    fun saveRememberMe(isRemember: Boolean) {
        sharedPreferences.edit().putBoolean(KEY_IS_REMEMBER_ME, isRemember).apply()
    }

    /**
     * Lấy trạng thái "Ghi nhớ đăng nhập", mặc định là false
     */
    fun getRememberMe(): Boolean {
        return sharedPreferences.getBoolean(KEY_IS_REMEMBER_ME, false)
    }

    /**
     * Xóa toàn bộ dữ liệu khi Logout
     */
    fun clear() {
        sharedPreferences.edit().clear().apply()
    }
}