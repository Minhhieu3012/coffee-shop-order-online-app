package vn.edu.ut.hieupm9898.customermobile.features.cart

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.NoteAdd
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import vn.edu.ut.hieupm9898.customermobile.ui.components.BrosButton
import vn.edu.ut.hieupm9898.customermobile.ui.components.CheckoutItemCard
import vn.edu.ut.hieupm9898.customermobile.ui.theme.CustomerMobileTheme
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderScreen(
    onBackClick: () -> Unit = {},
    onOrderClick: () -> Unit = {}
) {
    // Dùng Scaffold để dựng khung chuẩn Material Design
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background, // Màu nền từ Theme (BrosBackground)
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Order",
                        style = MaterialTheme.typography.headlineMedium // Font chuẩn từ Theme
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        bottomBar = {
            OrderBottomBar(
                totalPrice = 155000.0, // Ví dụ tổng tiền
                onOrderClick = onOrderClick
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp)
        ) {
            // 1. Toggle chuyển đổi Delivery / Pick Up
            item {
                DeliveryToggle()
                Spacer(modifier = Modifier.height(24.dp))
            }

            // 2. Địa chỉ nhận hàng
            item {
                Text(
                    text = "Delivery Address",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(12.dp))
                AddressSection()
                Divider(
                    modifier = Modifier.padding(vertical = 20.dp),
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
                )
            }

            // 3. Danh sách món (Tích hợp CheckoutItemCard)
            item {
                // Giả lập state số lượng cho món 1
                var qty1 by remember { mutableIntStateOf(1) }
                CheckoutItemCard(
                    title = "Cappuccino",
                    subtitle1 = "with Chocolate",
                    subtitle2 = "Medium Size",
                    price = 45000.0,
                    imageUrl = "https://img.freepik.com/free-photo/cup-coffee-with-heart-drawn-foam_1286-70.jpg", // Link ảnh mẫu
                    quantity = qty1,
                    onIncrementClick = { qty1++ },
                    onDecrementClick = { if (qty1 > 1) qty1-- },
                    onDeleteClick = { /* Xử lý xóa */ }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Giả lập món 2
                var qty2 by remember { mutableIntStateOf(2) }
                CheckoutItemCard(
                    title = "Cold Brew",
                    subtitle1 = "Low sugar",
                    subtitle2 = "Large Size",
                    price = 55000.0,
                    imageUrl = "https://img.freepik.com/free-photo/ice-coffee-tall-glass-with-cream-poured-over_140725-7254.jpg",
                    quantity = qty2,
                    onIncrementClick = { qty2++ },
                    onDecrementClick = { if (qty2 > 1) qty2-- },
                    onDeleteClick = { /* Xử lý xóa */ }
                )

                Spacer(modifier = Modifier.height(24.dp))
            }

            // 4. Tóm tắt thanh toán
            item {
                PaymentSummary(
                    subTotal = 155000.0,
                    deliveryFee = 15000.0,
                    discount = 15000.0
                )
                Spacer(modifier = Modifier.height(100.dp)) // Khoảng trống để không bị BottomBar che
            }
        }
    }
}

@Composable
fun DeliveryToggle() {
    var selectedOption by remember { mutableStateOf("Deliver") }
    val primaryColor = MaterialTheme.colorScheme.primary // Màu nâu (BrosButton)
    val surfaceColor = MaterialTheme.colorScheme.onPrimary // Màu chữ trên nền nâu

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Color(0xFFEDEDED), // Màu xám nhẹ cho nền toggle
                shape = RoundedCornerShape(14.dp)
            )
            .padding(4.dp)
    ) {
        val options = listOf("Deliver", "Pick Up")
        options.forEach { option ->
            val isSelected = option == selectedOption
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(10.dp))
                    // Nếu chọn -> Màu nâu, không chọn -> Trong suốt
                    .background(if (isSelected) primaryColor else Color.Transparent)
                    .clickable { selectedOption = option }
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = option,
                    style = MaterialTheme.typography.labelLarge,
                    color = if (isSelected) surfaceColor else MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Composable
fun AddressSection() {
    Column {
        Text(
            text = "KTX Khu B, ĐHQG TP.HCM",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = "Đường Tô Vĩnh Diện, Đông Hòa, Dĩ An, Bình Dương",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.secondary
        )
        Spacer(modifier = Modifier.height(12.dp))
        Row {
            // Nút Edit Address
            OutlinedButton(
                onClick = {},
                shape = RoundedCornerShape(20.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Edit Address",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Nút Add Note
            OutlinedButton(
                onClick = {},
                shape = RoundedCornerShape(20.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
            ) {
                Icon(
                    imageVector = Icons.Default.NoteAdd,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Add Note",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Composable
fun PaymentSummary(subTotal: Double, deliveryFee: Double, discount: Double) {
    val total = subTotal + deliveryFee - discount
    val formatter = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))

    Text("Payment Summary", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
    Spacer(modifier = Modifier.height(12.dp))

    // Tiền hàng
    Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
        Text("Price", style = MaterialTheme.typography.bodyMedium)
        Text(formatter.format(subTotal), style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
    }
    Spacer(modifier = Modifier.height(8.dp))

    // Phí ship
    Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
        Text("Delivery Fee", style = MaterialTheme.typography.bodyMedium)
        Row {
            Text(
                formatter.format(deliveryFee),
                style = MaterialTheme.typography.bodyMedium,
                textDecoration = androidx.compose.ui.text.style.TextDecoration.LineThrough,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                formatter.format(2000.0), // Giả sử giảm ship
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
        }
    }

    Divider(Modifier.padding(vertical = 16.dp))

    // Tổng cộng
    Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
        Text("Total Payment", style = MaterialTheme.typography.titleMedium)
        Text(
            formatter.format(total),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary, // Màu nâu nhấn mạnh
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun OrderBottomBar(totalPrice: Double, onOrderClick: () -> Unit) {
    // Surface tạo bóng đổ phía trên thanh bottom bar
    Surface(
        shadowElevation = 20.dp,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        color = Color.White // Nền trắng để nổi bật nút
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Có thể thêm icon Ví tiền hoặc Tiền mặt ở đây nếu muốn
                Text("Cash/Wallet", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
                Text(
                    NumberFormat.getCurrencyInstance(Locale("vi", "VN")).format(totalPrice),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            }

            // Tích hợp BrosButton
            BrosButton(
                text = "Order",
                onClick = onOrderClick,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

// --- PREVIEW ---
@Preview(showBackground = true)
@Composable
fun OrderScreenPreviewIntegrated() {
    // Bọc trong CustomerMobileTheme để thấy đúng màu sắc
    CustomerMobileTheme {
        OrderScreen()
    }
}