package vn.edu.ut.hieupm9898.customermobile.features.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import vn.edu.ut.hieupm9898.customermobile.R
import vn.edu.ut.hieupm9898.customermobile.data.model.Product // Đã sử dụng Model Product (giả định có các trường cần thiết)
import vn.edu.ut.hieupm9898.customermobile.navigation.AppRoutes
// --- TÍCH HỢP COMPONENTS ---
import vn.edu.ut.hieupm9898.customermobile.ui.components.BrosBottomNavBar
import vn.edu.ut.hieupm9898.customermobile.ui.components.BrosTextField
import vn.edu.ut.hieupm9898.customermobile.ui.components.CategoryChip
import vn.edu.ut.hieupm9898.customermobile.ui.components.CoffeeCard
import vn.edu.ut.hieupm9898.customermobile.ui.theme.BrosBackground
import vn.edu.ut.hieupm9898.customermobile.ui.theme.BrosTitle

// Data mẫu đã được làm giàu (Thêm trường subtitle và isFavorite)
private val dummyProducts = listOf(
    Product("1", "Caffe Mocha", "Dark Roast, 120 Cal", 4.53, "link_to_img_1", "Coffee", true),
    Product("2", "Flat White", "Creamy, High Caffeine", 3.53, "link_to_img_2", "Coffee", false),
    Product("3", "Trà Đào", "Iced Peach Tea", 3.00, "link_to_img_3", "Tea", false),
    Product("4", "Bánh Mì", "Traditional Vietnamese Sandwich", 2.50, "link_to_img_4", "Food", true),
    Product("5", "Cappuccino", "Light & Foamy", 4.20, "link_to_img_5", "Coffee", false),
    Product("6", "Matcha Latte", "Sweet & Earthy", 4.80, "link_to_img_6", "Tea", true)
)
private val categories = listOf("All", "Coffee", "Tea", "Food", "Dessert")


@Composable
fun HomeScreen(navController: NavController) {
    var searchText by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("All") }

    val filteredProducts = remember(selectedCategory, searchText) {
        dummyProducts.filter { product ->
            (selectedCategory == "All" || product.category == selectedCategory) &&
                    (product.name.contains(searchText, ignoreCase = true) || product.description.contains(searchText, ignoreCase = true))
        }
    }

    // Sử dụng Scaffold để quản lý Padding dưới
    Scaffold(
        containerColor = BrosBackground,
        // (BrosBottomNavBar được đặt trong MainScreen, nên ta chỉ cần tính toán PaddingValues)
    ) { paddingValues ->

        // Dùng LazyColumn để cuộn toàn bộ nội dung
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
        ) {
            // --- 1. HEADER (Lớp trên cùng) ---
            item {
                Spacer(modifier = Modifier.height(20.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Avatar và Lời chào
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.logo),
                            contentDescription = "Avatar",
                            modifier = Modifier.size(50.dp).clip(CircleShape).background(Color.White)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text("Good Morning!", fontSize = 14.sp, color = Color.Gray)
                            Text("Hieu Pham", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = BrosTitle)
                        }
                    }
                    // Nút thông báo
                    IconButton(onClick = { /* TODO */ }) {
                        Icon(Icons.Default.Notifications, contentDescription = null, tint = BrosTitle)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }

            // --- 2. SEARCH BAR (Dùng BrosTextField) ---
            item {
                BrosTextField(
                    value = searchText,
                    onValueChange = { searchText = it },
                    label = "Search coffee...",
                    icon = Icons.Default.Search // Icon kính lúp
                )
                Spacer(modifier = Modifier.height(24.dp))
            }

            // --- 3. CATEGORIES (Dùng CategoryChip) ---
            item {
                Text("Categories", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = BrosTitle)
                Spacer(modifier = Modifier.height(16.dp))

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

            // --- 4. PRODUCT GRID (Dùng CoffeeCard) ---
            item {
                Text("Món Ngon Cho Hôm Nay", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 8.dp), color = BrosTitle)
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Do LazyVerticalGrid không thể cuộn lồng dễ dàng trong LazyColumn,
            // ta dùng LazyVerticalGrid cho phần còn lại (phần sản phẩm)
            // Tuy nhiên, vì LazyColumn đã bao bọc, ta sẽ mô phỏng Grid bằng cách sắp xếp thủ công (hoặc sử dụng Grid trong Item)
            // Cách đơn giản nhất: Tạo Item dạng Grid (lấy 2 item/lần)
            items(filteredProducts.chunked(2)) { pair ->
                Row(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    pair.forEach { product ->
                        // Component CoffeeCard (ĐÃ TÍCH HỢP CHUẨN THAM SỐ)
                        CoffeeCard(
                            title = product.name,
                            subtitle = product.description,
                            price = product.price,
                            imageUrl = product.imageUrl,
                            isFavorite = product.isFavorite,
                            onCardClick = { navController.navigate(AppRoutes.createProductDetailRoute(product.id)) },
                            onAddClick = { /* TODO */ },
                            onFavoriteClick = { /* TODO */ },
                            modifier = Modifier.weight(1f)
                        )
                    }
                    // Nếu hàng cuối cùng chỉ có 1 món, thêm Spacer để cân bằng
                    if (pair.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun HomePreview() {
    HomeScreen(navController = rememberNavController())
}