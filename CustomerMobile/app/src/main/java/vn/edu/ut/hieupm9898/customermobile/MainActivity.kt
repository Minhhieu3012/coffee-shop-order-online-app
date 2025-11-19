package vn.edu.ut.hieupm9898.customermobile

import android.content.pm.ApplicationInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import vn.edu.ut.hieupm9898.customermobile.ui.theme.CustomerMobileTheme
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import android.util.Log
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.navigation.compose.rememberNavController
import vn.edu.ut.hieupm9898.customermobile.navigation.AppNavigation

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val isDebugMode = (applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0

        if (isDebugMode) {
            try {
                val host = "10.0.2.2"

                Firebase.auth.useEmulator(host, 9099)

                Firebase.firestore.useEmulator(host, 8080)

                Firebase.storage.useEmulator(host, 9199)

                Log.i("EmulatorConfig", "Đã kết nối thành công với Firebase Emulator!")

            } catch (e: Exception) {
                Log.e("EmulatorConfig", "LỖI: Không thể kết nối với Emulator.", e)
            }
        }

        setContent {
            CustomerMobileTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    val navController = rememberNavController()
                    AppNavigation(navController = navController)
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