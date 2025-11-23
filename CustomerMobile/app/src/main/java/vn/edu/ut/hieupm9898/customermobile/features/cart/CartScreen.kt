package vn.edu.ut.hieupm9898.customermobile.features.cart

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Delete
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
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import vn.edu.ut.hieupm9898.customermobile.data.local.CartEntity
import vn.edu.ut.hieupm9898.customermobile.navigation.AppRoutes
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
        }
    )
}

@Composable
fun CartScreenContent(
    cartItems: List<CartEntity>,
    totalPrice: Double,
    onIncreaseClick: (CartEntity) -> Unit,
    onDecreaseClick: (CartEntity) -> Unit,
    onRemoveItem: (CartEntity) -> Unit,
    onCheckoutClick: () -> Unit,
    onGoHomeClick: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(BrosBackground)
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Giỏ hàng",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = BrosBrown,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp)
            )

            if (cartItems.isEmpty()) {
                // ✅ Empty View nhưng vẫn chừa chỗ cho Bottom Nav
                EmptyCartView(onGoHomeClick = onGoHomeClick)
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(
                        start = 20.dp,
                        end = 20.dp,
                        top = 10.dp,
                        bottom = 200.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
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
        }

        if (cartItems.isNotEmpty()) {
            CartBottomBar(
                totalPrice = totalPrice,
                onCheckoutClick = onCheckoutClick,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 80.dp)
            )
        }
    }
}

@Composable
fun EmptyCartView(
    onGoHomeClick: () -> Unit
) {
    // ✅ Sử dụng Box để căn giữa nội dung nhưng vẫn đảm bảo layout không đè lên BottomBar
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 80.dp), // Chừa chỗ cho Navbar
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(24.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ShoppingCart,
                contentDescription = null,
                modifier = Modifier.size(120.dp),
                tint = Color.LightGray.copy(alpha = 0.5f)
            )

            Spacer(modifier = Modifier.height(24.dp))

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

            Spacer(modifier = Modifier.height(32.dp))

            // ✅ Nút bấm vẫn giữ lại như một Call-to-action (CTA) chính
            BrosButton(
                text = "Khám phá Menu",
                onClick = onGoHomeClick,
                modifier = Modifier
                    .width(200.dp)
                    .height(50.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // ✅ Dòng text gợi ý Navbar (như bạn yêu cầu về trải nghiệm)
            Text(
                text = "hoặc chọn mục Trang chủ bên dưới",
                style = MaterialTheme.typography.bodySmall,
                color = Color.LightGray
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
                    .size(80.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.LightGray)
            )

            Spacer(modifier = Modifier.width(16.dp))

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

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = formatPrice(item.price),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = BrosBrown
                )

                Spacer(modifier = Modifier.height(12.dp))

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
    Surface(
        modifier = modifier.fillMaxWidth(),
        shadowElevation = 16.dp,
        color = Color.White,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 24.dp, vertical = 16.dp)
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

            Spacer(modifier = Modifier.height(16.dp))

            BrosButton(
                text = "Thanh toán",
                onClick = onCheckoutClick,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

fun formatPrice(amount: Double): String {
    return NumberFormat.getCurrencyInstance(Locale("vi", "VN")).format(amount)
}