package vn.edu.ut.hieupm9898.customermobile.ui.components

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import vn.edu.ut.hieupm9898.customermobile.ui.theme.CustomerMobileTheme
import java.text.NumberFormat
import java.util.Locale

/**
 * Thẻ (Card) hiển thị thông tin 1 sản phẩm cà phê, khớp với thiết kế mới.
 *
 * @param title Tên sản phẩm (Vd: "Cold coffee frappe").
 * @param subtitle Mô tả phụ (Vd: "90mg Caffeine : 100 Cal").
 * @param price Giá sản phẩm (Vd: 3.55).
 * @param imageUrl Link URL của ảnh sản phẩm.
 * @param isFavorite Trạng thái (đã được yêu thích hay chưa).
 * @param onCardClick Hàm (lambda) được gọi khi nhấn vào *toàn bộ* thẻ.
 * @param onFavoriteClick Hàm (lambda) được gọi khi nhấn vào nút Yêu thích (❤️).
 * @param onAddClick Hàm (lambda) được gọi khi nhấn vào nút Thêm (+).
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
    modifier: Modifier = Modifier
) {
    // Định dạng giá tiền (Vd: $3.55)
    val formatter = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))
    val formattedPrice = formatter.format(price)

    Card(
        modifier = modifier
            .width(220.dp)
            .clickable { onCardClick() },
        shape = RoundedCornerShape(24.dp), // Bo góc 
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        // Box cho phép xếp chồng các phần tử (ảnh, nút tim, nút +)
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            // ----- PHẦN NỀN (Ảnh và Text) -----
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                // (1) PHẦN ẢNH
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(imageUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f) // Giữ tỉ lệ 1:1 (vuông)
                        .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)),
                    contentScale = ContentScale.Crop
                )

                // (2) PHẦN TEXT (TÊN, MÔ TẢ, GIÁ)
                Column(
                    modifier = Modifier.padding(
                        top = 16.dp,
                        start = 16.dp,
                        end = 16.dp,
                        bottom = 36.dp // "né" nút (+)
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

            // ----- PHẦN ĐÈ LÊN (Overlays) -----

            // (3) NÚT YÊU THÍCH
            // Nút này được đặt ở góc trên bên phải (TopEnd) của Box
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

            // (4) NÚT THÊM (+)
            // Nút này được đặt ở góc dưới bên phải (BottomEnd) của Box
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


@Preview(showBackground = true)
@Composable
fun CoffeeCardUpdatedPreview() {
    CustomerMobileTheme {
        Column(modifier = Modifier.padding(32.dp)) {
            CoffeeCard(
                title = "Cold coffee frappe",
                subtitle = "90mg Caffeine : 100 Cal",
                price = 35000.0,
                imageUrl = "https://example.com/image.jpg", // (Ảnh placeholder)
                isFavorite = true, // Test trạng thái "đã thích"
                onCardClick = {},
                onFavoriteClick = {},
                onAddClick = {}
            )
        }
    }
}