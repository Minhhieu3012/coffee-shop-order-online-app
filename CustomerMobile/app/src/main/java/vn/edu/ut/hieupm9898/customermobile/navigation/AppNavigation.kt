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
        // --- SPLASH SCREEN ---
        composable(AppRoutes.SPLASH) {
            SplashScreen(
                onGetStartedClick = {
                    navController.navigate(AppRoutes.ONBOARDING_1) {
                        popUpTo(AppRoutes.SPLASH) { inclusive = true }
                    }
                }
            )
        }

        // --- ONBOARDING FLOW ---
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

        // --- AUTH FLOW (Nested Navigation) ---
        navigation(
            startDestination = AppRoutes.LOGIN,
            route = AppRoutes.AUTH_GRAPH
        ) {
            // ✅ TRUYỀN navController vào authNavGraph
            authNavGraph(navController = navController)
        }

        // --- MAIN APP FLOW ---
        composable(AppRoutes.HOME) {
            MainScreen()
        }

        // --- PRODUCT DETAIL ---
        composable(
            route = AppRoutes.PRODUCT_DETAIL,
            arguments = listOf(
                navArgument(AppRoutes.PRODUCT_DETAIL_ID) {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getString(AppRoutes.PRODUCT_DETAIL_ID)
            // TODO: Implement ProductDetailScreen(navController, productId)
            // ProductDetailScreen(navController = navController, productId = productId ?: "")
        }
    }
}

/**
 * Helper function để navigate tới Auth Flow
 */
private fun navigateToAuthFlow(navController: NavHostController) {
    navController.navigate(AppRoutes.AUTH_GRAPH) {
        popUpTo(AppRoutes.SPLASH) { inclusive = true }
    }
}