package vn.edu.ut.hieupm9898.customermobile.features.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
fun StoreLocatorScreen(
    onBackClick: () -> Unit = {},
    onStoreSelected: (String) -> Unit = {}
) {
    Box(modifier = Modifier.fillMaxSize()) {
        // 1. MAP BACKGROUND (Giả lập bản đồ bằng ảnh)
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data("https://media.wired.com/photos/59269cd37034dc5f91becd64/master/pass/GoogleMapTA.jpg") // Ảnh map mẫu
                .crossfade(true)
                .build(),
            contentDescription = "Map Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // 2. TOP BAR (Nút Back)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 48.dp, start = 24.dp, end = 24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Sử dụng Surface để tạo nút nổi có bóng đổ đúng chuẩn Material
            Surface(
                onClick = onBackClick,
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = 4.dp,
                modifier = Modifier.size(44.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }

        // 3. BOTTOM SHEET LIST (Danh sách cửa hàng)
        StoreListBottomSheet(
            modifier = Modifier.align(Alignment.BottomCenter),
            onStoreSelected = onStoreSelected
        )
    }
}

@Composable
fun StoreListBottomSheet(
    modifier: Modifier = Modifier,
    onStoreSelected: (String) -> Unit
) {
    // Dữ liệu giả lập các cửa hàng
    val stores = listOf(
        StoreData("KTX Khu B Store", "230m away", "Open 08:00 - 22:00"),
        StoreData("Thủ Đức Branch", "1.2km away", "Open 07:00 - 21:00"),
        StoreData("Dĩ An Coffee", "3.5km away", "Open 09:00 - 23:00"),
        StoreData("Bros Coffee HQ", "5.0km away", "Open 08:00 - 20:00")
    )

    // Surface đóng vai trò như BottomSheet cố định
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(0.5f), // Chiếm 50% màn hình dưới
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        color = MaterialTheme.colorScheme.background, // Màu nền Be
        shadowElevation = 16.dp
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            // Thanh nắm kéo (Visual indicator)
            Box(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .width(40.dp)
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Select nearest Store",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Danh sách cuộn
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(stores) { store ->
                    StoreItem(store, onClick = { onStoreSelected(store.name) })
                }
            }
        }
    }
}

// Data class nội bộ để chứa dữ liệu cửa hàng
data class StoreData(val name: String, val distance: String, val hours: String)

@Composable
fun StoreItem(store: StoreData, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                shape = RoundedCornerShape(12.dp)
            )
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icon Location có nền màu Nâu nhạt
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(10.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Tên và giờ mở cửa
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = store.name,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = store.hours,
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.bodySmall
            )
        }

        // Khoảng cách và Icon điều hướng
        Column(horizontalAlignment = Alignment.End) {
            Icon(
                imageVector = Icons.Default.Navigation,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = store.distance,
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StoreLocatorPreview() {
    CustomerMobileTheme { StoreLocatorScreen() }
}