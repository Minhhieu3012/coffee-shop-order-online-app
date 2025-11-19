package vn.edu.ut.hieupm9898.customermobile.features.cart

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.GpsFixed
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import vn.edu.ut.hieupm9898.customermobile.ui.theme.CustomerMobileTheme

@Composable
fun DeliveryScreen(
    onBackClick: () -> Unit = {}
) {
    // Box chứa toàn bộ màn hình
    Box(modifier = Modifier.fillMaxSize()) {
        // 1. Bản đồ nền (Giả lập bằng ảnh)
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data("https://media.wired.com/photos/59269cd37034dc5f91becd64/master/pass/GoogleMapTA.jpg")
                .crossfade(true)
                .build(),
            contentDescription = "Map Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // 2. Các nút điều khiển phía trên (Back, GPS)
        DeliveryTopControls(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .statusBarsPadding(), // Tránh bị tai thỏ che
            onBackClick = onBackClick
        )

        // 3. Thông tin giao hàng (Bottom Sheet)
        DeliveryBottomSheet(
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
fun DeliveryTopControls(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 24.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Nút Back (Sử dụng Surface để có bóng đổ chuẩn Material)
        Surface(
            onClick = onBackClick,
            shape = RoundedCornerShape(14.dp),
            color = Color.White,
            shadowElevation = 4.dp,
            modifier = Modifier.size(48.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        // Nút GPS
        Surface(
            onClick = { /* Center Map logic */ },
            shape = RoundedCornerShape(14.dp),
            color = Color.White,
            shadowElevation = 4.dp,
            modifier = Modifier.size(48.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Default.GpsFixed,
                    contentDescription = "GPS",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Composable
fun DeliveryBottomSheet(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        color = MaterialTheme.colorScheme.background, // Nền màu Be/Trắng từ Theme
        shadowElevation = 16.dp
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Handle bar (thanh ngang để kéo)
            Box(
                modifier = Modifier
                    .width(40.dp)
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Thời gian dự kiến
            Text(
                text = "10 minutes left",
                style = MaterialTheme.typography.headlineSmall, // Font to đậm
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Text(
                text = "Delivery to Jl. Kpg Sutoyo",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Thanh tiến trình (Progress Bar)
            DeliveryProgressBar()

            Spacer(modifier = Modifier.height(24.dp))

            // Card thông tin tài xế
            DriverInfoCard()
        }
    }
}

@Composable
fun DeliveryProgressBar() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Logic: 4 bước, 3 đường nối
        // Bước 1, 2, 3: Active
        DeliveryStep(active = true)
        DeliveryLine(active = true)
        DeliveryStep(active = true)
        DeliveryLine(active = true)
        DeliveryStep(active = true)

        // Bước 4: Inactive
        DeliveryLine(active = false)
        DeliveryStep(active = false)
    }
}

@Composable
fun DeliveryStep(active: Boolean) {
    // Sử dụng màu Primary (Nâu) nếu Active, màu Outline nếu Inactive
    val color = if (active) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
    Box(
        modifier = Modifier
            .size(12.dp)
            .clip(CircleShape)
            .background(color)
    )
}

@Composable
fun DeliveryLine(active: Boolean) {
    val color = if (active) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
    Box(
        modifier = Modifier
            .width(40.dp) // Độ dài đường nối
            .height(2.dp) // Độ dày
            .background(color)
    )
}

@Composable
fun DriverInfoCard() {
    // Card chứa thông tin tài xế, có viền bao quanh
    Surface(
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)),
        color = MaterialTheme.colorScheme.surface,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar Tài xế
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data("https://img.freepik.com/free-photo/portrait-handsome-smiling-young-man-model-wearing-casual-summer-pink-clothes-fashion-stylish-man-posing_158538-5350.jpg") // Ảnh mẫu
                    .crossfade(true)
                    .build(),
                contentDescription = "Driver Avatar",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.secondaryContainer)
            )

            Spacer(modifier = Modifier.width(14.dp))

            // Tên & Chức vụ
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Johan Hawn",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Personal Courier",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.secondary
                )
            }

            // Nút Gọi điện
            IconButton(
                onClick = { /* Call Driver */ },
                modifier = Modifier
                    .size(48.dp)
                    .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
            ) {
                Icon(
                    imageVector = Icons.Default.Call,
                    contentDescription = "Call Driver",
                    tint = MaterialTheme.colorScheme.primary // Icon màu Nâu chủ đạo
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DeliveryScreenPreviewIntegrated() {
    CustomerMobileTheme {
        DeliveryScreen()
    }
}