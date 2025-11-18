package vn.edu.ut.hieupm9898.customermobile.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
 * Thẻ (Card) hiển thị một món hàng yêu thích.
 *
 * @param title Tên sản phẩm.
 * @param subtitle1 Dòng mô tả 1 (Vd: "with whipped cream...").
 * @param subtitle2 Dòng mô tả 2 (Vd: "90mg Caffeine...").
 * @param price Giá sản phẩm.
 * @param imageUrl Link URL của ảnh sản phẩm.
 * @param onAddClick Hàm (lambda) được gọi khi nhấn nút "Add to cart".
 * @param onRemoveClick Hàm (lambda) được gọi khi nhấn nút "Remove".
 */
@Composable
fun FavoriteCard(
    title: String,
    subtitle1: String,
    subtitle2: String,
    price: Double,
    imageUrl: String,
    onAddClick: () -> Unit,
    onRemoveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Định dạng giá tiền
    val formatter = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))
    val formattedPrice = formatter.format(price)

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp), // Bo góc
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onPrimary
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically // Căn giữa các phần tử
        ) {
            // ----- 1. CỘT BÊN TRÁI (Ảnh + Giá) -----
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(end = 8.dp) // Thêm khoảng cách nhỏ bên phải
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(imageUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = title,
                    modifier = Modifier
                        .size(90.dp) // Kích thước ảnh tròn
                        .clip(CircleShape), // Bo tròn ảnh
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = formattedPrice,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface // Màu text chính
                )
            }

            // Thêm một khoảng đệm nhỏ
            Spacer(modifier = Modifier.width(8.dp))

            // ----- 2. CỘT BÊN PHẢI (Thông tin + Nút) -----
            Column(
                modifier = Modifier.weight(1f), // Tự động chiếm không gian còn lại
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = subtitle1,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
                Text(
                    text = subtitle2,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(12.dp))

                // ----- HÀNG CÁC NÚT BẤM -----
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp) // Khoảng cách giữa 2 nút
                ) {
                    // Nút "Add to cart" (Nút đặc)
                    Button(
                        onClick = onAddClick,
                        modifier = Modifier.weight(1f), // Chia đều không gian
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text(
                            text = "Add to cart",
                            style = MaterialTheme.typography.labelLarge
                        )
                    }

                    // Nút "Remove" (Nút viền)
                    OutlinedButton(
                        onClick = onRemoveClick,
                        modifier = Modifier.weight(1f), // Chia đều không gian
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, Color(0xFF8B4513)) // Viền Nâu
                    ) {
                        Text(
                            text = "Remove",
                            style = MaterialTheme.typography.labelLarge,
                            color = Color(0xFF8B4513) // Chữ Nâu
                        )
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun FavoriteCardPreview() {
    CustomerMobileTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF7F0E5))
                .padding(16.dp)
        ) {
            FavoriteCard(
                title = "Cold coffee frapuccino",
                subtitle1 = "with whipped cream and straws.",
                subtitle2 = "90mg Caffeine : 100 Calories",
                price = 40000.0,
                imageUrl = "https://example.com/image.jpg", // (Ảnh placeholder)
                onAddClick = {},
                onRemoveClick = {}
            )
        }
    }
}