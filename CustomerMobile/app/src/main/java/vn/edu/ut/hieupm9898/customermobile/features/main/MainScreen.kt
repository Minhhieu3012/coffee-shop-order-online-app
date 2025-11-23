package vn.edu.ut.hieupm9898.customermobile.features.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
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
    rootNavController: NavHostController
) {
    // NavController này chỉ quản lý các tab con (Home, Cart, Profile...)
    val mainNavController = rememberNavController()

    val navBackStackEntry by mainNavController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val bottomBarRoutes = listOf(AppRoutes.HOME, AppRoutes.FAVORITE, AppRoutes.CART, AppRoutes.PROFILE)

    CustomerMobileTheme {
        Scaffold(
            bottomBar = {
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
                    // 👇 TRUYỀN rootNavController VÀO ĐỂ NAVIGATE ĐẾN CÁC PROFILE SUB-SCREENS
                    ProfileScreen(
                        navController = rootNavController,
                        onEditProfileClick = { rootNavController.navigate(AppRoutes.EDIT_PROFILE) },
                        onAddressClick = { rootNavController.navigate(AppRoutes.ADDRESS_LIST) },
                        onPaymentClick = { rootNavController.navigate(AppRoutes.PAYMENT_METHODS) },
                        onHistoryClick = { rootNavController.navigate(AppRoutes.ORDER_HISTORY) },
                        onNotificationsClick = { rootNavController.navigate(AppRoutes.NOTIFICATIONS) },
                        onBackClick = { } // Profile không có nút back
                    )
                }

                // --- 2. CHI TIẾT SẢN PHẨM ---
                composable(
                    route = AppRoutes.DETAIL,
                    arguments = listOf(navArgument("id") { type = NavType.StringType })
                ) { backStackEntry ->
                    val productId = backStackEntry.arguments?.getString("id") ?: ""

                    ProductDetailScreen(
                        title = "Cold Coffee",
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

                // 👇 XÓA TẤT CẢ CÁC PROFILE SUB-SCREENS Ở ĐÂY
                // Vì chúng đã được khai báo trong ProfileNavGraph

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
            }
        }
    }
}