package vn.edu.ut.hieupm9898.customermobile.features.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
// Đảm bảo import đúng Component Card đã tạo
import vn.edu.ut.hieupm9898.customermobile.ui.components.OrderHistoryCard
import vn.edu.ut.hieupm9898.customermobile.ui.theme.CustomerMobileTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderHistoryScreen(
    onBackClick: () -> Unit = {}
) {
    // State quản lý tab đang chọn (0: Ongoing, 1: History)
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Ongoing", "History")

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background, // Màu nền Theme
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "My Orders",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.headlineMedium
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
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // 1. THANH TAB TÙY CHỈNH
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = MaterialTheme.colorScheme.background,
                contentColor = MaterialTheme.colorScheme.primary,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        height = 3.dp,
                        color = MaterialTheme.colorScheme.primary // Màu Nâu
                    )
                },
                divider = {
                    // Ẩn đường kẻ mặc định hoặc làm mờ nó
                    HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                },
                modifier = Modifier.padding(horizontal = 24.dp)
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = {
                            Text(
                                text = title,
                                fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                                style = MaterialTheme.typography.titleMedium,
                                color = if (selectedTab == index) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
                            )
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 2. DANH SÁCH ĐƠN HÀNG
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (selectedTab == 0) {
                    // --- TAB: ONGOING (Đang giao) ---
                    items(2) {
                        OrderHistoryCard(
                            title = "Cappuccino",
                            description = "Spicy · Medium",
                            size = "x1",
                            price = 45000.0,
                            imageUrl = "https://img.freepik.com/free-photo/cup-coffee-with-heart-drawn-foam_1286-70.jpg",
                            onReorderClick = {
                                // Logic khi nhấn nút (VD: Chuyển sang màn hình Delivery)
                            }
                        )
                    }
                } else {
                    // --- TAB: HISTORY (Lịch sử cũ) ---
                    items(4) {
                        OrderHistoryCard(
                            title = "Americano",
                            description = "No sugar · Large",
                            size = "x2",
                            price = 60000.0,
                            imageUrl = "https://img.freepik.com/free-photo/ice-coffee-tall-glass-with-cream-poured-over_140725-7254.jpg",
                            onReorderClick = {
                                // Logic Re-order
                            }
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OrderHistoryPreview() {
    CustomerMobileTheme { OrderHistoryScreen() }
}