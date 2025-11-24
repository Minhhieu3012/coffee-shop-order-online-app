package vn.edu.ut.hieupm9898.customermobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import vn.edu.ut.hieupm9898.customermobile.navigation.AppNavigation
import vn.edu.ut.hieupm9898.customermobile.ui.theme.CustomerMobileTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, false)

        // ⛔️ COMMENT DÒNG NÀY ĐI
        // configureFirebaseEmulator()

        setContent {
            CustomerMobileTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    AppNavigation(navController = navController)
                }
            }
        }
    }

    // ⛔️ COMMENT TOÀN BỘ HÀM NÀY
    /*
    private fun configureFirebaseEmulator() {
        val isDebugMode = (applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0

        if (!isDebugMode) {
            Log.i(TAG, "App đang chạy ở Production mode - Không sử dụng Emulator")
            return
        }

        try {
            Firebase.auth.useEmulator(EMULATOR_HOST, AUTH_PORT)
            Firebase.firestore.useEmulator(EMULATOR_HOST, FIRESTORE_PORT)
            Firebase.storage.useEmulator(EMULATOR_HOST, STORAGE_PORT)
            Log.i(TAG, "🚀 Đã kết nối thành công với Firebase Emulator Suite!")
        } catch (e: Exception) {
            Log.e(TAG, "❌ LỖI: Không thể kết nối với Firebase Emulator", e)
        }
    }
    */
}