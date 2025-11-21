package vn.edu.ut.hieupm9898.customermobile.features.cart

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import vn.edu.ut.hieupm9898.customermobile.data.model.CartItem
import vn.edu.ut.hieupm9898.customermobile.data.model.Product
import vn.edu.ut.hieupm9898.customermobile.ui.components.BrosButton
import java.text.NumberFormat
import java.util.Locale

// Màu chủ đạo
private val CoffeeBrown = Color(0xFF6F4E37)
private val BackgroundColor = Color(0xFFF5F1E8)

/**
 * 1. Màn hình Stateful (Có logic)
 * Chịu trách nhiệm kết nối ViewModel và gọi Content hiển thị
 */
@Composable
fun CartScreen(
    navController: NavController,
    viewModel: CartViewModel = hiltViewModel()
) {
    val cartItems by viewModel.cartItems.collectAsState()
    val totalPrice by viewModel.totalPrice.collectAsState()

    // Gọi phần giao diện tĩnh và truyền dữ liệu vào
    CartScreenContent(
        cartItems = cartItems,
        totalPrice = totalPrice,
        onRemoveItem = { item -> viewModel.removeItem(item) },
        onCheckoutClick = { navController.navigate("checkout") }
    )
}

/**
 * 2. Màn hình Stateless (Chỉ giao diện)
 * Tách ra để có thể Preview dễ dàng mà không cần chạy App
 */
@Composable
fun CartScreenContent(
    cartItems: List<CartItem>,
    totalPrice: Double,
    onRemoveItem: (CartItem) -> Unit,
    onCheckoutClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
            .padding(top = 16.dp)
    ) {

        Spacer(modifier = Modifier.height(20.dp))
        // Header
        Text(
            text = "Đơn hàng của tôi",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = CoffeeBrown,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp)
        )

        if (cartItems.isEmpty()) {
            EmptyCartView()
        } else {
            Column(modifier = Modifier.fillMaxSize()) {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 10.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(cartItems) { item ->
                        CartItemRow(
                            item = item,
                            onDeleteClick = { onRemoveItem(item) }
                        )
                    }
                }

                CartBottomBar(
                    totalPrice = totalPrice,
                    onCheckoutClick = onCheckoutClick
                )
            }
        }
    }
}

// --- CÁC COMPONENT UI ---

@Composable
fun CartItemRow(item: CartItem, onDeleteClick: () -> Unit) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        shadowElevation = 2.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(12.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(if (item.product.imageUrl.isNotEmpty()) item.product.imageUrl else item.product.imageRes)
                    .crossfade(true).build(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(80.dp).clip(RoundedCornerShape(12.dp)).background(Color.LightGray)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = item.product.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, maxLines = 1)
                Text(text = "${formatPrice(item.product.price)} x ${item.quantity}", style = MaterialTheme.typography.bodyMedium, color = CoffeeBrown, fontWeight = FontWeight.SemiBold)
            }
            IconButton(onClick = onDeleteClick) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = "Remove", tint = Color.Gray.copy(alpha = 0.6f))
            }
        }
    }
}

@Composable
fun CartBottomBar(totalPrice: Double, onCheckoutClick: () -> Unit) {
    Surface(
        shadowElevation = 16.dp,
        color = Color.White,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(modifier = Modifier.padding(24.dp).fillMaxWidth()) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Total Amount", style = MaterialTheme.typography.bodyLarge, color = Color.Gray)
                Text(text = formatPrice(totalPrice), style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = CoffeeBrown)
            }
            Spacer(modifier = Modifier.height(16.dp))
            BrosButton(text = "Checkout", onClick = onCheckoutClick, modifier = Modifier.fillMaxWidth())
        }
    }
}

@Composable
fun EmptyCartView() {
    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(imageVector = Icons.Default.ShoppingCart, contentDescription = null, modifier = Modifier.size(100.dp), tint = Color.LightGray)
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Giỏ hàng trống!", style = MaterialTheme.typography.titleLarge, color = Color.Gray)
    }
}

fun formatPrice(amount: Double): String {
    return NumberFormat.getCurrencyInstance(Locale("vi", "VN")).format(amount)
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewCartScreen_Default() {
    CartScreenContent(
        cartItems = emptyList(), // <--- Truyền danh sách rỗng
        totalPrice = 0.0,
        onRemoveItem = {},
        onCheckoutClick = {}
    )
}