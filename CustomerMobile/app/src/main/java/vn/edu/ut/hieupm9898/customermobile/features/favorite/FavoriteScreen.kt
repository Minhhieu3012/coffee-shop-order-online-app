package vn.edu.ut.hieupm9898.customermobile.features.favorite

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import vn.edu.ut.hieupm9898.customermobile.ui.components.CoffeeCard
import vn.edu.ut.hieupm9898.customermobile.ui.components.EmptyStateScreen
import vn.edu.ut.hieupm9898.customermobile.ui.theme.CustomerMobileTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteScreen(
    onProductClick: (Int) -> Unit = {},
    onGoHomeClick: () -> Unit = {}
) {
    // Dữ liệu giả: Đổi thành emptyList() để test giao diện rỗng
    val favoriteProducts = remember {
        listOf(
            FavoriteProduct(1, "Cappuccino", "With Chocolate", 45000.0, "https://img.freepik.com/free-photo/cup-coffee-with-heart-drawn-foam_1286-70.jpg"),
            FavoriteProduct(2, "Espresso", "Strong & Dark", 30000.0, "https://img.freepik.com/free-photo/fresh-coffee-steaming-cup-wooden-table-generated-by-ai_188544-23456.jpg"),
            FavoriteProduct(3, "Latte", "Creamy & Sweet", 50000.0, "https://img.freepik.com/free-photo/view-3d-coffee-cup_23-2151083712.jpg")
        )
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Yêu thích",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.headlineMedium
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp)
        ) {
            if (favoriteProducts.isEmpty()) {
                // --- TRƯỜNG HỢP RỖNG (Sử dụng EmptyStateScreen đã làm) ---
                EmptyStateScreen(
                    title = "No favorite drinks yet!",
                    message = "Start exploring and mark your loved drinks.",
                    buttonText = "Explore Now",
                    onClick = onGoHomeClick
                )
            } else {
                // --- TRƯỜNG HỢP CÓ DỮ LIỆU ---
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(bottom = 20.dp)
                ) {
                    items(favoriteProducts) { product ->
                        CoffeeCard(
                            title = product.title,
                            subtitle = product.subtitle,
                            price = product.price,
                            imageUrl = product.imageUrl,
                            isFavorite = true, // Luôn là true ở màn hình này
                            onCardClick = { onProductClick(product.id) },
                            onFavoriteClick = { /* Logic bỏ thích */ },
                            onAddClick = { /* Logic thêm vào giỏ */ }
                        )
                    }
                }
            }
        }
    }
}

data class FavoriteProduct(val id: Int, val title: String, val subtitle: String, val price: Double, val imageUrl: String)

@Preview(showBackground = true)
@Composable
fun FavoritePreview() {
    CustomerMobileTheme { FavoriteScreen() }
}