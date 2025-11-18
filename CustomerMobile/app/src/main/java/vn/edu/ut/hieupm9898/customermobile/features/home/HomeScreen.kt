

package vn.edu.ut.hieupm9898.customermobile.features.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import vn.edu.ut.hieupm9898.customermobile.data.model.Product
import vn.edu.ut.hieupm9898.customermobile.navigation.AppRoutes
// Tích hợp components của bạn
import vn.edu.ut.hieupm9898.customermobile.ui.components.BrosBottomNavBar
import vn.edu.ut.hieupm9898.customermobile.ui.components.CategoryChip
import vn.edu.ut.hieupm9898.customermobile.ui.components.CoffeeCard

// Data mẫu cho Preview và testing
private val dummyProducts = listOf(
    Product("1", "Espresso", 3.50, "url1", "Coffee"),
    Product("2", "Cà Phê Sữa", 4.00, "url2", "Coffee"),
    Product("3", "Trà Đào", 3.00, "url3", "Tea"),
    Product("4", "Bánh Mì", 2.50, "url4", "Food"),
)
private val categories = listOf("All", "Coffee", "Tea", "Food", "Dessert")


@Composable
fun HomeScreen(navController: NavController) {
    // State giả định (trong thực tế sẽ lấy từ ViewModel)
    var selectedCategory by remember { mutableStateOf("All") }
    val products = dummyProducts

    // Lấy route hiện tại để truyền vào BottomBar
    val currentRoute = AppRoutes.HOME

    Scaffold(
        bottomBar = {
            // SỬA LỖI: Xóa tham số navHostController vì nó không tồn tại trong BrosBottomNavBar.kt
            BrosBottomNavBar(
                currentRoute = currentRoute,
                onNavigate = { route ->
                    navController.navigate(route) {
                        popUpTo(AppRoutes.HOME) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        },
    ) { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            // --- Phần 1: Thanh Danh mục (Category Chips) ---
            item {
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(categories) { category ->
                        // SỬA LỖI: CategoryChip cần tham số 'text' (thay cho 'name')
                        CategoryChip(
                            text = category,
                            isSelected = category == selectedCategory,
                            onClick = { selectedCategory = category }
                        )
                    }
                }
            }

            // --- Phần 2: Danh sách Sản phẩm (Coffee Cards) ---
            item {
                Text(
                    text = "Món Ngon Cho Hôm Nay",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            val filteredProducts = if (selectedCategory == "All") products else products.filter { it.category == selectedCategory }

            items(filteredProducts) { product ->
                // SỬA LỖI: CoffeeCard không nhận object 'product' hay 'item', mà nhận thuộc tính riêng biệt
                CoffeeCard(
                    // SỬA LỖI: Thay thế các tham số không tồn tại bằng các tham số riêng biệt
                    title = product.name,
                    price = product.price, // SỬA LỖI: No value passed for parameter price
                    subtitle = product.category,
                    imageUrl = product.imageUrl,
                    isFavorite = false,

                    onCardClick = {
                        navController.navigate(AppRoutes.PRODUCT_DETAIL.replace("{productId}", product.id))
                    },
                    onAddClick = { /* Logic thêm vào giỏ hàng */ },
                    onFavoriteClick = { /* Logic thay đổi trạng thái yêu thích */ }
                )
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun HomeScreenPreview() {
    HomeScreen(navController = rememberNavController())
}