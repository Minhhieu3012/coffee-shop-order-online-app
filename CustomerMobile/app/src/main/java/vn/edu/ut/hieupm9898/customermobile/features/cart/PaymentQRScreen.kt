package vn.edu.ut.hieupm9898.customermobile.features.cart

import android.graphics.Bitmap
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter
import kotlinx.coroutines.launch
import vn.edu.ut.hieupm9898.customermobile.data.local.CartEntity
import vn.edu.ut.hieupm9898.customermobile.navigation.AppRoutes
import vn.edu.ut.hieupm9898.customermobile.ui.theme.BrosBackground
import vn.edu.ut.hieupm9898.customermobile.ui.theme.BrosBrown
import vn.edu.ut.hieupm9898.customermobile.ui.theme.BrosButton
import vn.edu.ut.hieupm9898.customermobile.ui.theme.BrosSubTitle
import vn.edu.ut.hieupm9898.customermobile.ui.theme.BrosTitle

// ========== QR CODE GENERATOR ==========
fun generateAmountQR(amount: Double): Bitmap? {
    return try {
        val qrContent = "AMOUNT:${amount.toInt()}"
        val writer = QRCodeWriter()
        val bitMatrix = writer.encode(qrContent, BarcodeFormat.QR_CODE, 512, 512)
        val width = bitMatrix.width
        val height = bitMatrix.height
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)

        for (x in 0 until width) {
            for (y in 0 until height) {
                bitmap.setPixel(
                    x,
                    y,
                    if (bitMatrix[x, y]) android.graphics.Color.BLACK
                    else android.graphics.Color.WHITE
                )
            }
        }
        bitmap
    } catch (e: WriterException) {
        e.printStackTrace()
        null
    }
}

