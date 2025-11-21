package vn.edu.ut.hieupm9898.customermobile.ui.components

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
 * Thẻ (Card) hiển thị một món hàng trong Lịch sử Đặt hàng.
 *
 * @param title Tên sản phẩm (Vd: "Iced coffee with icecream").
 * @param description Mô tả (Vd: "with ice cream.").
 * @param size Kích thước (Vd: "16 Oz (medium size)").
 * @param price Giá sản phẩm (Vd: 4.50).
 * @param imageUrl Link URL của ảnh sản phẩm.
 * @param onReorderClick Hàm (lambda) được gọi khi nhấn nút "REORDER".
 */
@Composable
fun OrderHistoryCard(
    title: String,
    description: String,
    size: String,
    price: Double,
    imageUrl: String,
    onReorderClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Định dạng giá tiền
    val formatter = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))
    val formattedPrice = formatter.format(price)

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp), // Bo góc
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onPrimary // Màu nền
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically, // Căn giữa các phần tử
        ) {
            // 1. ẢNH (Bên trái)
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = title,
                modifier = Modifier
                    .size(80.dp) // Kích thước ảnh tròn
                    .clip(CircleShape), // Bo tròn ảnh
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(16.dp))

            // 2. CỘT THÔNG TIN (Ở giữa)
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
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
                Text(
                    text = size,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = formattedPrice,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // 3. NÚT "REORDER" (Bên phải)
            Button(
                onClick = onReorderClick,
                modifier = Modifier.padding(top = 100.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                )
            ) {
                Text(
                    text = "RE-ORDER",
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun OrderHistoryCardPreview() {
    CustomerMobileTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF7F0E5))
                .padding(16.dp)
        ) {
            OrderHistoryCard(
                title = "Iced coffee with icecream",
                description = "with ice cream.",
                size = "16 Oz (medium size)",
                price = 40000.0,
                imageUrl = "https://example.com/image.jpg", // (Ảnh placeholder)
                onReorderClick = {}
            )
        }
    }
}