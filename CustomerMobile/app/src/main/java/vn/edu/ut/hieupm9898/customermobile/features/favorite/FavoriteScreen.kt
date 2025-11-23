package vn.edu.ut.hieupm9898.customermobile.features.favorite

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import vn.edu.ut.hieupm9898.customermobile.ui.components.CoffeeCard
import vn.edu.ut.hieupm9898.customermobile.ui.components.EmptyStateScreen
import vn.edu.ut.hieupm9898.customermobile.ui.theme.BrosBackground
import vn.edu.ut.hieupm9898.customermobile.ui.theme.BrosBrown

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteScreen(
    onProductClick: (String) -> Unit = {},
    onGoHomeClick: () -> Unit = {},
    onNavigateToCart: () -> Unit = {},
    viewModel: FavoriteViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.loadFavoriteProducts()
    }

    // Handle add to cart notification
    LaunchedEffect(uiState.addToCartMessage) {
        if (uiState.addToCartMessage != null) {
            val result = snackbarHostState.showSnackbar(
                message = uiState.addToCartMessage!!,
                actionLabel = if (uiState.addToCartSuccess) "Xem giỏ" else null,
                duration = SnackbarDuration.Short
            )
            if (result == SnackbarResult.ActionPerformed && uiState.addToCartSuccess) {
                onNavigateToCart()
            }
            viewModel.resetCartNotification()
        }
    }

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let { msg ->
            if (!msg.contains("giỏ hàng")) {
                snackbarHostState.showSnackbar(
                    message = msg,
                    actionLabel = "Thử lại",
                    duration = SnackbarDuration.Short
                )
            }
            viewModel.clearError()
        }
    }

    Scaffold(
        containerColor = BrosBackground,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Yêu thích",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.headlineMedium
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = BrosBackground
                )
            )
        },
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.padding(bottom = 80.dp)
            ) { snackbarData ->
                Snackbar(
                    snackbarData = snackbarData,
                    containerColor = if (uiState.addToCartSuccess) BrosBrown else MaterialTheme.colorScheme.error,
                    contentColor = Color.White,
                    actionColor = Color.White
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                uiState.favoriteProducts.isEmpty() -> {
                    EmptyStateScreen(
                        title = "Chưa có món yêu thích!",
                        message = "Hãy khám phá và đánh dấu những món đồ uống yêu thích của bạn.",
                        buttonText = "Khám phá ngay",
                        onClick = onGoHomeClick
                    )
                }

                else -> {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 20.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        contentPadding = PaddingValues(top = 20.dp, bottom = 100.dp)
                    ) {
                        items(items = uiState.favoriteProducts, key = { it.id }) { product ->

                            // ✅ Tính toán trạng thái hết hàng
                            val isOutOfStock = product.isOutOfStock()

                            CoffeeCard(
                                title = product.name,
                                subtitle = product.description,
                                price = product.price,
                                imageUrl = product.imageUrl,
                                isFavorite = true,
                                // ✅ Truyền trạng thái Hết hàng vào UI
                                isOutOfStock = isOutOfStock,
                                onCardClick = {
                                    // ✅ Chỉ cho click nếu còn hàng
                                    if (!isOutOfStock) onProductClick(product.id)
                                },
                                onFavoriteClick = { viewModel.toggleFavorite(product.id) },
                                // ✅ Thêm vào giỏ (Logic VM đã check OOS)
                                onAddClick = { viewModel.quickAddToCart(product) }
                            )
                        }
                    }
                }
            }
        }
    }
}