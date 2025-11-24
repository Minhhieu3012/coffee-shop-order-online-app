package vn.edu.ut.hieupm9898.customermobile.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
 * Thẻ hiển thị sản phẩm trong màn hình Thanh toán (Checkout).
 * Có chức năng tăng giảm số lượng và nút xóa (thùng rác).
 *
 * @param title Tên sản phẩm.
 * @param subtitle1 Dòng mô tả 1 (Vd: "with ice cream.").
 * @param subtitle2 Dòng mô tả 2 (Vd: "20 Oz (large size)").
 * @param price Giá đơn vị (Vd: 3.55).
 * @param imageUrl Link ảnh.
 * @param quantity Số lượng hiện tại.
 * @param onIncrementClick Hàm gọi khi nhấn nút (+).
 * @param onDecrementClick Hàm gọi khi nhấn nút (-).
 * @param onDeleteClick Hàm gọi khi nhấn nút Thùng rác.
 */
@Composable
fun CheckoutItemCard(
    title: String,
    subtitle1: String,
    subtitle2: String,
    price: Double,
    imageUrl: String,
    quantity: Int,
    onIncrementClick: () -> Unit,
    onDecrementClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Định dạng giá tiền (Vd: $3.55)
    val formatter = NumberFormat.getCurrencyInstance(Locale("en", "US"))
    val formattedPrice = formatter.format(price)

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onPrimary
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 1. ẢNH TRÒN (Bên trái)
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = title,
                modifier = Modifier
                    .size(90.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(16.dp))

            // 2. CỘT THÔNG TIN (Ở giữa & Bên phải)
            Column(
                modifier = Modifier.weight(1f)
            ) {
                // Tên sản phẩm
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                // Các dòng mô tả
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
                Spacer(modifier = Modifier.height(8.dp))

                // Giá tiền
                Text(
                    text = formattedPrice,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(18.dp))

                // --- HÀNG CÁC NÚT ĐIỀU KHIỂN ---
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween // Đẩy 2 nhóm nút ra 2 đầu
                ) {
                    // Nhóm 1: Nút Thùng Rác (Trái)
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = Color(0xFF8B4513), // Màu Nâu
                        modifier = Modifier
                            .size(32.dp)
                            .clickable { onDeleteClick() }
                    )

                    // Nhóm 2: Bộ tăng giảm số lượng (Phải)
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Nút Trừ (-)
                        Surface(
                            onClick = onDecrementClick,
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(1.dp, Color.LightGray),
                            color = Color.White,
                            modifier = Modifier.size(32.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Remove,
                                    contentDescription = "Decrease",
                                    modifier = Modifier.size(18.dp),
                                    tint = Color.Gray
                                )
                            }
                        }

                        // Số lượng
                        Text(
                            text = quantity.toString(),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        // Nút Cộng (+)
                        Surface(
                            onClick = onIncrementClick,
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFF8B4513), // Màu Nâu
                            modifier = Modifier.size(32.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Increase",
                                    modifier = Modifier.size(18.dp),
                                    tint = Color.White
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun CheckoutItemCardPreview() {
    CustomerMobileTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF7F0E5)) // Màu nền be
                .padding(16.dp)
        ) {
            CheckoutItemCard(
                title = "Cold Coffee Frappucino",
                subtitle1 = "with ice cream.",
                subtitle2 = "20 Oz (large size)",
                price = 3.55,
                imageUrl = "https://example.com/image.jpg", // Ảnh placeholder
                quantity = 1,
                onIncrementClick = {},
                onDecrementClick = {},
                onDeleteClick = {}
            )
        }
    }
}