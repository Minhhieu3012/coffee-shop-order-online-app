package vn.edu.ut.hieupm9898.customermobile.features.product_detail

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import vn.edu.ut.hieupm9898.customermobile.data.model.Product
import vn.edu.ut.hieupm9898.customermobile.ui.components.BrosButton
import java.text.NumberFormat
import java.util.Locale

// --- MÀU SẮC (Nên đưa vào ui/theme/Color.kt sau này) ---
private val CoffeeBackground = Color(0xFFF5F1E8)
private val CoffeeBrown = Color(0xFF6F4E37)
private val StarYellow = Color(0xFFFFC107)

/**
 * Màn hình chính: ProductDetailScreen
 * Chịu trách nhiệm kết nối ViewModel và điều hướng
 */
@Composable
fun ProductDetailScreen(
    navController: NavController,
    product: Product, // Nhận object Product từ màn hình Home truyền sang
    viewModel: ProductDetailViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    // State nội bộ của màn hình
    var selectedSize by remember { mutableStateOf("M") } // Mặc định Size M
    var selectedDairy by remember { mutableStateOf("Whole Milk") }
    var isFavorite by remember { mutableStateOf(product.isFavorite) }

    // Dữ liệu giả lập cho Option (Sau này có thể lấy từ DB)
    val availableSizes = listOf("S", "M", "L")
    val availableDairy = listOf(
        Pair("Whole Milk", 0.0),
        Pair("Almond Milk", 10000.0),
        Pair("Oat Milk", 15000.0)
    )

    // Gọi phần hiển thị giao diện
    ProductDetailContent(
        product = product, // Truyền nguyên object để dùng
        rating = 4.5f,
        ratingCountText = "(120 reviews)",
        isFavorite = isFavorite,
        availableSizes = availableSizes,
        selectedSize = selectedSize,
        availableDairy = availableDairy,
        selectedDairy = selectedDairy,

        // --- XỬ LÝ SỰ KIỆN ---
        onBackClick = { navController.popBackStack() },
        onFavoriteClick = { isFavorite = !isFavorite },
        onSizeSelected = { newSize -> selectedSize = newSize },
        onDairySelected = { newDairy -> selectedDairy = newDairy },

        // LOGIC THÊM VÀO GIỎ HÀNG
        onAddToCartClick = {
            // 1. Gọi ViewModel để lưu vào Repository
            viewModel.addToCart(
                product = product,
                quantity = 1,
                size = selectedSize
            )

            // 2. Thông báo cho người dùng
            Toast.makeText(context, "Đã thêm ${product.name} (Size $selectedSize) vào giỏ!", Toast.LENGTH_SHORT).show()

            // 3. Điều hướng (Chọn 1 trong 2 cách dưới)
            navController.popBackStack() // Cách 1: Quay lại menu để chọn tiếp
            // navController.navigate("cart") // Cách 2: Đi thẳng tới giỏ hàng
        }
    )
}

