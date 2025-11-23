package vn.edu.ut.hieupm9898.customermobile.features.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import kotlinx.coroutines.launch

// --- IMPORT FEATURES ---
import vn.edu.ut.hieupm9898.customermobile.features.cart.*
import vn.edu.ut.hieupm9898.customermobile.features.favorite.FavoriteScreen
import vn.edu.ut.hieupm9898.customermobile.features.home.*
import vn.edu.ut.hieupm9898.customermobile.features.product_detail.ProductDetailScreen
import vn.edu.ut.hieupm9898.customermobile.features.product_detail.ProductDetailViewModel
import vn.edu.ut.hieupm9898.customermobile.features.product_detail.RelatedProduct
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

    // ✅ CÁC ROUTE HIỂN THỊ BOTTOM NAV BAR
    val bottomBarRoutes = listOf(
        AppRoutes.HOME,
        AppRoutes.FAVORITE,
        AppRoutes.CART,
        AppRoutes.PROFILE
    )

    // ✅ KHỞI TẠO SNACKBAR HOST STATE
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    CustomerMobileTheme {
        Scaffold(
            // ✅ SNACKBAR HOST
            snackbarHost = {
                SnackbarHost(
                    hostState = snackbarHostState,
                    modifier = Modifier.padding(bottom = 80.dp) // Tránh bị BottomBar che
                )
            },
            // ✅ BOTTOM NAV BAR - LUÔN HIỂN THỊ Ở CÁC TAB CHÍNH
            bottomBar = {
                if (currentRoute in bottomBarRoutes) {
                    BrosBottomNavBar(
                        currentRoute = currentRoute ?: AppRoutes.HOME,
                        onNavigate = { route ->
                            mainNavController.navigate(route) {
                                // ✅ ĐẢM BẢO NAVIGATE MƯỢT MÀ, KHÔNG BỊ DUPLICATE
                                popUpTo(mainNavController.graph.findStartDestination().id) {
                                    saveState = true
                                }
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

                // ========================================
                // 1. CÁC TAB CHÍNH (MAIN TABS)
                // ========================================

                // ✅ HOME SCREEN
                composable(AppRoutes.HOME) {
                    HomeScreen(
                        onProductClick = { id ->
                            mainNavController.navigate("${AppRoutes.DETAIL_BASE}/$id")
                        },
                        onSearchClick = {
                            mainNavController.navigate(AppRoutes.SEARCH)
                        },
                        onNavigateToCart = {
                            mainNavController.navigate(AppRoutes.CART) {
                                launchSingleTop = true
                            }
                        }
                    )
                }

                // ✅ FAVORITE SCREEN
                composable(AppRoutes.FAVORITE) {
                    FavoriteScreen(
                        onProductClick = { id ->
                            mainNavController.navigate("${AppRoutes.DETAIL_BASE}/$id")
                        },
                        onGoHomeClick = {
                            mainNavController.navigate(AppRoutes.HOME) {
                                popUpTo(AppRoutes.HOME) { inclusive = false }
                                launchSingleTop = true
                            }
                        },
                        onNavigateToCart = {
                            mainNavController.navigate(AppRoutes.CART) {
                                launchSingleTop = true
                            }
                        }
                    )
                }

                // ✅ CART SCREEN - LUÔN CHO PHÉP NAVIGATE RA NGOÀI
                composable(AppRoutes.CART) {
                    CartScreen(
                        navController = mainNavController
                    )
                }

                // ✅ PROFILE SCREEN
                composable(AppRoutes.PROFILE) {
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

                // ========================================
                // 2. CHI TIẾT SẢN PHẨM
                // ========================================

                composable(
                    route = AppRoutes.DETAIL,
                    arguments = listOf(navArgument("id") { type = NavType.StringType })
                ) { backStackEntry ->
                    val productId = backStackEntry.arguments?.getString("id") ?: ""

                    // ViewModel
                    val viewModel: ProductDetailViewModel = hiltViewModel()
                    val uiState by viewModel.uiState.collectAsState()

                    // Load product khi vào màn hình
                    LaunchedEffect(productId) {
                        viewModel.loadProduct(productId)
                    }

                    // Hiển thị loading hoặc error
                    when {
                        uiState.isLoading -> {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }

                        uiState.errorMessage != null -> {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("Lỗi: ${uiState.errorMessage}")
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Button(onClick = { viewModel.retry(productId) }) {
                                        Text("Thử lại")
                                    }
                                }
                            }
                        }

                        uiState.product != null -> {
                            val product = uiState.product!!

                            // Convert related products sang RelatedProduct
                            val relatedProducts = uiState.relatedProducts.map { p ->
                                RelatedProduct(
                                    id = p.id,
                                    name = p.name,
                                    subtitle = p.description,
                                    price = "${p.price.toInt()}đ",
                                    imageUrl = p.imageUrl
                                )
                            }

                            ProductDetailScreen(
                                title = product.name,
                                subtitle = product.description,
                                rating = 4.5f,
                                ratingCountText = "(4.5)",
                                description = product.description,
                                imageUrl = product.imageUrl,
                                isFavorite = uiState.isFavorite,
                                availableSizes = listOf("Nhỏ", "Trung bình", "Lớn"),
                                selectedSize = uiState.selectedSize,
                                availableDairy = listOf(
                                    "Whole Milk" to 0.0,
                                    "Almond Milk" to 5000.0,
                                    "Oat Milk" to 7000.0
                                ),
                                selectedDairy = uiState.selectedDairy,
                                relatedProducts = relatedProducts,
                                onBackClick = { mainNavController.popBackStack() },
                                onFavoriteClick = { viewModel.toggleFavorite() },
                                onSizeSelected = { viewModel.selectSize(it) },
                                onDairySelected = { viewModel.selectDairy(it) },
                                onAddToCartClick = {
                                    viewModel.addToCart()

                                    coroutineScope.launch {
                                        val result = snackbarHostState.showSnackbar(
                                            message = "✅ Đã thêm ${product.name} vào giỏ hàng",
                                            actionLabel = "Xem giỏ"
                                        )

                                        if (result == androidx.compose.material3.SnackbarResult.ActionPerformed) {
                                            mainNavController.navigate(AppRoutes.CART) {
                                                launchSingleTop = true
                                            }
                                        } else {
                                            mainNavController.popBackStack()
                                        }
                                    }
                                },
                                onRelatedProductClick = { relatedProduct ->
                                    mainNavController.navigate("${AppRoutes.DETAIL_BASE}/${relatedProduct.id}")
                                }
                            )
                        }
                    }
                }

                // ========================================
                // 3. CÁC MÀN HÌNH PHỤ
                // ========================================

                composable(AppRoutes.SEARCH) {
                    SearchScreen(onBackClick = { mainNavController.popBackStack() })
                }

                // ========================================
                // 4. CART FLOW
                // ========================================

                composable(AppRoutes.PAYMENT_QR) {
                    PaymentQRScreen(
                        onBackClick = { mainNavController.popBackStack() },
                        onPaymentSuccess = {
                            mainNavController.navigate(AppRoutes.ORDER_SUCCESS) {
                                popUpTo(AppRoutes.CART) { inclusive = true }
                            }
                        }
                    )
                }

                composable(AppRoutes.ORDER_SUCCESS) {
                    OrderSuccessScreen(
                        onTrackOrderClick = {
                            mainNavController.navigate(AppRoutes.DELIVERY)
                        },
                        onHomeClick = {
                            mainNavController.navigate(AppRoutes.HOME) {
                                popUpTo(AppRoutes.HOME) { inclusive = true }
                            }
                        }
                    )
                }

                composable(AppRoutes.DELIVERY) {
                    DeliveryScreen(
                        onBackClick = {
                            mainNavController.navigate(AppRoutes.HOME) {
                                popUpTo(AppRoutes.HOME) { inclusive = true }
                            }
                        }
                    )
                }
            }
        }
    }
}