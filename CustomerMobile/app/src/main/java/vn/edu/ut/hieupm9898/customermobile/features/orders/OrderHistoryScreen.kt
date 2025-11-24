package vn.edu.ut.hieupm9898.customermobile.features.orders

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import vn.edu.ut.hieupm9898.customermobile.data.model.OrderStatus
import vn.edu.ut.hieupm9898.customermobile.ui.components.OrderHistoryCard
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderHistoryScreen(
    onBackClick: () -> Unit = {},
    onNavigateToCart: () -> Unit,
    viewModel: OrderHistoryViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val formatter = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))
    val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale("vi", "VN"))

    val snackbarHostState = remember { SnackbarHostState() }

    // ✅ LẮNG NGHE SỰ KIỆN ĐIỀU HƯỚNG TỪ VIEWMODEL
    LaunchedEffect(Unit) {
        viewModel.navigationChannel.collect { event ->
            when (event) {
                is OrderHistoryEvent.NavigateToCart -> {
                    onNavigateToCart()
                }
            }
        }
    }

    // Show error snackbar
    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let { error ->
            snackbarHostState.showSnackbar(
                message = error,
                duration = SnackbarDuration.Short
            )
            viewModel.clearError()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Đơn hàng của tôi",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.headlineMedium,
                        fontSize = 24.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.size(24.dp) // Adjusted size to standard
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
            // TAB ROW
            TabRow(
                selectedTabIndex = uiState.selectedTab,
                containerColor = MaterialTheme.colorScheme.background,
                contentColor = MaterialTheme.colorScheme.primary,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[uiState.selectedTab]),
                        height = 3.dp,
                        color = MaterialTheme.colorScheme.primary
                    )
                },
                divider = {
                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
                    )
                },
                modifier = Modifier.padding(horizontal = 24.dp)
            ) {
                listOf("Hiện tại", "Lịch sử").forEachIndexed { index, title ->
                    Tab(
                        selected = uiState.selectedTab == index,
                        onClick = { viewModel.selectTab(index) },
                        text = {
                            Text(
                                text = title,
                                fontWeight = if (uiState.selectedTab == index)
                                    FontWeight.Bold else FontWeight.Normal,
                                style = MaterialTheme.typography.titleMedium,
                                color = if (uiState.selectedTab == index)
                                    MaterialTheme.colorScheme.primary
                                else
                                    MaterialTheme.colorScheme.secondary
                            )
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // CONTENT
            if (uiState.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                val orders = if (uiState.selectedTab == 0)
                    uiState.ongoingOrders
                else
                    uiState.historyOrders

                if (orders.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = if (uiState.selectedTab == 0)
                                    "Chưa có đơn hàng nào"
                                else
                                    "Chưa có lịch sử",
                                style = MaterialTheme.typography.titleLarge,
                                color = Color.Gray
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Hãy đặt món ngay!",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Gray
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        contentPadding = PaddingValues(
                            horizontal = 24.dp,
                            vertical = 8.dp,
                            // Thêm bottom padding để tránh bị che bởi navigation bar nếu có
                            // bottom = 80.dp
                        ),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(orders) { order ->
                            val firstItem = order.items.firstOrNull()

                            if (firstItem != null) {
                                val statusDisplay = try {
                                    OrderStatus.valueOf(order.status).getDisplayName()
                                } catch (e: Exception) {
                                    order.status
                                }

                                OrderHistoryCard(
                                    title = firstItem.productName,
                                    description = buildString {
                                        if (firstItem.size.isNotEmpty()) {
                                            append(firstItem.size)
                                        }
                                        if (firstItem.dairy.isNotEmpty()) {
                                            if (isNotEmpty()) append(" · ")
                                            append(firstItem.dairy)
                                        }
                                    },
                                    size = "x${firstItem.quantity}",
                                    price = order.total,
                                    imageUrl = firstItem.productImage,
                                    status = statusDisplay,
                                    orderDate = order.createdAt?.toDate()?.let {
                                        dateFormat.format(it)
                                    } ?: "",
                                    itemCount = order.items.size,
                                    onReorderClick = {
                                        // ✅ GỌI HÀM REORDER
                                        viewModel.reorderOrder(order)
                                    },
                                    onCancelClick = if (uiState.selectedTab == 0) {
                                        { viewModel.cancelOrder(order.orderId) }
                                    } else null
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}