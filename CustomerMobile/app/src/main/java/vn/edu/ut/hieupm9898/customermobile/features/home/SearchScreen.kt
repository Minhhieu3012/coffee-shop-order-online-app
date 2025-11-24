package vn.edu.ut.hieupm9898.customermobile.features.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import vn.edu.ut.hieupm9898.customermobile.ui.components.CategoryChip
import vn.edu.ut.hieupm9898.customermobile.ui.components.CoffeeCard
import vn.edu.ut.hieupm9898.customermobile.ui.theme.CustomerMobileTheme
import androidx.compose.foundation.clickable
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    onBackClick: () -> Unit = {},
    onProductClick: (Int) -> Unit = {}
) {
    var searchText by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("All") }

    // Dữ liệu giả lập kết quả tìm kiếm
    val searchResults = remember {
        listOf(
            SearchProduct(1, "Cappuccino", "With Chocolate", 45000.0, "https://placehold.co/150"),
            SearchProduct(2, "Americano", "No Milk", 35000.0, "https://placehold.co/150"),
            SearchProduct(3, "Espresso", "Strong", 30000.0, "https://placehold.co/150"),
            SearchProduct(4, "Latte", "Creamy", 50000.0, "https://placehold.co/150")
        )
    }

    // Dữ liệu lịch sử tìm kiếm
    val recentSearches = listOf("Cappuccino", "Cold Brew", "Iced Latte")

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            // Custom Top Bar chứa ô tìm kiếm
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 48.dp, bottom = 16.dp, start = 20.dp, end = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }

                // Ô tìm kiếm
                TextField(
                    value = searchText,
                    onValueChange = { searchText = it },
                    placeholder = { Text("Search coffee...", color = Color.Gray) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    trailingIcon = {
                        if (searchText.isNotEmpty()) {
                            IconButton(onClick = { searchText = "" }) {
                                Icon(Icons.Default.Close, contentDescription = "Clear")
                            }
                        } else {
                            Icon(Icons.Default.Tune, contentDescription = "Filter")
                        }
                    },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.weight(1f).height(56.dp)
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp)
        ) {
            if (searchText.isEmpty()) {
                // HIỂN THỊ LỊCH SỬ TÌM KIẾM KHI CHƯA NHẬP GÌ
                Text(
                    "Recent Searches",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(12.dp))
                recentSearches.forEach { text ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { searchText = text }
                            .padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.History, contentDescription = null, tint = Color.Gray)
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(text, style = MaterialTheme.typography.bodyLarge)
                    }
                }
            } else {
                // HIỂN THỊ KẾT QUẢ TÌM KIẾM

                // 1. Filter Chips
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    CategoryChip("All", selectedCategory == "All", onClick = { selectedCategory = "All" })
                    CategoryChip("Coffee", selectedCategory == "Coffee", onClick = { selectedCategory = "Coffee" })
                    CategoryChip("Non-Coffee", selectedCategory == "Non-Coffee", onClick = { selectedCategory = "Non-Coffee" })
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 2. Grid Results
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    items(searchResults) { product ->
                        CoffeeCard(
                            title = product.title,
                            subtitle = product.subtitle,
                            price = product.price,
                            imageUrl = product.imageUrl,
                            isFavorite = false,
                            onCardClick = { onProductClick(product.id) },
                            onFavoriteClick = {},
                            onAddClick = {}
                        )
                    }
                }
            }
        }
    }
}

data class SearchProduct(val id: Int, val title: String, val subtitle: String, val price: Double, val imageUrl: String)

@Preview
@Composable
fun SearchScreenPreview() {
    CustomerMobileTheme { SearchScreen() }
}