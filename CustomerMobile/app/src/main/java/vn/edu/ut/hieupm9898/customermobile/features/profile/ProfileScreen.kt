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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import vn.edu.ut.hieupm9898.customermobile.data.model.User
import vn.edu.ut.hieupm9898.customermobile.navigation.AppRoutes
import vn.edu.ut.hieupm9898.customermobile.ui.theme.CustomerMobileTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    onEditProfileClick: () -> Unit,
    onAddressClick: () -> Unit,
    onPaymentClick: () -> Unit,
    onHistoryClick: () -> Unit,
    onNotificationsClick: () -> Unit,
    onBackClick: () -> Unit = {},
    viewModel: ProfileViewModel = hiltViewModel() // 👈 THÊM ViewModel
) {
    val context = LocalContext.current
    val currentUser by viewModel.currentUser.collectAsState() // 👈 LẤY USER
    val isLoading by viewModel.isLoading.collectAsState()

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

            // 1. User Info Section - HIỂN THỊ LOADING HOẶC USER
            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                UserProfileHeader(user = currentUser) // 👈 TRUYỀN USER VÀO
            }

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

                // 👇 THÊM PHẦN HIỂN THỊ ĐIỂM THƯỞNG
                if (currentUser != null) {
                    ProfileStatsCard(
                        loyaltyPoints = currentUser!!.loyaltyPoints,
                        totalOrders = currentUser!!.totalOrders,
                        totalSpent = currentUser!!.totalSpent
                    )
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

            // 3. Logout Button
            Surface(
                onClick = {
                    val sharedPreferences = context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
                    with(sharedPreferences.edit()) {
                        putBoolean("IS_LOGGED_IN", false)
                        putString("USER_EMAIL", "")
                        apply()
                    }

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

// 👇 CẬP NHẬT UserProfileHeader ĐỂ NHẬN USER
@Composable
fun UserProfileHeader(user: User?) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
        ) {
            if (user?.avatarUrl.isNullOrEmpty()) {
                // Hiển thị icon mặc định nếu không có avatar
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Default Avatar",
                    modifier = Modifier.size(60.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            } else {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(user?.avatarUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = "User Avatar",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(110.dp)
                        .clip(CircleShape)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = user?.displayName?.takeIf { it.isNotEmpty() } ?: "Chưa có tên",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Text(
            text = user?.email?.takeIf { it.isNotEmpty() } ?: "Chưa có email",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.secondary
        )

        // Hiển thị số điện thoại nếu có
        if (!user?.phoneNumber.isNullOrEmpty()) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = user?.phoneNumber ?: "",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.outline
            )
        }
    }
}

// 👇 THÊM CARD HIỂN THỊ THỐNG KÊ
@Composable
fun ProfileStatsCard(
    loyaltyPoints: Int,
    totalOrders: Int,
    totalSpent: Double
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Thống kê tài khoản",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                StatItem(
                    icon = Icons.Default.Stars,
                    label = "Điểm thưởng",
                    value = loyaltyPoints.toString()
                )
                StatItem(
                    icon = Icons.Default.ShoppingBag,
                    label = "Đơn hàng",
                    value = totalOrders.toString()
                )
                StatItem(
                    icon = Icons.Default.AttachMoney,
                    label = "Tổng chi",
                    value = "${String.format("%.0f", totalSpent)}đ"
                )
            }
        }
    }
}

@Composable
fun StatItem(
    icon: ImageVector,
    label: String,
    value: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.outline
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