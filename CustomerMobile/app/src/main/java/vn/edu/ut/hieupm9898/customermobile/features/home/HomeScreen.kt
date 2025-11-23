package vn.edu.ut.hieupm9898.customermobile.features.home

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import vn.edu.ut.hieupm9898.customermobile.R
import vn.edu.ut.hieupm9898.customermobile.features.auth.AuthViewModel
import vn.edu.ut.hieupm9898.customermobile.ui.components.BrosTextField
import vn.edu.ut.hieupm9898.customermobile.ui.components.CategoryChip
import vn.edu.ut.hieupm9898.customermobile.ui.components.CoffeeCard
import vn.edu.ut.hieupm9898.customermobile.ui.theme.BrosBackground
import vn.edu.ut.hieupm9898.customermobile.ui.theme.BrosTitle

private val categories = listOf("Tất cả", "Cà phê", "Trà", "Đồ ăn")

@Composable
fun HomeScreen(
    onProductClick: (String) -> Unit,
    onSearchClick: () -> Unit,
    authViewModel: AuthViewModel = hiltViewModel(),
    homeViewModel: HomeViewModel = hiltViewModel() // 👈 THÊM ViewModel
) {
    // 👇 LẤY STATE TỪ VIEWMODEL
    val uiState by homeViewModel.uiState.collectAsState()
    val currentUser by authViewModel.currentUser.collectAsState()

    var searchText by remember { mutableStateOf("") }

    // Load user info
    LaunchedEffect(Unit) {
        authViewModel.loadCurrentUser()
    }

    // Load products khi vào màn hình (chỉ chạy 1 lần)
    LaunchedEffect(Unit) {
        homeViewModel.loadProducts()
    }

    // Debug log
    LaunchedEffect(currentUser) {
        Log.d("HomeScreen", "👤 Current user: ${currentUser?.displayName}")
    }

    // Lọc products dựa trên category và search
    val filteredProducts = remember(uiState.products, uiState.selectedCategory, searchText) {
        var filtered = uiState.products

        // Filter by category
        if (uiState.selectedCategory != "Tất cả") {
            val categoryMap = mapOf(
                "Cà phê" to "Coffee",
                "Trà" to "Tea",
                "Đồ ăn" to "Food"
            )
            val englishCategory = categoryMap[uiState.selectedCategory]
            if (englishCategory != null) {
                filtered = filtered.filter { it.category == englishCategory }
            }
        }

        // Filter by search
        if (searchText.isNotEmpty()) {
            filtered = filtered.filter { product ->
                product.name.contains(searchText, ignoreCase = true) ||
                        product.description.contains(searchText, ignoreCase = true)
            }
        }

        filtered
    }

    Scaffold(
        containerColor = BrosBackground
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 24.dp)
            ) {
                // --- 1. HEADER ---
                item {
                    Spacer(modifier = Modifier.height(20.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Image(
                                painter = painterResource(id = R.drawable.logo),
                                contentDescription = "Avatar",
                                modifier = Modifier
                                    .size(60.dp)
                                    .clip(CircleShape)
                                    .background(Color.White)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    "Chào buổi sáng!",
                                    fontSize = 20.sp,
                                    color = Color.Gray
                                )
                                Text(
                                    text = currentUser?.displayName ?: "Khách hàng",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = BrosTitle
                                )
                            }
                        }
                        IconButton(onClick = { /* TODO: Notifications */ }) {
                            Icon(
                                Icons.Default.Notifications,
                                contentDescription = "Notifications",
                                tint = BrosTitle,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                }

                // --- 2. SEARCH BAR ---
                item {
                    BrosTextField(
                        value = searchText,
                        onValueChange = { searchText = it },
                        label = "Tìm kiếm...",
                        icon = Icons.Default.Search
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                }

                // --- 3. CATEGORIES ---
                item {
                    Text(
                        "Phân loại",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = BrosTitle
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }

                item {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(categories) { category ->
                            CategoryChip(
                                text = category,
                                isSelected = category == uiState.selectedCategory,
                                onClick = { homeViewModel.filterByCategory(category) }
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                }

                // --- 4. PRODUCT TITLE ---
                item {
                    Text(
                        "Đồ uống và món ăn đi kèm",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = BrosTitle,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                // --- 5. PRODUCT GRID ---
                if (!uiState.isLoading && filteredProducts.isEmpty()) {
                    // Empty state
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 40.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = if (searchText.isNotEmpty())
                                    "Không tìm thấy sản phẩm"
                                else
                                    "Chưa có sản phẩm",
                                color = Color.Gray
                            )
                        }
                    }
                } else {
                    items(filteredProducts.chunked(2)) { productPair ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            productPair.forEach { product ->
                                CoffeeCard(
                                    title = product.name,
                                    subtitle = product.description,
                                    price = product.price,
                                    imageUrl = product.imageUrl,
                                    isFavorite = product.isFavorite,
                                    onCardClick = { onProductClick(product.id) },
                                    onAddClick = { /* TODO: Add to cart */ },
                                    onFavoriteClick = { homeViewModel.toggleFavorite(product.id) },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                            if (productPair.size == 1) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(80.dp))
                }
            }

            // --- 6. LOADING INDICATOR ---
            if (uiState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            // --- 7. ERROR MESSAGE ---
            if (uiState.errorMessage != null) {
                Snackbar(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp),
                    action = {
                        TextButton(onClick = { homeViewModel.retry() }) {
                            Text("Thử lại")
                        }
                    }
                ) {
                    Text(uiState.errorMessage ?: "Có lỗi xảy ra")
                }
            }
        }
    }
}