package vn.edu.ut.hieupm9898.customermobile.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import vn.edu.ut.hieupm9898.customermobile.ui.theme.CustomerMobileTheme
import java.text.NumberFormat
import java.util.Locale

/**
 * Thẻ (Card) hiển thị thông tin 1 sản phẩm cà phê.
 *
 * @param title Tên sản phẩm.
 * @param subtitle Mô tả phụ.
 * @param price Giá sản phẩm.
 * @param imageUrl Link ảnh (có thể rỗng).
 * @param isFavorite Trạng thái yêu thích.
 * @param isOutOfStock Trạng thái hết hàng. ✅ MỚI THÊM
 * @param onCardClick Click vào toàn bộ card.
 * @param onFavoriteClick Click nút tim.
 * @param onAddClick Click nút +.
 */
@Composable
fun CoffeeCard(
    title: String,
    subtitle: String,
    price: Double,
    imageUrl: String,
    isFavorite: Boolean,
    onCardClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    onAddClick: () -> Unit,
    modifier: Modifier = Modifier,
    isOutOfStock: Boolean = false // ✅ Modifier phải là optional parameter cuối cùng
) {
    // Định dạng giá tiền
    val formatter = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))
    val formattedPrice = formatter.format(price)

    Card(
        modifier = modifier
            .width(220.dp)
            .alpha(if (isOutOfStock) 0.6f else 1f) // ✅ Làm mờ nếu hết hàng
            .clickable(enabled = !isOutOfStock) { // ✅ Disable click nếu hết hàng
                onCardClick()
            },
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                // ✅ BOX CHỨA ẢNH VÀ NHÃN HẾT HÀNG
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                ) {
                    // Ảnh sản phẩm
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(if (imageUrl.isNotBlank()) imageUrl else null)
                            .crossfade(true)
                            .build(),
                        contentDescription = title,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                            .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)),
                        contentScale = ContentScale.Crop
                    )

                    // ✅ NHÃN HẾT HÀNG (Overlay trên ảnh)
                    if (isOutOfStock) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(1f)
                                .background(Color.Black.copy(alpha = 0.7f))
                                .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "HẾT HÀNG",
                                color = Color.White,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                Column(
                    modifier = Modifier.padding(
                        top = 16.dp,
                        start = 16.dp,
                        end = 16.dp,
                        bottom = 36.dp
                    )
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground,
                        maxLines = 1
                    )
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.secondary
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = formattedPrice,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            // Nút tim (vẫn hiển thị khi hết hàng)
            IconButton(
                onClick = onFavoriteClick,
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
                Icon(
                    imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Yêu thích",
                    tint = if (isFavorite) Color.Red else Color.White
                )
            }

            // ✅ Nút + (Ẩn khi hết hàng)
            if (!isOutOfStock) {
                IconButton(
                    onClick = onAddClick,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp)
                        .size(40.dp),
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = Color.White
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Thêm vào giỏ hàng"
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CoffeeCardNormalPreview() {
    CustomerMobileTheme {
        Column(modifier = Modifier.padding(32.dp)) {
            CoffeeCard(
                title = "Cold coffee frappe",
                subtitle = "90mg Caffeine : 100 Cal",
                price = 35000.0,
                imageUrl = "",
                isFavorite = true,
                isOutOfStock = false,
                onCardClick = {},
                onFavoriteClick = {},
                onAddClick = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CoffeeCardOutOfStockPreview() {
    CustomerMobileTheme {
        Column(modifier = Modifier.padding(32.dp)) {
            CoffeeCard(
                title = "Iced Latte",
                subtitle = "120mg Caffeine : 150 Cal",
                price = 42000.0,
                imageUrl = "",
                isFavorite = false,
                isOutOfStock = true, // ✅ HẾT HÀNG
                onCardClick = {},
                onFavoriteClick = {},
                onAddClick = {}
            )
        }
    }
}