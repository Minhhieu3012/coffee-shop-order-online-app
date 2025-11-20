package vn.edu.ut.hieupm9898.customermobile.features.main
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navigation
import vn.edu.ut.hieupm9898.customermobile.features.auth.*
import vn.edu.ut.hieupm9898.customermobile.features.cart.*
import vn.edu.ut.hieupm9898.customermobile.features.favorite.FavoriteScreen
import vn.edu.ut.hieupm9898.customermobile.features.home.*
import vn.edu.ut.hieupm9898.customermobile.features.onboarding.*
import vn.edu.ut.hieupm9898.customermobile.features.product_detail.ProductDetailScreen
import vn.edu.ut.hieupm9898.customermobile.features.profile.*
import vn.edu.ut.hieupm9898.customermobile.navigation.AppRoutes
import vn.edu.ut.hieupm9898.customermobile.ui.components.BrosBottomNavBar
import vn.edu.ut.hieupm9898.customermobile.ui.theme.CustomerMobileTheme
import vn.edu.ut.hieupm9898.customermobile.features.auth.LoginScreen
import vn.edu.ut.hieupm9898.customermobile.features.auth.RegisterScreen

// Khai báo hàm riêng cho Auth Graph để giữ MainScreen gọn gàng
fun NavGraphBuilder.authGraph(
    navController: NavHostController,
    onLoginSuccess: () -> Unit
) {
    navigation(startDestination = AppRoutes.LOGIN, route = AppRoutes.AUTH_GRAPH) {

        composable(AppRoutes.LOGIN) {
            LoginScreen(
                onLoginSuccess = onLoginSuccess,
                onNavigateToRegister = { navController.navigate(AppRoutes.REGISTER) },
                onNavigateToForgot = { navController.navigate("forgot_password") }
            )
        }

        // ... (Thêm các composable khác của Auth như RegisterScreen, OTPVerificationScreen, v.v.)
        // Ví dụ:
        composable(AppRoutes.REGISTER) {
            RegisterScreen(onBackClick = { navController.popBackStack() })
        }
        composable("forgot_password") {
            ForgotPasswordScreen(onBackClick = { navController.popBackStack() })
        }
    }
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val bottomBarRoutes = listOf(AppRoutes.HOME, AppRoutes.FAVORITE, AppRoutes.CART, AppRoutes.PROFILE)

    CustomerMobileTheme {
        Scaffold(
            bottomBar = {
                if (currentRoute in bottomBarRoutes) {
                    BrosBottomNavBar(
                        currentRoute = currentRoute ?: AppRoutes.HOME,
                        onNavigate = { route ->
                            navController.navigate(route) {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        ) { paddingValues ->

            NavHost(
                navController = navController,
                startDestination = AppRoutes.SPLASH,
                modifier = Modifier.padding(paddingValues)
            ) {

                // 1. ONBOARDING & SPLASH GRAPH
                composable(AppRoutes.SPLASH) {
                    SplashScreen(onTimeout = { navController.navigate(AppRoutes.ONBOARDING) })
                }

                composable(AppRoutes.ONBOARDING) {
                    OnboardingScreen(onGetStartedClick = {
                        navController.navigate(AppRoutes.AUTH_GRAPH) {
                            popUpTo(AppRoutes.SPLASH) { inclusive = true }
                        }
                    })
                }

                // 2. AUTH GRAPH (LOGIN, REGISTER, ETC.)
                authGraph(
                    navController = navController,
                    onLoginSuccess = {
                        navController.navigate(AppRoutes.MAIN_APP_GRAPH) {
                            popUpTo(AppRoutes.AUTH_GRAPH) { inclusive = true }
                        }
                    }
                )

                // 3. MAIN APP GRAPH (HOME, PROFILE, ETC.)
                navigation(startDestination = AppRoutes.HOME, route = AppRoutes.MAIN_APP_GRAPH) {

                    // MAIN TABS
                    composable(AppRoutes.HOME) {
                        HomeScreen(
                            onProductClick = { id -> navController.navigate("${AppRoutes.DETAIL_BASE}/$id") },
                            onSearchClick = { navController.navigate(AppRoutes.SEARCH) }
                        )
                    }

                    composable(AppRoutes.FAVORITE) { FavoriteScreen() }

                    composable(AppRoutes.CART) {
                        OrderScreen(
                            onBackClick = { navController.popBackStack() },
                            onOrderClick = { navController.navigate(AppRoutes.PAYMENT_QR) }
                        )
                    }

                    composable(AppRoutes.PROFILE) {
                        ProfileScreen(
                            onEditProfileClick = { navController.navigate(AppRoutes.EDIT_PROFILE) },
                            onAddressClick = { navController.navigate(AppRoutes.ADDRESS_LIST) },
                            onPaymentClick = { navController.navigate(AppRoutes.PAYMENT_METHODS) },
                            onHistoryClick = { navController.navigate(AppRoutes.ORDER_HISTORY) },
                            onNotificationsClick = { navController.navigate(AppRoutes.NOTIFICATIONS) }
                        )
                    }

                    // SUB SCREENS
                    composable(
                        route = AppRoutes.DETAIL,
                        arguments = listOf(navArgument("id") { type = NavType.IntType })
                    ) {
                        ProductDetailScreen(
                            productId = it.arguments?.getInt("id") ?: 0,
                            onBackClick = { navController.popBackStack() }
                        )
                    }

                    composable(AppRoutes.SEARCH) { SearchScreen(onBackClick = { navController.popBackStack() }) }
                    composable(AppRoutes.EDIT_PROFILE) { EditProfileScreen(onBackClick = { navController.popBackStack() }) }
                    composable(AppRoutes.ADDRESS_LIST) { AddressScreen(onBackClick = { navController.popBackStack() }) }
                    composable(AppRoutes.ADD_ADDRESS) { AddAddressScreen(onBackClick = { navController.popBackStack() }) }
                    composable(AppRoutes.CHANGE_PASS) { ChangePasswordScreen(onBackClick = { navController.popBackStack() }) }
                    composable(AppRoutes.CONTACT) { ContactUsScreen(onBackClick = { navController.popBackStack() }) }
                    composable(AppRoutes.NOTIFICATIONS) { NotificationScreen(onBackClick = { navController.popBackStack() }) }
                    composable(AppRoutes.ORDER_HISTORY) { OrderHistoryScreen(onBackClick = { navController.popBackStack() }) }
                    composable(AppRoutes.REWARDS) { RewardsScreen(onBackClick = { navController.popBackStack() }) }
                    composable(AppRoutes.SETTINGS) { SettingsScreen(onBackClick = { navController.popBackStack() }) }
                    composable(AppRoutes.FEEDBACK) { FeedbackScreen(onBackClick = { navController.popBackStack() }) }
                    composable(AppRoutes.DELETE_ACCOUNT) { DeleteAccountScreen(onBackClick = { navController.popBackStack() }) }

                    // CART FLOW
                    composable(AppRoutes.PAYMENT_QR) {
                        PaymentQRScreen(
                            onBackClick = { navController.popBackStack() },
                            onPaymentSuccess = { navController.navigate(AppRoutes.ORDER_SUCCESS) }
                        )
                    }

                    composable(AppRoutes.ORDER_SUCCESS) {
                        OrderSuccessScreen(
                            onTrackOrderClick = { navController.navigate(AppRoutes.DELIVERY) },
                            onHomeClick = { navController.navigate(AppRoutes.HOME) }
                        )
                    }

                    composable(AppRoutes.DELIVERY) { DeliveryScreen(onBackClick = { navController.navigate(AppRoutes.ORDER_HISTORY) }) }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    CustomerMobileTheme {
        // Preview đơn giản của MainScreen
    }
}