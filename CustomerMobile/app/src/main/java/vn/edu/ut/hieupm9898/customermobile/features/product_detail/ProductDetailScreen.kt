package vn.edu.ut.hieupm9898.customermobile.features.product_detail

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import vn.edu.ut.hieupm9898.customermobile.ui.components.BrosButton
import vn.edu.ut.hieupm9898.customermobile.ui.theme.CustomerMobileTheme
import java.text.NumberFormat
import java.util.Locale

// Màu sắc tùy chỉnh
private val CoffeeBackground = Color(0xFFF5F1E8)
private val CoffeeBrown = Color(0xFF6F4E37)
private val StarYellow = Color(0xFFFFC107)

/**
 * Product Detail Screen với UI cải thiện
 * - Gradient overlay trên ảnh
 * - Animation mượt mà
 * - Size selector với icon cốc coffee
 * - Section "People also viewed"
 * - Typography và spacing tốt hơn
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    title: String,
    subtitle: String,
    rating: Float,
    ratingCountText: String,
    description: String,
    imageUrl: String,
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
            Surface(
                shadowElevation = 8.dp,
                color = CoffeeBackground
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 16.dp)
                ) {
                    BrosButton(
                        text = "Add to cart",
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
            // Ảnh Header với gradient overlay đẹp
            ProductImageHeader(
                imageUrl = imageUrl,
                isFavorite = isFavorite,
                onBackClick = onBackClick,
                onFavoriteClick = onFavoriteClick
            )

            // Content section với background bo góc
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                color = CoffeeBackground
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    // Title & Rating
                    ProductHeader(
                        title = title,
                        subtitle = subtitle,
                        rating = rating,
                        ratingCountText = ratingCountText
                    )

                    // Description
                    DescriptionSection(description = description)

                    Spacer(modifier = Modifier.height(4.dp))

                    // Size Selector
                    SizeSection(
                        sizes = availableSizes,
                        selectedSize = selectedSize,
                        onSizeSelected = onSizeSelected
                    )

                    // Dairy Choice
                    DairySection(
                        options = availableDairy,
                        selectedOption = selectedDairy,
                        onOptionSelected = onDairySelected
                    )

                    // Related Products (nếu có)
                    if (relatedProducts.isNotEmpty()) {
                        RelatedProductsSection(
                            products = relatedProducts,
                            onProductClick = onRelatedProductClick
                        )
                    }

                    // Spacing cho bottom button
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

/**
 * Header ảnh với gradient overlay và buttons
 */
@Composable
private fun ProductImageHeader(
    imageUrl: String,
    isFavorite: Boolean,
    onBackClick: () -> Unit,
    onFavoriteClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(360.dp)
    ) {
        // Ảnh sản phẩm
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(imageUrl)
                .crossfade(true)
                .build(),
            contentDescription = "Product Image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Gradient overlay (tối phía trên để button rõ hơn)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.3f),
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.1f)
                        )
                    )
                )
        )

        // Top buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Back button
            Surface(
                onClick = onBackClick,
                shape = CircleShape,
                color = Color.White.copy(alpha = 0.9f),
                shadowElevation = 4.dp
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    modifier = Modifier.padding(14.dp),
                    tint = CoffeeBrown
                )
            }

            // Favorite button
            Surface(
                onClick = onFavoriteClick,
                shape = CircleShape,
                color = Color.White.copy(alpha = 0.9f),
                shadowElevation = 4.dp
            ) {
                Icon(
                    imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Favorite",
                    modifier = Modifier.padding(14.dp),
                    tint = if (isFavorite) Color.Red else CoffeeBrown
                )
            }
        }
    }
}

/**
 * Product title, subtitle và rating
 */
@Composable
private fun ProductHeader(
    title: String,
    subtitle: String,
    rating: Float,
    ratingCountText: String
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodyLarge,
            color = Color.Gray
        )

        // Rating với stars
        RatingBar(rating = rating, ratingCountText = ratingCountText)
    }
}

/**
 * Rating bar với ngôi sao
 */
@Composable
private fun RatingBar(
    rating: Float,
    ratingCountText: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        for (i in 1..5) {
            val star = when {
                rating >= i -> Icons.Default.Star
                rating >= i - 0.5f -> Icons.Default.StarHalf
                else -> Icons.Default.StarBorder
            }
            Icon(
                imageVector = star,
                contentDescription = null,
                tint = StarYellow,
                modifier = Modifier.size(22.dp)
            )
        }
        Text(
            text = ratingCountText,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray,
            modifier = Modifier.padding(start = 4.dp)
        )
    }
}

/**
 * Description section
 */
@Composable
private fun DescriptionSection(description: String) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = "Description",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = description,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            lineHeight = 22.sp
        )
    }
}

/**
 * Size selector với icon cốc coffee
 */
