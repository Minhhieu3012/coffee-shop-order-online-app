package vn.edu.ut.hieupm9898.customermobile.features.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp // Cần thư viện extended
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos // Cần thư viện extended
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import vn.edu.ut.hieupm9898.customermobile.ui.theme.CustomerMobileTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onEditProfileClick: () -> Unit = {},
    onAddressClick: () -> Unit = {},
    onPaymentClick: () -> Unit = {},
    onHistoryClick: () -> Unit = {},
    onNotificationsClick: () -> Unit = {}, // <-- ĐÃ THÊM THAM SỐ NÀY
    onLogoutClick: () -> Unit = {}
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Profile",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            // 1. User Info Section
            UserProfileHeader()

            Spacer(modifier = Modifier.height(32.dp))

            // 2. Menu Options List
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                ProfileOptionItem(
                    icon = Icons.Default.Person,
                    title = "Edit Profile",
                    onClick = onEditProfileClick
                )
                ProfileOptionItem(
                    icon = Icons.Default.LocationOn,
                    title = "Address",
                    onClick = onAddressClick
                )
                ProfileOptionItem(
                    icon = Icons.Default.CreditCard,
                    title = "Payment Method",
                    onClick = onPaymentClick
                )
                ProfileOptionItem(
                    icon = Icons.Default.History,
                    title = "Order History",
                    onClick = onHistoryClick
                )
                ProfileOptionItem(
                    icon = Icons.Default.Notifications,
                    title = "Notifications",
                    onClick = onNotificationsClick // <-- GỌI HÀM NÀY
                )
                ProfileOptionItem(
                    icon = Icons.Default.Security,
                    title = "Security",
                    onClick = {}
                )
            }

            Spacer(modifier = Modifier.height(48.dp))

            // 3. Logout Button
            Surface(
                onClick = onLogoutClick,
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.errorContainer,
                modifier = Modifier.fillMaxWidth().height(56.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    // Lưu ý: Nếu chưa có icon ExitToApp, dùng tạm Icons.Default.Close
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Log Out",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.error,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

// ... (Các hàm UserProfileHeader và ProfileOptionItem giữ nguyên như cũ) ...
// (Bạn nhớ giữ lại phần Preview và các hàm con ở dưới nhé!)
@Composable
fun UserProfileHeader() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Avatar với viền
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surface) // Viền giả
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data("https://img.freepik.com/free-photo/portrait-handsome-smiling-young-man-model-wearing-casual-summer-pink-clothes-fashion-stylish-man-posing_158538-5350.jpg")
                    .crossfade(true)
                    .build(),
                contentDescription = "User Avatar",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(110.dp) // Ảnh nhỏ hơn box để lòi viền ra
                    .clip(CircleShape)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Tên
        Text(
            text = "Hieu PM",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        // Email/Phone
        Text(
            text = "hieupm9898@ut.edu.vn",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.secondary
        )
    }
}

@Composable
fun ProfileOptionItem(
    icon: ImageVector,
    title: String,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surface, // Màu nền Header/Card
        shadowElevation = 2.dp, // Đổ bóng nhẹ
        modifier = Modifier.fillMaxWidth().height(60.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon bên trái (có nền tròn nhạt)
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary, // Màu nâu chủ đạo
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Title
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f)
            )

            // Mũi tên bên phải
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProfileScreenPreview() {
    CustomerMobileTheme {
        ProfileScreen()
    }
}