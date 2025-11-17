package vn.edu.ut.hieupm9898.customermobile.navigation

import androidx.compose.runtime.Composable // Cần thiết cho @Composable
import androidx.navigation.NavController // Cần thiết nếu bạn dùng NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost // Cần thiết cho NavHost
import androidx.navigation.compose.composable // Cần thiết cho composable function
import androidx.navigation.compose.rememberNavController // Cần thiết nếu bạn dùng rememberNavController
import androidx.navigation.NavType // Cần thiết cho NavType
import androidx.navigation.navArgument


// 1. Định nghĩa Routes
object Screens {
    // Luồng Khởi động
    const val Splash = "splash_screen"
    const val Onboarding = "onboarding_flow"
    //Luồng Xác Thực
    const val Login = "login_screen"
    const val Register = "register_screen"

    const val Home = "home_screen"
    const val Cart = "cart_screen"
    const val Profile = "profile_screen"

    const val ProductDetail = "product_detail/{productId}"
    const val ProductIdArg = "productId"
}

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        // Đặt màn hình khởi đầu là Splash Screen
        startDestination = Screens.Splash
    ) {
        // --- 1. LUỒNG KHỞI ĐỘNG ---

        composable(Screens.Splash) {
        }

        composable(Screens.Onboarding) {
        }

        // --- 2. LUỒNG XÁC THỰC ---

        composable(Screens.Login) {
        }

        composable(Screens.Register) {
        }


        // Màn hình chi tiết sản phẩm
        composable(
            route = Screens.ProductDetail,
            arguments = listOf(navArgument(Screens.ProductIdArg) { type = NavType.StringType })
        ) {
        }

        composable(Screens.Home) { /* HomeScreen Placeholder */ }
        composable(Screens.Cart) { /* CartScreen Placeholder */ }
        composable(Screens.Profile) { /* ProfileScreen Placeholder */ }
    }
}