@Composable
private fun SizeSection(
    sizes: List<String>,
    selectedSize: String,
    onSizeSelected: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = "Choose your size",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            sizes.forEach { size ->
                SizeChip(
                    text = size,
                    subtext = when (size) {
                        "Small" -> "12 Oz"
                        "Medium" -> "16 Oz"
                        "Large" -> "20 Oz"
                        else -> ""
                    },
                    isSelected = size == selectedSize,
                    onClick = { onSizeSelected(size) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

/**
 * Size chip component với coffee cup icon
 */
@Composable
private fun SizeChip(
    text: String,
    subtext: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = if (isSelected) CoffeeBrown else Color.White,
        border = BorderStroke(
            width = if (isSelected) 0.dp else 1.dp,
            color = Color.Gray.copy(alpha = 0.3f)
        ),
        shadowElevation = if (isSelected) 4.dp else 0.dp
    ) {
        Column(
            modifier = Modifier.padding(vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            // Coffee cup icon
            Icon(
                imageVector = Icons.Default.LocalCafe,
                contentDescription = null,
                tint = if (isSelected) Color.White else Color.Gray,
                modifier = Modifier.size(28.dp)
            )

            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = if (isSelected) Color.White else Color.Gray
            )

            Text(
                text = subtext,
                style = MaterialTheme.typography.bodySmall,
                color = if (isSelected) Color.White.copy(alpha = 0.8f) else Color.Gray
            )
        }
    }
}

/**
 * Dairy choice section
 */
@Composable
private fun DairySection(
    options: List<Pair<String, Double>>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = "Dairy Choice",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Surface(
            shape = RoundedCornerShape(12.dp),
            color = Color.White,
            shadowElevation = 2.dp
        ) {
            Column(modifier = Modifier.padding(vertical = 8.dp)) {
                options.forEachIndexed { index, (name, price) ->
                    DairyOption(
                        name = name,
                        price = price,
                        isSelected = name == selectedOption,
                        onClick = { onOptionSelected(name) }
                    )

                    if (index < options.size - 1) {
                        HorizontalDivider(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            color = Color.Gray.copy(alpha = 0.2f)
                        )
                    }
                }
            }
        }
    }
}

/**
 * Dairy option row
 */
@Composable
private fun DairyOption(
    name: String,
    price: Double,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val formatter = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))
    val formattedPrice = formatter.format(price)

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
            colors = RadioButtonDefaults.colors(
                selectedColor = CoffeeBrown
            )
        )

        Text(
            text = name,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f),
            color = if (isSelected) CoffeeBrown else Color.Black
        )

        Text(
            text = formattedPrice,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

/**
 * Related products section
 */
@Composable
private fun RelatedProductsSection(
    products: List<RelatedProduct>,
    onProductClick: (RelatedProduct) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = "People also viewed",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(products) { product ->
                RelatedProductCard(
                    product = product,
                    onClick = { onProductClick(product) }
                )
            }
        }
    }
}

/**
 * Related product card
 */
@Composable
private fun RelatedProductCard(
    product: RelatedProduct,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        shadowElevation = 4.dp,
        modifier = Modifier.width(140.dp)
    ) {
        Column {
            // Image
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(product.imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = product.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp),
                contentScale = ContentScale.Crop
            )

            // Info
            Column(
                modifier = Modifier.padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2,
                    color = CoffeeBrown
                )

                Text(
                    text = product.subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    maxLines = 1
                )

                Text(
                    text = product.price,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = CoffeeBrown
                )
            }
        }
    }
}

// Data class cho related products
data class RelatedProduct(
    val id: String,
    val name: String,
    val subtitle: String,
    val price: String,
    val imageUrl: String
)


@Preview(showBackground = true, name = "Product Detail - Improved")
@Composable
fun ImprovedProductDetailPreview() {
    val fakeSizes = listOf("Small", "Medium", "Large")
    val fakeDairy = listOf(
        Pair("Whole Milk", 0.57),
        Pair("Almond Milk", 1.00),
        Pair("Oat Milk", 1.25)
    )
    val fakeRelated = listOf(
        RelatedProduct("1", "Pumpkin Brew", "150mg · 230 Cal", "45000.0", ""),
        RelatedProduct("2", "Iced Coffee Cram", "110mg · 130 Cal", "35000.0", ""),
        RelatedProduct("3", "Cold Brew", "140mg · 120 Cal", "47000.0", "")
    )

    var isFavorite by remember { mutableStateOf(false) }
    var selectedSize by remember { mutableStateOf("Small") }
    var selectedDairy by remember { mutableStateOf("Whole Milk") }

    CustomerMobileTheme {
        ProductDetailScreen(
            title = "Cold coffee frapuccino",
            subtitle = "90mg Caffeine : 100Cal",
            rating = 3.0f,
            ratingCountText = "(3.0)",
            description = "Tasteful and flavorful icecream coffee. Ice cream with whipped cream and caramel syrup.",
            imageUrl = "",
            isFavorite = isFavorite,
            availableSizes = fakeSizes,
            selectedSize = selectedSize,
            availableDairy = fakeDairy,
            selectedDairy = selectedDairy,
            relatedProducts = fakeRelated,
            onBackClick = {},
            onFavoriteClick = { isFavorite = !isFavorite },
            onSizeSelected = { selectedSize = it },
            onDairySelected = { selectedDairy = it },
            onAddToCartClick = {}
        )
    }
}