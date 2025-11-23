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
import vn.edu.ut.hieupm9898.customermobile.ui.components.CategoryChip
import vn.edu.ut.hieupm9898.customermobile.ui.components.CoffeeCard
import vn.edu.ut.hieupm9898.customermobile.ui.components.SearchBarWithSuggestions
import vn.edu.ut.hieupm9898.customermobile.ui.components.skeleton.HomeScreenSkeleton
import vn.edu.ut.hieupm9898.customermobile.ui.theme.BrosBackground
import vn.edu.ut.hieupm9898.customermobile.ui.theme.BrosTitle

private val categories = listOf("Tất cả", "Cà phê", "Trà", "Đá xay")

@Composable
fun HomeScreen(
    onProductClick: (String) -> Unit,
    onSearchClick: () -> Unit,
    authViewModel: AuthViewModel = hiltViewModel(),
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by homeViewModel.uiState.collectAsState()
    val currentUser by authViewModel.currentUser.collectAsState()

    // Load user info
    LaunchedEffect(Unit) {
        authViewModel.loadCurrentUser()
    }

    // Load products
    LaunchedEffect(Unit) {
        homeViewModel.loadProducts()
    }

    // Debug log
    LaunchedEffect(currentUser) {
        Log.d("HomeScreen", "👤 Current user: ${currentUser?.displayName}")
    }

    // Get filtered products
    val filteredProducts = remember(
        uiState.allProducts,
        uiState.selectedCategory,
        uiState.searchQuery
    ) {
        homeViewModel.getFilteredProducts()
    }

    Scaffold(
        containerColor = BrosBackground
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            if (uiState.isLoading) {
                HomeScreenSkeleton(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(horizontal = 24.dp),
                    contentPadding = PaddingValues(bottom = 100.dp)
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
                                        "Xin chào thượng đế!",
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

                    // --- 2. SEARCH BAR WITH SUGGESTIONS ---
                    item {
                        SearchBarWithSuggestions(
                            query = uiState.searchQuery,
                            onQueryChange = { homeViewModel.onSearchQueryChange(it) },
                            suggestions = uiState.searchResults,
                            showSuggestions = uiState.showSuggestions,
                            onSuggestionClick = { product ->
                                homeViewModel.selectSuggestion(product)
                                onProductClick(product.id)
                            },
                            onClearClick = { homeViewModel.clearSearch() },
                            isSearching = uiState.isSearching
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
                                    onClick = { homeViewModel.filterByCategory(category) },
                                    count = if (category == "Đá xay") {
                                        null
                                    } else {
                                        homeViewModel.getProductCountByCategory(category)
                                    }
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(24.dp))
                    }

                    // --- 4. PRODUCT TITLE ---
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "Đồ uống và món ăn đi kèm",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = BrosTitle
                            )

                            if (uiState.searchQuery.isNotEmpty()) {
                                Text(
                                    text = "${filteredProducts.size} kết quả",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.Gray
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    // --- 5. PRODUCT GRID ---
                    if (filteredProducts.isEmpty()) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 40.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Notifications,
                                        contentDescription = null,
                                        modifier = Modifier.size(64.dp),
                                        tint = Color.LightGray
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Text(
                                        text = if (uiState.searchQuery.isNotEmpty())
                                            "Không tìm thấy \"${uiState.searchQuery}\""
                                        else
                                            "Chưa có sản phẩm",
                                        color = Color.Gray,
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                }
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
                                        isOutOfStock = product.isOutOfStock(), // ✅ THÊM DÒNG NÀY
                                        onCardClick = {
                                            // ✅ Chỉ cho click vào nếu còn hàng
                                            if (!product.isOutOfStock()) {
                                                onProductClick(product.id)
                                            }
                                        },
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
                }
            }

            // Error Message
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