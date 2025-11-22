package vn.edu.ut.hieupm9898.customermobile.features.profile

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
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
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import vn.edu.ut.hieupm9898.customermobile.navigation.AppRoutes
import vn.edu.ut.hieupm9898.customermobile.ui.theme.CustomerMobileTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController, // [QUAN TRỌNG] Thêm cái này để điều hướng
    onEditProfileClick: () -> Unit,
    onAddressClick: () -> Unit,
    onPaymentClick: () -> Unit,
    onHistoryClick: () -> Unit,
    onNotificationsClick: () -> Unit,
    // onLogoutClick: () -> Unit, <-- Đã xóa dòng này vì ta xử lý trực tiếp bên dưới
    onBackClick: () -> Unit = {}
) {
    val context = LocalContext.current // Lấy context để xóa bộ nhớ máy

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Cài đặt", fontWeight = FontWeight.Bold, fontSize = 30.sp) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            modifier = Modifier.size(60.dp)
                        )
                    }
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
                    title = "Chỉnh sửa hồ sơ",
                    onClick = onEditProfileClick
                )
                ProfileOptionItem(
                    icon = Icons.Default.LocationOn,
                    title = "Địa chỉ",
                    onClick = onAddressClick
                )
                ProfileOptionItem(
                    icon = Icons.Default.CreditCard,
                    title = "Phương thức thanh toán",
                    onClick = onPaymentClick
                )
                ProfileOptionItem(
                    icon = Icons.Default.History,
                    title = "Lịch sử mua hàng",
                    onClick = onHistoryClick
                )
                ProfileOptionItem(
                    icon = Icons.Default.Notifications,
                    title = "Thông báo",
                    onClick = onNotificationsClick
                )
                ProfileOptionItem(
                    icon = Icons.Default.Security,
                    title = "Bảo mật",
                    onClick = {}
                )
            }

            Spacer(modifier = Modifier.height(48.dp))

            // 3. Logout Button (XỬ LÝ TRỰC TIẾP TẠI ĐÂY)
            Surface(
                onClick = {
                    // BƯỚC 1: Xóa trạng thái đăng nhập ("Quên người dùng")
                    val sharedPreferences = context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
                    with(sharedPreferences.edit()) {
                        putBoolean("IS_LOGGED_IN", false)
                        putString("USER_EMAIL", "")
                        apply()
                    }

                    // BƯỚC 2: Đá về màn hình Đăng nhập (AUTH_GRAPH)
                    // Và xóa sạch lịch sử để không bấm Back quay lại được
                    navController.navigate(AppRoutes.AUTH_GRAPH) {
                        popUpTo(AppRoutes.MAIN_APP_GRAPH) { inclusive = true }
                    }
                },
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.errorContainer,
                modifier = Modifier.fillMaxWidth().height(56.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Đăng xuất",
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

// --- CÁC COMPOSABLE CON GIỮ NGUYÊN ---

@Composable
fun UserProfileHeader() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.onBackground)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data("https://img.freepik.com/free-photo/portrait-handsome-smiling-young-man-model-wearing-casual-summer-pink-clothes-fashion-stylish-man-posing_158538-5350.jpg")
                    .crossfade(true)
                    .build(),
                contentDescription = "User Avatar",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(110.dp)
                    .clip(CircleShape)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Khách hàng",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = "customer123@gmail.com",
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
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 2.dp,
        modifier = Modifier.fillMaxWidth().height(60.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
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
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f)
            )

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
        ProfileScreen(
            navController = rememberNavController(),
            onEditProfileClick = {},
            onAddressClick = {},
            onPaymentClick = {},
            onHistoryClick = {},
            onNotificationsClick = {}
        )
    }
}