// navigation/AppNavigation.kt

package vn.edu.ut.hieupm9898.customermobile.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.NavType
import androidx.navigation.navArgument

// Import các màn hình Onboarding (Vẫn giữ nguyên)
import vn.edu.ut.hieupm9898.customermobile.features.onboarding.SplashScreen
import vn.edu.ut.hieupm9898.customermobile.features.onboarding.Onboarding1Screen
import vn.edu.ut.hieupm9898.customermobile.features.onboarding.Onboarding2Screen
import vn.edu.ut.hieupm9898.customermobile.features.onboarding.Onboarding3Screen

// *** Đã xóa import LoginScreenPlaceholder và HomeScreenPlaceholder ***

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = AppRoutes.SPLASH // Bắt đầu bằng SplashScreen
    ) {

        // --- 1. LUỒNG KHỞI ĐỘNG (Splash & Onboarding) ---
        composable(AppRoutes.SPLASH) {
            SplashScreen(
                onGetStartedClick = {
                    navController.navigate(AppRoutes.ONBOARDING_1) {
                        popUpTo(AppRoutes.SPLASH) { inclusive = true } // Xóa Splash
                    }
                }
            )
        }

        composable(AppRoutes.ONBOARDING_1) {
            Onboarding1Screen(
                onSkip = { navigateToAuthFlow(navController) },
                onNext = { navController.navigate(AppRoutes.ONBOARDING_2) }
            )
        }

        composable(AppRoutes.ONBOARDING_2) {
            Onboarding2Screen(
                onSkip = { navigateToAuthFlow(navController) },
                onNext = { navController.navigate(AppRoutes.ONBOARDING_3) }
            )
        }

        composable(AppRoutes.ONBOARDING_3) {
            Onboarding3Screen(
                onSkip = { navigateToAuthFlow(navController) },
                onNext = { navigateToAuthFlow(navController) } // Hoàn thành -> Auth
            )
        }

        // --- 2. LUỒNG XÁC THỰC VÀ CHÍNH ---
        composable(AppRoutes.AUTH_FLOW) {
            // Cần thay bằng màn hình LoginScreen thực tế (ví dụ: LoginScreen(navController))
            androidx.compose.material3.Text("Login Screen Placeholder")
        }

        composable(AppRoutes.HOME) {
            // Cần thay bằng màn hình HomeScreen thực tế (ví dụ: HomeScreen(navController))
            androidx.compose.material3.Text("Home Screen Placeholder")
        }

        // ... Định nghĩa các composable khác (ProductDetail, Cart, Profile)
        composable(
            route = AppRoutes.PRODUCT_DETAIL,
            arguments = listOf(
                navArgument(AppRoutes.PRODUCT_DETAIL_ID) { type = NavType.StringType }
            )
        ) { /* ProductDetailScreen implementation */ }
    }
}

// Hàm tiện ích để xóa toàn bộ stack Onboarding
private fun navigateToAuthFlow(navController: NavHostController) {
    navController.navigate(AppRoutes.AUTH_FLOW) {
        // Xóa toàn bộ stack trước AUTH_FLOW (Splash, Onboarding 1, 2, 3)
        popUpTo(AppRoutes.SPLASH) { inclusive = true }
    }
}

// *** Đã xóa định nghĩa LoginScreenPlaceholder và HomeScreenPlaceholder ***