// ========== COMPOSE SCREEN ==========
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentQRScreen(
    navController: NavController,
    viewModel: CartViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    // State từ ViewModel
    val cartItems by viewModel.cartItems.collectAsState()
    val totalPrice by viewModel.totalPrice.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()

    // Local state
    var qrBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var showQR by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Thanh toán",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.Default.ArrowBack,
                            "Quay lại",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BrosBrown
                )
            )
        },
        containerColor = BrosBackground
    ) { paddingValues ->

        // Kiểm tra giỏ hàng rỗng
        if (cartItems.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Giỏ hàng trống",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = BrosTitle
                    )
                    Button(
                        onClick = { navController.navigate(AppRoutes.HOME) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = BrosButton
                        )
                    ) {
                        Text("Về trang chủ")
                    }
                }
            }
            return@Scaffold
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // ========== THÔNG TIN KHÁCH HÀNG ==========
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Thông tin khách hàng",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = BrosTitle,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Tên:",
                                color = BrosSubTitle,
                                fontSize = 14.sp
                            )
                            Text(
                                text = currentUser?.displayName ?: "Khách hàng",
                                fontWeight = FontWeight.SemiBold,
                                color = BrosTitle,
                                fontSize = 14.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "SĐT:",
                                color = BrosSubTitle,
                                fontSize = 14.sp
                            )
                            Text(
                                text = currentUser?.phoneNumber ?: "Chưa cập nhật",
                                fontWeight = FontWeight.SemiBold,
                                color = BrosTitle,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }

            // ========== DANH SÁCH SẢN PHẨM ==========
            item {
                Text(
                    text = "Đơn hàng của bạn",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = BrosTitle
                )
            }

            items(cartItems) { item ->
                CartItemCard(item = item)
            }

            // ========== TỔNG TIỀN ==========
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = BrosBrown
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Tạm tính",
                                color = Color.White,
                                fontSize = 16.sp
                            )
                            Text(
                                text = "${totalPrice.toInt()}đ",
                                color = Color.White,
                                fontSize = 16.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Phí giao hàng",
                                color = Color.White,
                                fontSize = 16.sp
                            )
                            Text(
                                text = "15,000đ",
                                color = Color.White,
                                fontSize = 16.sp
                            )
                        }

                        Divider(
                            modifier = Modifier.padding(vertical = 12.dp),
                            color = Color.White.copy(alpha = 0.3f)
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Tổng cộng",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                            )
                            Text(
                                text = "${(totalPrice + 15000).toInt()}đ",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 22.sp
                            )
                        }
                    }
                }
            }

            // ========== NÚT TẠO QR ==========
            item {
                Button(
                    onClick = {
                        showQR = true
                        qrBitmap = generateAmountQR(totalPrice + 15000)
                        if (qrBitmap == null) {
                            Toast.makeText(
                                context,
                                "Lỗi tạo mã QR",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = BrosButton
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "🎫 Tạo mã QR thanh toán",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            // ========== HIỂN THỊ QR CODE ==========
            if (showQR && qrBitmap != null) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "📱 Quét mã để thanh toán",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = BrosTitle,
                                modifier = Modifier.padding(bottom = 16.dp)
                            )

                            Card(
                                modifier = Modifier.size(320.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color.White
                                ),
                                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                            ) {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Image(
                                        bitmap = qrBitmap!!.asImageBitmap(),
                                        contentDescription = "QR Code",
                                        modifier = Modifier.size(280.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = "Số tiền",
                                fontSize = 14.sp,
                                color = BrosSubTitle
                            )
                            Text(
                                text = "${(totalPrice + 15000).toInt()}đ",
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold,
                                color = BrosBrown
                            )
                        }
                    }
                }
            }

            // ========== NÚT ĐÃ THANH TOÁN ==========
            item {
                Button(
                    onClick = {
                        if (!showQR) {
                            Toast.makeText(
                                context,
                                "Vui lòng tạo mã QR trước",
                                Toast.LENGTH_SHORT
                            ).show()
                            return@Button
                        }

                        isLoading = true
                        scope.launch {
                            try {
                                val result = viewModel.createOrderFromCart()
                                isLoading = false

                                result.onSuccess { order ->
                                    Toast.makeText(
                                        context,
                                        "✅ Đặt hàng thành công!",
                                        Toast.LENGTH_LONG
                                    ).show()

                                    // Clear cart
                                    viewModel.clearCart()

                                    // Navigate to success screen
                                    navController.navigate(AppRoutes.ORDER_SUCCESS) {
                                        popUpTo(AppRoutes.CART) { inclusive = true }
                                    }
                                }.onFailure { exception ->
                                    Toast.makeText(
                                        context,
                                        "❌ Lỗi: ${exception.message}",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            } catch (e: Exception) {
                                isLoading = false
                                Toast.makeText(
                                    context,
                                    "❌ Lỗi: ${e.message}",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    enabled = !isLoading && showQR,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4CAF50),
                        disabledContainerColor = Color.Gray
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(
                            text = "✅ Đã thanh toán",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }

            // ========== HƯỚNG DẪN ==========
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFFFF9C4)
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "📌 Hướng dẫn thanh toán",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = BrosTitle,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Text(
                            text = "1. Nhấn nút 'Tạo mã QR' để tạo mã thanh toán\n" +
                                    "2. Đưa mã QR cho nhân viên quét hoặc quét trên ứng dụng ngân hàng\n" +
                                    "3. Sau khi chuyển khoản thành công, nhấn 'Đã thanh toán'\n" +
                                    "4. Đơn hàng sẽ được xác nhận và bắt đầu chuẩn bị",
                            fontSize = 14.sp,
                            lineHeight = 20.sp,
                            color = BrosTitle
                        )
                    }
                }
            }

            // Bottom spacing
            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

// ========== CART ITEM CARD COMPONENT ==========
@Composable
fun CartItemCard(item: CartEntity) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.productName,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color = BrosTitle
                )
                Text(
                    text = "Size: ${item.size}",
                    color = BrosSubTitle,
                    fontSize = 13.sp
                )
                if (item.notes.isNotEmpty()) {
                    Text(
                        text = "Ghi chú: ${item.notes}",
                        color = BrosSubTitle,
                        fontSize = 12.sp
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "SL: ${item.quantity}",
                    color = BrosTitle,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = "${item.price.toInt()}đ",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = BrosBrown
                )
                Text(
                    text = "= ${item.lineTotal.toInt()}đ",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = BrosButton
                )
            }
        }
    }
}