package vn.edu.ut.hieupm9898.customermobile.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import vn.edu.ut.hieupm9898.customermobile.features.auth.authNavGraph
import vn.edu.ut.hieupm9898.customermobile.features.profile.profileNavGraph
import vn.edu.ut.hieupm9898.customermobile.features.onboarding.SplashScreen
import vn.edu.ut.hieupm9898.customermobile.features.onboarding.Onboarding1Screen
import vn.edu.ut.hieupm9898.customermobile.features.onboarding.Onboarding2Screen
import vn.edu.ut.hieupm9898.customermobile.features.onboarding.Onboarding3Screen
import vn.edu.ut.hieupm9898.customermobile.features.main.MainScreen
import vn.edu.ut.hieupm9898.customermobile.features.favorite.FavoriteScreen
import vn.edu.ut.hieupm9898.customermobile.features.cart.PaymentQRScreen
import vn.edu.ut.hieupm9898.customermobile.features.cart.OrderSuccessScreen
import vn.edu.ut.hieupm9898.customermobile.features.orders.OrderHistoryScreen
import vn.edu.ut.hieupm9898.customermobile.features.cart.CartScreen
import vn.edu.ut.hieupm9898.customermobile.features.profile.ProfileScreen // ✅ Import ProfileScreen

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = AppRoutes.SPLASH
    ) {
        // 1. Splash
        composable(AppRoutes.SPLASH) {
            SplashScreen(
                navController = navController,
                onGetStartedClick = {
                    navController.navigate(AppRoutes.ONBOARDING_1) {
                        popUpTo(AppRoutes.SPLASH) { inclusive = true }
                    }
                }
            )
        }

        // 2. Onboarding
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

        // 3. Auth graph
        authNavGraph(
            navController = navController,
            onLoginSuccess = {
                navController.navigate(AppRoutes.MAIN_APP_GRAPH) {
                    popUpTo(AppRoutes.AUTH_GRAPH) { inclusive = true }
                }
            }
        )

        // 4. Main app graph
        navigation(
            route = AppRoutes.MAIN_APP_GRAPH,
            startDestination = AppRoutes.HOME
        ) {
            // Home chứa bottom bar
            composable(AppRoutes.HOME) {
                MainScreen(rootNavController = navController)
            }

            // Favorite route
            composable(AppRoutes.FAVORITE) {
                FavoriteScreen(
                    onProductClick = { productId ->
                        navController.navigate(AppRoutes.createProductDetailRoute(productId))
                    },
                    onGoHomeClick = {
                        navController.navigate(AppRoutes.HOME) {
                            popUpTo(AppRoutes.HOME) { inclusive = false }
                            launchSingleTop = true
                        }
                    },
                    // Đảm bảo FavoriteScreen có tham số này nếu bạn đã thêm ở bước trước
                    onNavigateToCart = {
                        navController.navigate(AppRoutes.CART) {
                            launchSingleTop = true
                        }
                    }
                )
            }

            // ✅ THÊM MÀN HÌNH PROFILE VÀO ĐÂY ĐỂ TRÁNH CRASH ✅
            composable(AppRoutes.PROFILE) {
                ProfileScreen(
                    navController = navController,
                    onEditProfileClick = { navController.navigate(AppRoutes.EDIT_PROFILE) },
                    onAddressClick = { navController.navigate(AppRoutes.ADDRESS_LIST) },
                    onPaymentClick = { navController.navigate(AppRoutes.PAYMENT_METHODS) },
                    onHistoryClick = { navController.navigate(AppRoutes.ORDER_HISTORY) },
                    onNotificationsClick = { navController.navigate(AppRoutes.NOTIFICATIONS) },
                    onBackClick = {
                        // Khi back từ tab Profile ở Root, thường sẽ về Home
                        navController.navigate(AppRoutes.HOME) {
                            popUpTo(AppRoutes.HOME) { inclusive = true }
                        }
                    }
                )
            }

            // Payment QR Screen
            composable(route = AppRoutes.PAYMENT_QR) {
                PaymentQRScreen(navController = navController)
            }

            // Order Success Screen
            composable(route = AppRoutes.ORDER_SUCCESS) {
                OrderSuccessScreen(
                    onViewOrderClick = {
                        navController.navigate(AppRoutes.ORDER_HISTORY) {
                            popUpTo(AppRoutes.HOME) { inclusive = false }
                            launchSingleTop = true
                        }
                    },
                    onHomeClick = {
                        navController.navigate(AppRoutes.HOME) {
                            popUpTo(AppRoutes.HOME) { inclusive = true }
                        }
                    }
                )
            }

            // Đăng ký các màn hình con của Profile (Edit, Address, v.v.)
            profileNavGraph(navController)

            // Product detail
            composable(
                route = AppRoutes.PRODUCT_DETAIL,
                arguments = listOf(
                    navArgument(AppRoutes.PRODUCT_DETAIL_ID) {
                        type = NavType.StringType
                    }
                )
            ) { backStackEntry ->
                val productId =
                    backStackEntry.arguments?.getString(AppRoutes.PRODUCT_DETAIL_ID)
                // TODO: ProductDetailScreen(productId)
            }

            // Màn hình Lịch sử đơn hàng
            composable(AppRoutes.ORDER_HISTORY) {
                OrderHistoryScreen(
                    onBackClick = {
                        navController.navigate(AppRoutes.HOME) {
                            popUpTo(AppRoutes.HOME) { inclusive = true }
                        }
                    },
                    onNavigateToCart = {
                        navController.navigate(AppRoutes.CART) {
                            launchSingleTop = true
                        }
                    }
                )
            }

            // Màn hình Giỏ hàng
            composable(AppRoutes.CART) {
                CartScreen(navController = navController)
            }
        }
    }
}

private fun navigateToAuthFlow(navController: NavHostController) {
    navController.navigate(AppRoutes.AUTH_GRAPH) {
        popUpTo(AppRoutes.SPLASH) { inclusive = true }
    }
}