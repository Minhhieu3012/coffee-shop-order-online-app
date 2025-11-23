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
import vn.edu.ut.hieupm9898.customermobile.data.model.Product
import vn.edu.ut.hieupm9898.customermobile.features.auth.AuthViewModel
import vn.edu.ut.hieupm9898.customermobile.ui.components.BrosTextField
import vn.edu.ut.hieupm9898.customermobile.ui.components.CategoryChip
import vn.edu.ut.hieupm9898.customermobile.ui.components.CoffeeCard
import vn.edu.ut.hieupm9898.customermobile.ui.theme.BrosBackground
import vn.edu.ut.hieupm9898.customermobile.ui.theme.BrosTitle

// Data mẫu
private val dummyProducts = listOf(
    Product("1", "Cà phê đen", "Dark Roast, 120 Cal", 4.53, "link_to_img_1", "Coffee", true),
    Product("2", "Cà phê sữa", "Creamy, High Caffeine", 3.53, "link_to_img_2", "Coffee", false),
    Product("3", "Trà Đào", "Iced Peach Tea", 3.00, "link_to_img_3", "Tea", false),
    Product("4", "Bánh Mì", "Traditional Vietnamese Sandwich", 2.50, "link_to_img_4", "Food", true),
    Product("5", "Cappuccino", "Light & Foamy", 4.20, "link_to_img_5", "Coffee", false),
    Product("6", "Matcha Latte", "Sweet & Earthy", 4.80, "link_to_img_6", "Tea", true)
)

private val categoryMap = mapOf(
    "Tất cả" to "All",
    "Cà phê" to "Coffee",
    "Trà" to "Tea",
    "Đồ ăn" to "Food"
)

private val categories = listOf("Tất cả", "Cà phê", "Trà", "Đồ ăn")

@Composable
fun HomeScreen(
    onProductClick: (String) -> Unit,
    onSearchClick: () -> Unit,
    authViewModel: AuthViewModel = hiltViewModel() // 👈 THÊM ViewModel
) {
    var searchText by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Tất cả") }
    var favoriteProducts by remember { mutableStateOf(dummyProducts.map { it.id to it.isFavorite }.toMap()) }

    // 👇 LẤY THÔNG TIN USER
    val currentUser by authViewModel.currentUser.collectAsState()

    LaunchedEffect(Unit) {
        authViewModel.loadCurrentUser()
    }

    // 👇 LOAD USER KHI VÀO SCREEN
    // Thêm log để debug
    LaunchedEffect(currentUser) {
        Log.d("HomeScreen", "👤 Current user: ${currentUser?.displayName}")
    }

    val filteredProducts = remember(selectedCategory, searchText, favoriteProducts) {
        dummyProducts.map { product ->
            product.copy(isFavorite = favoriteProducts[product.id] ?: product.isFavorite)
        }.filter { product ->
            val categoryFilter = selectedCategory == "Tất cả" ||
                    product.category == categoryMap[selectedCategory]

            val searchFilter = searchText.isEmpty() ||
                    product.name.contains(searchText, ignoreCase = true) ||
                    product.description.contains(searchText, ignoreCase = true)

            categoryFilter && searchFilter
        }
    }

    Scaffold(
        containerColor = BrosBackground
    ) { paddingValues ->
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
                            // 👇 HIỂN THỊ TÊN USER
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
                            isSelected = category == selectedCategory,
                            onClick = { selectedCategory = category }
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
                            onFavoriteClick = {
                                favoriteProducts = favoriteProducts.toMutableMap().apply {
                                    this[product.id] = !(this[product.id] ?: product.isFavorite)
                                }
                            },
                            modifier = Modifier.weight(1f)
                        )
                    }
                    if (productPair.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}