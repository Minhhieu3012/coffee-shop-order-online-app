package vn.edu.ut.hieupm9898.customermobile

import android.content.pm.ApplicationInfo
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import vn.edu.ut.hieupm9898.customermobile.navigation.AppNavigation
import vn.edu.ut.hieupm9898.customermobile.ui.theme.CustomerMobileTheme

class MainActivity : ComponentActivity() {

    companion object {
        private const val TAG = "MainActivity"
        private const val EMULATOR_HOST = "10.0.2.2"
        private const val AUTH_PORT = 9099
        private const val FIRESTORE_PORT = 8080
        private const val STORAGE_PORT = 9199
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Enable edge-to-edge display
        enableEdgeToEdge()

        // Cấu hình Window để hỗ trợ Edge-to-Edge tốt hơn
        WindowCompat.setDecorFitsSystemWindows(window, false)

        // Cấu hình Firebase Emulator (chỉ trong Debug mode)
        configureFirebaseEmulator()

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

    /**
     * Cấu hình Firebase Emulator cho môi trường phát triển
     * Chỉ chạy khi app ở chế độ Debug
     */
    private fun configureFirebaseEmulator() {
        val isDebugMode = (applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0

        if (!isDebugMode) {
            Log.i(TAG, "App đang chạy ở Production mode - Không sử dụng Emulator")
            return
        }

        try {
            // Cấu hình Firebase Auth Emulator
            Firebase.auth.useEmulator(EMULATOR_HOST, AUTH_PORT)
            Log.d(TAG, "✓ Firebase Auth Emulator: $EMULATOR_HOST:$AUTH_PORT")

            // Cấu hình Firestore Emulator
            Firebase.firestore.useEmulator(EMULATOR_HOST, FIRESTORE_PORT)
            Log.d(TAG, "✓ Firestore Emulator: $EMULATOR_HOST:$FIRESTORE_PORT")

            // Cấu hình Storage Emulator
            Firebase.storage.useEmulator(EMULATOR_HOST, STORAGE_PORT)
            Log.d(TAG, "✓ Storage Emulator: $EMULATOR_HOST:$STORAGE_PORT")

            Log.i(TAG, "🚀 Đã kết nối thành công với Firebase Emulator Suite!")

        } catch (e: IllegalStateException) {
            // Emulator đã được cấu hình trước đó
            Log.w(TAG, "⚠️ Firebase Emulator đã được cấu hình rồi", e)
        } catch (e: Exception) {
            // Lỗi khác
            Log.e(TAG, "❌ LỖI: Không thể kết nối với Firebase Emulator", e)
        }
    }
}