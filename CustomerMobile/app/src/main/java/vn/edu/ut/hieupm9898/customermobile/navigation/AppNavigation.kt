package vn.edu.ut.hieupm9898.customermobile.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import vn.edu.ut.hieupm9898.customermobile.features.auth.authNavGraph
import vn.edu.ut.hieupm9898.customermobile.features.onboarding.SplashScreen
import vn.edu.ut.hieupm9898.customermobile.features.onboarding.Onboarding1Screen
import vn.edu.ut.hieupm9898.customermobile.features.onboarding.Onboarding2Screen
import vn.edu.ut.hieupm9898.customermobile.features.onboarding.Onboarding3Screen
import vn.edu.ut.hieupm9898.customermobile.features.main.MainScreen

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = AppRoutes.SPLASH
    ) {
        // --- 1. MÀN HÌNH CHỜ (SPLASH) ---
        composable(AppRoutes.SPLASH) {
            SplashScreen(
                navController = navController, // Truyền vào để nó tự check đăng nhập
                onGetStartedClick = {
                    // Nếu bấm Bắt đầu -> Vào Onboarding
                    navController.navigate(AppRoutes.ONBOARDING_1) {
                        popUpTo(AppRoutes.SPLASH) { inclusive = true }
                    }
                }
            )
        }

        // --- 2. LUỒNG ONBOARDING ---
        composable(AppRoutes.ONBOARDING_1) {
            Onboarding1Screen(
                onSkip = { navigateToAuthFlow(navController) },
                onNext = { navController.navigate(AppRoutes.ONBOARDING_2) },
                onGetStartedClick = { navigateToAuthFlow(navController) }
            )
        }

        composable(AppRoutes.ONBOARDING_2) {
            Onboarding2Screen(
                onSkip = { navigateToAuthFlow(navController) },
                onNext = { navController.navigate(AppRoutes.ONBOARDING_3) },
                onGetStartedClick = { navigateToAuthFlow(navController) }
            )
        }

        composable(AppRoutes.ONBOARDING_3) {
            Onboarding3Screen(
                onSkip = { navigateToAuthFlow(navController) },
                onNext = { navigateToAuthFlow(navController) },
                onGetStartedClick = { navigateToAuthFlow(navController) }
            )
        }

        // --- 3. LUỒNG XÁC THỰC (LOGIN/REGISTER) ---
        authNavGraph(
            navController = navController,
            onLoginSuccess = {
                navController.navigate(AppRoutes.MAIN_APP_GRAPH) {
                    popUpTo(AppRoutes.AUTH_GRAPH) { inclusive = true }
                }
            }
        )

        // --- 4. LUỒNG ỨNG DỤNG CHÍNH (MAIN APP) ---
        // Đây là "ngôi nhà chung" sau khi đăng nhập thành công
        navigation(
            route = AppRoutes.MAIN_APP_GRAPH,
            startDestination = AppRoutes.HOME
        ) {
            // Màn hình chứa Bottom Bar (Trang chủ, Giỏ hàng...)
            composable(AppRoutes.HOME) {
                MainScreen()
            }

            // Màn hình Chi tiết sản phẩm
            composable(
                route = AppRoutes.PRODUCT_DETAIL,
                arguments = listOf(
                    navArgument(AppRoutes.PRODUCT_DETAIL_ID) {
                        type = NavType.StringType
                    }
                )
            ) { backStackEntry ->
                val productId = backStackEntry.arguments?.getString(AppRoutes.PRODUCT_DETAIL_ID)
                // TODO: Gọi màn hình ProductDetailScreen(productId = productId) tại đây
            }
        }
    }
}

/**
 * Hàm phụ trợ: Chuyển đến luồng Đăng nhập
 */
private fun navigateToAuthFlow(navController: NavHostController) {
    navController.navigate(AppRoutes.AUTH_GRAPH) {
        popUpTo(AppRoutes.SPLASH) { inclusive = true }
    }
}