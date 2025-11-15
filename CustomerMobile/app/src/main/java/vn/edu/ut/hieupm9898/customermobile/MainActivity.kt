package vn.edu.ut.hieupm9898.customermobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import vn.edu.ut.hieupm9898.customermobile.ui.theme.CustomerMobileTheme

// Thư viện Firebase
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import android.util.Log
import com.google.firebase.auth.ktx.BuildConfig


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // --- BẮT ĐẦU CODE KẾT NỐI EMULATOR ---
        // (Code này chỉ chạy khi bạn đang "Debug" app)
        if (BuildConfig.DEBUG) {
            try {
                // "10.0.2.2" là địa chỉ IP đặc biệt để máy ảo Android
                // trỏ về "localhost" (127.0.0.1) của máy tính của bạn
                val host = "10.0.2.2"

                // Trỏ Firebase Auth về Emulator cổng 9099
                Firebase.auth.useEmulator(host, 9099)

                // Trỏ Firestore về Emulator cổng 8080
                Firebase.firestore.useEmulator(host, 8080)

                // Trỏ Storage về Emulator cổng 9199
                Firebase.storage.useEmulator(host, 9199)

                // Ghi log (nhật ký) để chúng ta biết là đã thành công
                Log.i("EmulatorConfig", "Đã kết nối thành công với Firebase Emulator!")

            } catch (e: Exception) {
                // Ghi log nếu có lỗi
                Log.e("EmulatorConfig", "LỖI: Không thể kết nối với Emulator.", e)
            }
        }
        // --- KẾT THÚC CODE KẾT NỐI EMULATOR ---

        setContent {
            CustomerMobileTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CustomerMobileTheme {
        Greeting("Android")
    }
}