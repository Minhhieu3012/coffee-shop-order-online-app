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
import androidx.compose.material3.SnackbarDuration
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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
    val mainNavController = rememberNavController()

    val navBackStackEntry by mainNavController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val bottomBarRoutes = listOf(
        AppRoutes.HOME,
        AppRoutes.FAVORITE,
        AppRoutes.CART,
        AppRoutes.PROFILE
    )

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    CustomerMobileTheme {
        Scaffold(
            snackbarHost = {
                SnackbarHost(
                    hostState = snackbarHostState,
                    modifier = Modifier.padding(bottom = 80.dp)
                )
            },
            bottomBar = {
                if (currentRoute in bottomBarRoutes) {
                    BrosBottomNavBar(
                        currentRoute = currentRoute ?: AppRoutes.HOME,
                        onNavigate = { route ->
                            mainNavController.navigate(route) {
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
            // ✅ QUAN TRỌNG: Chỉ áp dụng paddingValues cho các màn hình KHÔNG PHẢI Cart và Favorite
            // Vì Cart và Favorite tự quản lý padding của riêng chúng

            NavHost(
                navController = mainNavController,
                startDestination = AppRoutes.HOME,
                modifier = Modifier.fillMaxSize()  // ✅ Loại bỏ padding ở đây
            ) {

                // HOME
                composable(AppRoutes.HOME) {
                    Box(modifier = Modifier.padding(paddingValues)) {  // ✅ Padding riêng cho Home
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
                }

                // FAVORITE - ✅ KHÔNG áp dụng paddingValues
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

                // CART - ✅ KHÔNG áp dụng paddingValues
                composable(AppRoutes.CART) {
                    CartScreen(
                        navController = mainNavController
                    )
                }

                // PROFILE
                composable(AppRoutes.PROFILE) {
                    Box(modifier = Modifier.padding(paddingValues)) {  // ✅ Padding riêng cho Profile
                        ProfileScreen(
                            navController = rootNavController,
                            onEditProfileClick = { rootNavController.navigate(AppRoutes.EDIT_PROFILE) },
                            onAddressClick = { rootNavController.navigate(AppRoutes.ADDRESS_LIST) },
                            onPaymentClick = { rootNavController.navigate(AppRoutes.PAYMENT_METHODS) },
                            onHistoryClick = { rootNavController.navigate(AppRoutes.ORDER_HISTORY) },
                            onNotificationsClick = { rootNavController.navigate(AppRoutes.NOTIFICATIONS) },
                            onBackClick = { }
                        )
                    }
                }

                // CHI TIẾT SẢN PHẨM
                composable(
                    route = AppRoutes.DETAIL,
                    arguments = listOf(navArgument("id") { type = NavType.StringType })
                ) { backStackEntry ->
                    val productId = backStackEntry.arguments?.getString("id") ?: ""

                    val viewModel: ProductDetailViewModel = hiltViewModel()
                    val uiState by viewModel.uiState.collectAsState()

                    LaunchedEffect(productId) {
                        viewModel.loadProduct(productId)
                    }

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
                                        val job = launch {
                                            snackbarHostState.showSnackbar(
                                                message = "Đã thêm ${product.name} vào giỏ hàng",
                                                duration = SnackbarDuration.Indefinite
                                            )
                                        }
                                        delay(1200)
                                        snackbarHostState.currentSnackbarData?.dismiss()
                                        job.cancel()
                                    }
                                },
                                onRelatedProductClick = { relatedProduct ->
                                    mainNavController.navigate("${AppRoutes.DETAIL_BASE}/${relatedProduct.id}")
                                }
                            )
                        }
                    }
                }

                // SEARCH
                composable(AppRoutes.SEARCH) {
                    SearchScreen(onBackClick = { mainNavController.popBackStack() })
                }

                // CART FLOW
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