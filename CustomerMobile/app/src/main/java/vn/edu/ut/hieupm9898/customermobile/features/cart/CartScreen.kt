package vn.edu.ut.hieupm9898.customermobile.features.cart

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import coil.compose.AsyncImage
import coil.request.ImageRequest
import vn.edu.ut.hieupm9898.customermobile.data.local.CartEntity
import vn.edu.ut.hieupm9898.customermobile.navigation.AppRoutes
import vn.edu.ut.hieupm9898.customermobile.ui.components.BrosBottomNavBar
import vn.edu.ut.hieupm9898.customermobile.ui.components.BrosButton
import vn.edu.ut.hieupm9898.customermobile.ui.theme.BrosBackground
import vn.edu.ut.hieupm9898.customermobile.ui.theme.BrosBrown
import java.text.NumberFormat
import java.util.Locale

@Composable
fun CartScreen(
    navController: NavController,
    viewModel: CartViewModel = hiltViewModel()
) {
    val cartItems by viewModel.cartItems.collectAsState()
    val totalPrice by viewModel.totalPrice.collectAsState()

    CartScreenContent(
        cartItems = cartItems,
        totalPrice = totalPrice,
        onIncreaseClick = { item -> viewModel.increaseQuantity(item) },
        onDecreaseClick = { item -> viewModel.decreaseQuantity(item) },
        onRemoveItem = { item -> viewModel.removeItem(item) },
        onCheckoutClick = { navController.navigate(AppRoutes.PAYMENT_QR) },
        onGoHomeClick = {
            navController.navigate(AppRoutes.HOME) {
                popUpTo(AppRoutes.HOME) { inclusive = false }
                launchSingleTop = true
            }
        },
        // ✅ Logic điều hướng cho BottomNavBar
        onBottomNavClick = { route ->
            if (route != AppRoutes.CART) { // Không reload nếu đang ở Cart
                navController.navigate(route) {
                    // Quay về đích bắt đầu của graph để tránh chồng chất backstack
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreenContent(
    cartItems: List<CartEntity>,
    totalPrice: Double,
    onIncreaseClick: (CartEntity) -> Unit,
    onDecreaseClick: (CartEntity) -> Unit,
    onRemoveItem: (CartEntity) -> Unit,
    onCheckoutClick: () -> Unit,
    onGoHomeClick: () -> Unit,
    onBottomNavClick: (String) -> Unit // Callback cho NavBar
) {
    Scaffold(
        containerColor = BrosBackground,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Giỏ hàng",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.headlineMedium
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = BrosBackground
                )
            )
        },
        // ✅ THÊM BOTTOM NAV BAR VÀO ĐÂY
        bottomBar = {
            BrosBottomNavBar(
                currentRoute = AppRoutes.CART, // Đánh dấu tab Giỏ hàng đang active
                onNavigate = onBottomNavClick
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues) // Padding này tự động trừ chiều cao của TopBar VÀ BottomBar
        ) {
            if (cartItems.isEmpty()) {
                EmptyCartView(onGoHomeClick = onGoHomeClick)
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(
                        start = 16.dp,
                        end = 16.dp,
                        top = 12.dp,
                        // ✅ Padding bottom đủ lớn để không bị thanh "Tổng cộng" che mất item cuối
                        bottom = 120.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(cartItems) { item ->
                        CartItemRow(
                            item = item,
                            onIncreaseClick = { onIncreaseClick(item) },
                            onDecreaseClick = { onDecreaseClick(item) },
                            onDeleteClick = { onRemoveItem(item) }
                        )
                    }
                }
            }

            // ✅ THANH THANH TOÁN (TOTAL + CHECKOUT)
            if (cartItems.isNotEmpty()) {
                CartBottomBar(
                    totalPrice = totalPrice,
                    onCheckoutClick = onCheckoutClick,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        // ✅ Chỉ cần padding nhỏ để cách BottomNavBar một chút cho đẹp
                        .padding(bottom = 16.dp)
                )
            }
        }
    }
}

@Composable
fun EmptyCartView(
    onGoHomeClick: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(24.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ShoppingCart,
                contentDescription = null,
                modifier = Modifier.size(100.dp),
                tint = Color.LightGray.copy(alpha = 0.5f)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Giỏ hàng trống!",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = BrosBrown
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Bạn chưa chọn món nào.",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            BrosButton(
                text = "Khám phá Menu",
                onClick = onGoHomeClick,
                modifier = Modifier
                    .width(200.dp)
                    .height(50.dp)
            )
        }
    }
}

@Composable
fun CartItemRow(
    item: CartEntity,
    onIncreaseClick: () -> Unit,
    onDecreaseClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        shadowElevation = 2.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(item.productImage)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(75.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.LightGray)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.productName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )

                Text(
                    text = "${item.size}${if (item.notes.isNotEmpty()) " • ${item.notes}" else ""}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = formatPrice(item.price),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = BrosBrown
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    IconButton(
                        onClick = onDecreaseClick,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = BrosBrown.copy(alpha = 0.1f)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Remove,
                                contentDescription = "Decrease",
                                tint = BrosBrown,
                                modifier = Modifier.padding(4.dp)
                            )
                        }
                    }

                    Text(
                        text = item.quantity.toString(),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.widthIn(min = 24.dp),
                        textAlign = TextAlign.Center
                    )

                    IconButton(
                        onClick = onIncreaseClick,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = BrosBrown
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Increase",
                                tint = Color.White,
                                modifier = Modifier.padding(4.dp)
                            )
                        }
                    }
                }
            }

            IconButton(onClick = onDeleteClick) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Remove",
                    tint = Color.Gray.copy(alpha = 0.6f)
                )
            }
        }
    }
}

@Composable
fun CartBottomBar(
    totalPrice: Double,
    onCheckoutClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Surface tạo nền trắng và shadow cho phần thanh toán
    Surface(
        modifier = modifier.fillMaxWidth().padding(horizontal = 16.dp), // Thêm padding ngang cho đẹp
        shadowElevation = 8.dp,
        color = Color.White,
        shape = RoundedCornerShape(24.dp) // Bo tròn nguyên khối
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 16.dp)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Tổng cộng",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Gray
                )
                Text(
                    text = formatPrice(totalPrice),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = BrosBrown
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            BrosButton(
                text = "Thanh toán",
                onClick = onCheckoutClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            )
        }
    }
}

fun formatPrice(amount: Double): String {
    return NumberFormat.getCurrencyInstance(Locale("vi", "VN")).format(amount)
}