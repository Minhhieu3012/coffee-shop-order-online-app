package vn.edu.ut.hieupm9898.customermobile.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import vn.edu.ut.hieupm9898.customermobile.features.auth.authNavGraph
import vn.edu.ut.hieupm9898.customermobile.features.main.MainScreen
import vn.edu.ut.hieupm9898.customermobile.features.onboarding.SplashScreen
import vn.edu.ut.hieupm9898.customermobile.features.onboarding.Onboarding1Screen
import vn.edu.ut.hieupm9898.customermobile.features.onboarding.Onboarding2Screen
import vn.edu.ut.hieupm9898.customermobile.features.onboarding.Onboarding3Screen

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = AppRoutes.SPLASH
    ) {

        composable(AppRoutes.SPLASH) {
            SplashScreen(
                onGetStartedClick = {
                    navController.navigate(AppRoutes.ONBOARDING_1) {
                        popUpTo(AppRoutes.SPLASH) { inclusive = true }
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
                onNext = { navigateToAuthFlow(navController) }
            )
        }

        // --- TÍCH HỢP LUỒNG XÁC THỰC (AUTH FLOW) ---
        navigation(
            startDestination = AppRoutes.LOGIN,
            route = AppRoutes.AUTH_FLOW
        ) {
            authNavGraph()
        }

        // --- TÍCH HỢP MÀN HÌNH CHÍNH (MAIN APP FLOW) ---
        composable(AppRoutes.HOME) {
            MainScreen()
        }

        composable(
            route = AppRoutes.PRODUCT_DETAIL,
            arguments = listOf(
                navArgument(AppRoutes.PRODUCT_DETAIL_ID) { type = NavType.StringType }
            )
        ) { /* ProductDetailScreen implementation */ }
    }
}

private fun navigateToAuthFlow(navController: NavHostController) {
    navController.navigate(AppRoutes.AUTH_FLOW) {
        popUpTo(AppRoutes.SPLASH) { inclusive = true }
    }
}