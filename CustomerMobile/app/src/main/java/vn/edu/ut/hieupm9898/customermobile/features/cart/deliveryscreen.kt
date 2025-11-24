package vn.edu.ut.hieupm9898.customermobile.features.cart

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import vn.edu.ut.hieupm9898.customermobile.navigation.AppRoutes
import vn.edu.ut.hieupm9898.customermobile.ui.theme.BrosBackground
import vn.edu.ut.hieupm9898.customermobile.ui.theme.BrosButton
import vn.edu.ut.hieupm9898.customermobile.ui.theme.BrosTitle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeliveryScreen(
    navController: NavController
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Theo Dõi Đơn Hàng", color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        containerColor = BrosBackground
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            // Icon (Giả định vị trí bản đồ/shipper)
            Text(
                text = "🚚",
                fontSize = 80.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Đơn hàng của bạn đang trên đường",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = BrosTitle,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Tài xế đang di chuyển đến địa chỉ của bạn. Thời gian dự kiến: 20 phút.",
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Khu vực hiển thị Bản đồ (Tạm thời là Card)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                    Text("📍 Bản đồ và Trạng thái theo dõi tại đây", color = BrosTitle)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Nút "Về Trang Chủ"
            Button(
                onClick = {
                    // Quay về trang chủ (hoặc chuyển sang màn hình chính)
                    navController.popBackStack(AppRoutes.HOME, inclusive = false)
                    navController.navigate(AppRoutes.HOME) {
                        popUpTo(AppRoutes.HOME) { inclusive = true }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = BrosButton)
            ) {
                Text("Về Trang Chủ", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}