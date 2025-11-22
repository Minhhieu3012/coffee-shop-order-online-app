package vn.edu.ut.hieupm9898.customermobile.features.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController // Import này
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

// --- IMPORT ---
import vn.edu.ut.hieupm9898.customermobile.features.cart.*
import vn.edu.ut.hieupm9898.customermobile.features.favorite.FavoriteScreen
import vn.edu.ut.hieupm9898.customermobile.features.home.*
import vn.edu.ut.hieupm9898.customermobile.features.product_detail.ProductDetailScreen
import vn.edu.ut.hieupm9898.customermobile.features.profile.*
import vn.edu.ut.hieupm9898.customermobile.navigation.AppRoutes
import vn.edu.ut.hieupm9898.customermobile.ui.components.BrosBottomNavBar
import vn.edu.ut.hieupm9898.customermobile.ui.theme.CustomerMobileTheme

@Composable
fun MainScreen(
    // [QUAN TRỌNG] Nhận rootNavController để điều hướng Đăng xuất
    rootNavController: NavHostController? = null
) {
    // NavController này chỉ quản lý các tab con (Home, Cart, Profile...)
    val mainNavController = rememberNavController()

    val navBackStackEntry by mainNavController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val bottomBarRoutes = listOf(AppRoutes.HOME, AppRoutes.FAVORITE, AppRoutes.CART, AppRoutes.PROFILE)

    CustomerMobileTheme {
        Scaffold(
            bottomBar = {
                // Chỉ hiện BottomBar ở 4 màn hình chính
                if (currentRoute in bottomBarRoutes) {
                    BrosBottomNavBar(
                        currentRoute = currentRoute ?: AppRoutes.HOME,
                        onNavigate = { route ->
                            mainNavController.navigate(route) {
                                popUpTo(mainNavController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        ) { paddingValues ->

            NavHost(
                navController = mainNavController,
                startDestination = AppRoutes.HOME,
                modifier = Modifier.padding(paddingValues)
            ) {

                // --- 1. CÁC TAB CHÍNH (MAIN TABS) ---
                composable(AppRoutes.HOME) {
                    HomeScreen(
                        onProductClick = { id -> mainNavController.navigate("${AppRoutes.DETAIL_BASE}/$id") },
                        onSearchClick = { mainNavController.navigate(AppRoutes.SEARCH) }
                    )
                }

                composable(AppRoutes.FAVORITE) {
                    FavoriteScreen(
                        onProductClick = { id ->
                            mainNavController.navigate("${AppRoutes.DETAIL_BASE}/$id")
                        },
                        onGoHomeClick = {
                            mainNavController.navigate(AppRoutes.HOME) {
                                popUpTo(AppRoutes.HOME) { inclusive = false }
                            }
                        }
                    )
                }

                composable(AppRoutes.CART) {
                    OrderScreen(
                        onBackClick = { mainNavController.popBackStack() },
                        onOrderClick = { mainNavController.navigate(AppRoutes.PAYMENT_QR) }
                    )
                }

                composable(AppRoutes.PROFILE) {
                    // [QUAN TRỌNG] Truyền rootNavController vào Profile để nó Đăng xuất được
                    // Nếu rootNavController null (khi preview), dùng tạm mainNavController để không crash
                    val controllerToUse = rootNavController ?: mainNavController

                    ProfileScreen(
                        navController = controllerToUse,
                        onEditProfileClick = { mainNavController.navigate(AppRoutes.EDIT_PROFILE) },
                        onAddressClick = { mainNavController.navigate(AppRoutes.ADDRESS_LIST) },
                        onPaymentClick = { mainNavController.navigate(AppRoutes.PAYMENT_METHODS) },
                        onHistoryClick = { mainNavController.navigate(AppRoutes.ORDER_HISTORY) },
                        onNotificationsClick = { mainNavController.navigate(AppRoutes.NOTIFICATIONS) }
                        // Đã xóa onLogoutClick vì ProfileScreen tự xử lý bên trong
                    )
                }

                // --- 2. CHI TIẾT SẢN PHẨM ---
                composable(
                    route = AppRoutes.DETAIL,
                    arguments = listOf(navArgument("id") { type = NavType.StringType })
                ) { backStackEntry ->
                    val productId = backStackEntry.arguments?.getString("id") ?: ""

                    ProductDetailScreen(
                        title = "Cold Coffee", // Mock data
                        subtitle = "100mg Caffeine · 120 Cal",
                        rating = 4.5f,
                        ratingCountText = "(4.5)",
                        description = "Mô tả sản phẩm...",
                        imageUrl = "",
                        isFavorite = false,
                        availableSizes = listOf("Nhỏ", "Trung bình", "Lớn"),
                        selectedSize = "Trung bình",
                        availableDairy = listOf("Whole Milk" to 0.0, "Almond Milk" to 1.0),
                        selectedDairy = "Whole Milk",
                        relatedProducts = emptyList(),
                        onBackClick = { mainNavController.popBackStack() },
                        onFavoriteClick = { },
                        onSizeSelected = { },
                        onDairySelected = { },
                        onAddToCartClick = { },
                        onRelatedProductClick = { product ->
                            mainNavController.navigate("${AppRoutes.DETAIL_BASE}/${product.id}")
                        }
                    )
                }

                // --- 3. CÁC MÀN HÌNH CON KHÁC ---
                composable(AppRoutes.SEARCH) {
                    SearchScreen(onBackClick = { mainNavController.popBackStack() })
                }

                composable(AppRoutes.EDIT_PROFILE) {
                    EditProfileScreen(onBackClick = { mainNavController.popBackStack() })
                }

                composable(AppRoutes.ADDRESS_LIST) {
                    AddressScreen(onBackClick = { mainNavController.popBackStack() })
                }

                composable(AppRoutes.ADD_ADDRESS) {
                    AddAddressScreen(onBackClick = { mainNavController.popBackStack() })
                }

                composable(AppRoutes.CHANGE_PASS) {
                    ChangePasswordScreen(onBackClick = { mainNavController.popBackStack() })
                }

                composable(AppRoutes.CONTACT) {
                    ContactUsScreen(onBackClick = { mainNavController.popBackStack() })
                }

                composable(AppRoutes.NOTIFICATIONS) {
                    NotificationScreen(onBackClick = { mainNavController.popBackStack() })
                }

                composable(AppRoutes.ORDER_HISTORY) {
                    OrderHistoryScreen(onBackClick = { mainNavController.popBackStack() })
                }

                composable(AppRoutes.REWARDS) {
                    RewardsScreen(onBackClick = { mainNavController.popBackStack() })
                }

                composable(AppRoutes.SETTINGS) {
                    SettingsScreen(onBackClick = { mainNavController.popBackStack() })
                }

                composable(AppRoutes.FEEDBACK) {
                    FeedbackScreen(onBackClick = { mainNavController.popBackStack() })
                }

                composable(AppRoutes.DELETE_ACCOUNT) {
                    DeleteAccountScreen(onBackClick = { mainNavController.popBackStack() })
                }

                // --- 4. CART FLOW ---
                composable(AppRoutes.PAYMENT_QR) {
                    PaymentQRScreen(
                        onBackClick = { mainNavController.popBackStack() },
                        onPaymentSuccess = { mainNavController.navigate(AppRoutes.ORDER_SUCCESS) }
                    )
                }

                composable(AppRoutes.ORDER_SUCCESS) {
                    OrderSuccessScreen(
                        onTrackOrderClick = { mainNavController.navigate(AppRoutes.DELIVERY) },
                        onHomeClick = { mainNavController.navigate(AppRoutes.HOME) }
                    )
                }

                composable(AppRoutes.DELIVERY) {
                    DeliveryScreen(
                        onBackClick = { mainNavController.navigate(AppRoutes.ORDER_HISTORY) }
                    )
                }

                composable(AppRoutes.PAYMENT_METHODS) {
                    // PaymentMethodsScreen(onBackClick = { mainNavController.popBackStack() })
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