package vn.edu.ut.hieupm9898.customermobile.features.cart

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import vn.edu.ut.hieupm9898.customermobile.navigation.AppRoutes
import vn.edu.ut.hieupm9898.customermobile.ui.theme.BrosBackground
import vn.edu.ut.hieupm9898.customermobile.ui.theme.BrosBrown
import vn.edu.ut.hieupm9898.customermobile.ui.theme.BrosButton
import vn.edu.ut.hieupm9898.customermobile.ui.theme.BrosSubTitle
import vn.edu.ut.hieupm9898.customermobile.ui.theme.BrosTitle

@Composable
fun OrderSuccessScreen(
    navController: NavController
) {
    // Animation effect
    LaunchedEffect(Unit) {
        delay(100)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BrosBackground),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Success Icon
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .background(
                        color = Color(0xFF4CAF50),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Success",
                    modifier = Modifier.size(80.dp),
                    tint = Color.White
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Success Title
            Text(
                text = "Đặt hàng thành công!",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = BrosTitle,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Success Message
            Text(
                text = "Cảm ơn bạn đã đặt hàng.\nĐơn hàng của bạn đang được xử lý.",
                fontSize = 16.sp,
                color = BrosSubTitle,
                textAlign = TextAlign.Center,
                lineHeight = 24.sp
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Order Details Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        text = "📦 Thông tin đơn hàng",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = BrosTitle,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    InfoRow(
                        label = "Trạng thái",
                        value = "Chờ xác nhận"
                    )
                    InfoRow(
                        label = "Thanh toán",
                        value = "Đã thanh toán"
                    )
                    InfoRow(
                        label = "Giao hàng",
                        value = "Giao tận nơi"
                    )
                    InfoRow(
                        label = "Thời gian dự kiến",
                        value = "20-30 phút"
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Track Order Button
            Button(
                onClick = {
                    navController.navigate(AppRoutes.ORDER_HISTORY) {
                        popUpTo(AppRoutes.HOME) { inclusive = false }
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
                Icon(
                    imageVector = Icons.Default.Receipt,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Xem đơn hàng",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Back to Home Button
            OutlinedButton(
                onClick = {
                    navController.navigate(AppRoutes.HOME) {
                        popUpTo(AppRoutes.HOME) { inclusive = true }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = BrosBrown
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Về trang chủ",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun InfoRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontSize = 15.sp,
            color = BrosSubTitle
        )
        Text(
            text = value,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
            color = BrosTitle
        )
    }
}