/**
 * Phần Giao Diện (Stateless UI)
 * Tách biệt hoàn toàn logic để dễ tái sử dụng và xem Preview
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailContent(
    product: Product,
    rating: Float,
    ratingCountText: String,
    isFavorite: Boolean,
    availableSizes: List<String>,
    selectedSize: String,
    availableDairy: List<Pair<String, Double>>,
    selectedDairy: String,
    relatedProducts: List<RelatedProduct> = emptyList(),
    onBackClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    onSizeSelected: (String) -> Unit,
    onDairySelected: (String) -> Unit,
    onAddToCartClick: () -> Unit,
    onRelatedProductClick: (RelatedProduct) -> Unit = {}
) {
    Scaffold(
        containerColor = CoffeeBackground,
        bottomBar = {
            // Thanh Button Add to Cart dính ở dưới đáy
            Surface(
                shadowElevation = 12.dp, // Đổ bóng đẹp hơn
                color = Color.White,
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                        .navigationBarsPadding() // Tránh bị che bởi thanh điều hướng của điện thoại
                ) {
                    BrosButton(
                        text = "Add to Cart  |  ${formatCurrency(product.price)}", // Hiển thị giá ngay trên nút
                        onClick = onAddToCartClick,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // 1. Ảnh Header Full Màn hình
            ProductImageHeader(
                imageUrl = product.imageUrl,
                imageRes = product.imageRes,
                isFavorite = isFavorite,
                onBackClick = onBackClick,
                onFavoriteClick = onFavoriteClick
            )

            // 2. Nội dung chi tiết (Bo góc tròn đẩy lên trên ảnh)
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = (-24).dp), // Đẩy lên đè nhẹ vào ảnh
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                color = CoffeeBackground
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    // Tên & Đánh giá
                    ProductHeader(
                        title = product.name,
                        subtitle = product.category,
                        rating = rating,
                        ratingCountText = ratingCountText
                    )

                    // Mô tả
                    DescriptionSection(description = product.description)

                    HorizontalDivider(color = Color.Gray.copy(alpha = 0.2f))

                    // Chọn Size
                    SizeSection(
                        sizes = availableSizes,
                        selectedSize = selectedSize,
                        onSizeSelected = onSizeSelected
                    )

                    // Chọn Sữa (Dairy)
                    DairySection(
                        options = availableDairy,
                        selectedOption = selectedDairy,
                        onOptionSelected = onDairySelected
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

// --- CÁC COMPONENT UI CON (HELPER) ---

@Composable
private fun ProductImageHeader(
    imageUrl: String,
    imageRes: Int?,
    isFavorite: Boolean,
    onBackClick: () -> Unit,
    onFavoriteClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(380.dp) // Cao hơn chút cho đẹp
    ) {
        // Xử lý hiển thị ảnh: Ưu tiên URL > Resource ID
        // Dùng Coil để load
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(if (imageUrl.isNotEmpty()) imageUrl else imageRes)
                .crossfade(true)
                .build(),
            contentDescription = "Product Image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Gradient đen mờ ở trên cùng (để nút Back dễ nhìn)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .align(Alignment.TopCenter)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Black.copy(alpha = 0.4f), Color.Transparent)
                    )
                )
        )

        // Nút Back và Favorite
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 48.dp) // Padding cho tai thỏ
                .align(Alignment.TopCenter),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            SmallIconBtn(
                icon = Icons.AutoMirrored.Filled.ArrowBack,
                onClick = onBackClick
            )

            SmallIconBtn(
                icon = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                onClick = onFavoriteClick,
                tint = if (isFavorite) Color.Red else CoffeeBrown
            )
        }
    }
}

@Composable
fun SmallIconBtn(icon: androidx.compose.ui.graphics.vector.ImageVector, onClick: () -> Unit, tint: Color = CoffeeBrown) {
    Surface(
        onClick = onClick,
        shape = CircleShape,
        color = Color.White.copy(alpha = 0.9f),
        shadowElevation = 4.dp
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.padding(10.dp),
            tint = tint
        )
    }
}

@Composable
private fun ProductHeader(title: String, subtitle: String, rating: Float, ratingCountText: String) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Gray,
                modifier = Modifier.weight(1f)
            )
            RatingBar(rating = rating, ratingCountText = ratingCountText)
        }
    }
}

@Composable
private fun RatingBar(rating: Float, ratingCountText: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        modifier = Modifier.background(Color.White, RoundedCornerShape(8.dp)).padding(4.dp)
    ) {
        Icon(Icons.Default.Star, contentDescription = null, tint = StarYellow, modifier = Modifier.size(18.dp))
        Text(text = "$rating", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
        Text(text = ratingCountText, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
    }
}

@Composable
private fun DescriptionSection(description: String) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(text = "Description", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Color.Black)
        Text(
            text = description,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Black.copy(0.7f),
            lineHeight = 22.sp
        )
    }
}

@Composable
private fun SizeSection(sizes: List<String>, selectedSize: String, onSizeSelected: (String) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(text = "Choose Size", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Color.Black)
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            sizes.forEach { size ->
                SizeChip(
                    text = size,
                    subtext = when (size) { "S" -> "Small"; "M" -> "Medium"; "L" -> "Large"; else -> "" },
                    isSelected = size == selectedSize,
                    onClick = { onSizeSelected(size) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun SizeChip(text: String, subtext: String, isSelected: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Surface(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = if (isSelected) CoffeeBrown else Color.White,
        border = BorderStroke(width = 1.dp, color = if (isSelected) CoffeeBrown else Color.LightGray),
        shadowElevation = if (isSelected) 4.dp else 0.dp
    ) {
        Column(
            modifier = Modifier.padding(vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = text, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = if (isSelected) Color.White else Color.Black)
            Text(text = subtext, style = MaterialTheme.typography.labelSmall, color = if (isSelected) Color.White.copy(alpha = 0.8f) else Color.Gray)
        }
    }
}

@Composable
private fun DairySection(options: List<Pair<String, Double>>, selectedOption: String, onOptionSelected: (String) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(text = "Dairy Choice", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Color.Black)
        Column(
            modifier = Modifier
                .background(Color.White, RoundedCornerShape(12.dp))
                .padding(vertical = 8.dp)
        ) {
            options.forEachIndexed { index, (name, price) ->
                DairyOption(name = name, price = price, isSelected = name == selectedOption, onClick = { onOptionSelected(name) })
                if (index < options.size - 1) HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = Color.Gray.copy(alpha = 0.1f))
            }
        }
    }
}

@Composable
private fun DairyOption(name: String, price: Double, isSelected: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = isSelected,
            onClick = onClick,
            colors = RadioButtonDefaults.colors(selectedColor = CoffeeBrown)
        )
        Text(
            text = name,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(1f).padding(start = 8.dp),
            color = Color.Black
        )
        if(price > 0) {
            Text(text = "+${formatCurrency(price)}", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = CoffeeBrown)
        }
    }
}

// Hàm định dạng tiền tệ VNĐ
fun formatCurrency(amount: Double): String {
    return NumberFormat.getCurrencyInstance(Locale("vi", "VN")).format(amount)
}

// Data Class tạm thời cho Related Product (Nếu chưa có trong Model chính)
data class RelatedProduct(val id: String, val name: String, val subtitle: String, val price: String, val imageUrl: String)