package vn.edu.ut.hieupm9898.customermobile.features.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavGraph.Companion.findStartDestination
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
                    SplashScreen(
                        onGetStartedClick = {
                            navController.navigate(AppRoutes.AUTH_GRAPH) {
                                popUpTo(AppRoutes.SPLASH) { inclusive = true }
                            }
                        },
                        onTimeout = {
                            navController.navigate(AppRoutes.ONBOARDING)
                        }
                    )
                }

                composable(AppRoutes.ONBOARDING) {
                    OnboardingPagerScreen(
                        onGetStartedClick = {
                            navController.navigate(AppRoutes.AUTH_GRAPH) {
                                popUpTo(AppRoutes.SPLASH) { inclusive = true }
                            }
                        }
                    )
                }

                // 2. AUTH GRAPH (LOGIN, REGISTER, ETC.)
                authNavGraph(
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

                    composable(AppRoutes.FAVORITE) {
                        FavoriteScreen(
                            onProductClick = { id ->
                                navController.navigate("${AppRoutes.DETAIL_BASE}/$id")
                            },
                            onGoHomeClick = {
                                navController.navigate(AppRoutes.HOME) {
                                    popUpTo(AppRoutes.HOME) { inclusive = false }
                                }
                            }
                        )
                    }

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
                            onNotificationsClick = { navController.navigate(AppRoutes.NOTIFICATIONS) },
                            onLogoutClick = {
                                navController.navigate(AppRoutes.AUTH_GRAPH) {
                                    popUpTo(AppRoutes.MAIN_APP_GRAPH) { inclusive = true }
                                }
                            }
                        )
                    }

                    // SUB SCREENS
                    composable(
                        route = AppRoutes.DETAIL,
                        arguments = listOf(navArgument("id") { type = NavType.IntType })
                    ) { backStackEntry ->
                        val productId = backStackEntry.arguments?.getInt("id") ?: 0

                        // Mock data cho demo - thay bằng ViewModel thực tế
                        var isFavorite by remember { mutableStateOf(false) }
                        var selectedSize by remember { mutableStateOf("Small") }
                        var selectedDairy by remember { mutableStateOf("Whole Milk") }

                        ProductDetailScreen(
                            productId = productId,
                            title = "Cold coffee frapuccino",
                            subtitle = "90mg Caffeine : 100Cal",
                            rating = 3.0f,
                            ratingCountText = "(3.0)",
                            description = "Tasteful and flavorful icecream coffee. Ice cream with whipped cream and caramel syrup.",
                            imageUrl = "https://img.freepik.com/free-photo/cup-coffee-with-heart-drawn-foam_1286-70.jpg",
                            isFavorite = isFavorite,
                            availableSizes = listOf("Small", "Medium", "Large"),
                            selectedSize = selectedSize,
                            availableDairy = listOf(
                                Pair("Whole Milk", 0.57),
                                Pair("Almond Milk", 1.00),
                                Pair("Oat Milk", 1.25)
                            ),
                            selectedDairy = selectedDairy,
                            relatedProducts = emptyList(),
                            onBackClick = { navController.popBackStack() },
                            onFavoriteClick = { isFavorite = !isFavorite },
                            onSizeSelected = { selectedSize = it },
                            onDairySelected = { selectedDairy = it },
                            onAddToCartClick = { /* TODO: Add to cart logic */ }
                        )
                    }

                    composable(AppRoutes.SEARCH) {
                        SearchScreen(
                            onBackClick = { navController.popBackStack() }
                        )
                    }

                    composable(AppRoutes.EDIT_PROFILE) {
                        EditProfileScreen(
                            onBackClick = { navController.popBackStack() }
                        )
                    }

                    composable(AppRoutes.ADDRESS_LIST) {
                        AddressScreen(
                            onBackClick = { navController.popBackStack() }
                        )
                    }

                    composable(AppRoutes.ADD_ADDRESS) {
                        AddAddressScreen(
                            onBackClick = { navController.popBackStack() }
                        )
                    }

                    composable(AppRoutes.CHANGE_PASS) {
                        ChangePasswordScreen(
                            onBackClick = { navController.popBackStack() }
                        )
                    }

                    composable(AppRoutes.CONTACT) {
                        ContactUsScreen(
                            onBackClick = { navController.popBackStack() }
                        )
                    }

                    composable(AppRoutes.NOTIFICATIONS) {
                        NotificationScreen(
                            onBackClick = { navController.popBackStack() }
                        )
                    }

                    composable(AppRoutes.ORDER_HISTORY) {
                        OrderHistoryScreen(
                            onBackClick = { navController.popBackStack() }
                        )
                    }

                    composable(AppRoutes.REWARDS) {
                        RewardsScreen(
                            onBackClick = { navController.popBackStack() }
                        )
                    }

                    composable(AppRoutes.SETTINGS) {
                        SettingsScreen(
                            onBackClick = { navController.popBackStack() }
                        )
                    }

                    composable(AppRoutes.FEEDBACK) {
                        FeedbackScreen(
                            onBackClick = { navController.popBackStack() }
                        )
                    }

                    composable(AppRoutes.DELETE_ACCOUNT) {
                        DeleteAccountScreen(
                            onBackClick = { navController.popBackStack() }
                        )
                    }

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

                    composable(AppRoutes.DELIVERY) {
                        DeliveryScreen(
                            onBackClick = { navController.navigate(AppRoutes.ORDER_HISTORY) }
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    CustomerMobileTheme {
        MainScreen()
    }
}