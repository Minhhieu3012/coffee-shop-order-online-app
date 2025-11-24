package vn.edu.ut.hieupm9898.customermobile.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import vn.edu.ut.hieupm9898.customermobile.ui.theme.CustomerMobileTheme
import java.text.NumberFormat
import java.util.Locale

/**
 * Thẻ hiển thị đơn hàng đang hoạt động (Active Order).
 *
 * @param status Trạng thái đơn hàng (Vd: "Out for delivery").
 * @param estimatedTime Thời gian dự kiến (Vd: "30mins").
 * @param summary Tóm tắt đơn hàng (Vd: "Iced Latte x2...").
 * @param totalPrice Tổng tiền (Vd: 24.02).
 * @param imageUrls Danh sách link ảnh các món trong đơn (tối thiểu 1 ảnh).
 * @param itemCount Tổng số lượng món (để hiển thị "+3").
 * @param onTrackOrderClick Hàm gọi khi nhấn nút "Track order".
 */
@Composable
fun CurrentOrder(
    status: String,
    estimatedTime: String,
    summary: String,
    totalPrice: Double,
    imageUrls: List<String>,
    itemCount: Int,
    onTrackOrderClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Định dạng tiền tệ
    val formatter = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))
    val formattedPrice = formatter.format(totalPrice)

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onPrimary
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // --- PHẦN TRÊN: ẢNH VÀ THÔNG TIN ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                // 1. CỤM ẢNH (Bên trái)
                // Chúng ta dùng Box để xếp các ảnh nhỏ
                Box(
                    modifier = Modifier
                        .width(80.dp)
                        .height(80.dp) // Giới hạn chiều cao vùng ảnh
                ) {
                    // Ảnh chính (To nhất)
                    if (imageUrls.isNotEmpty()) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(imageUrls[0])
                                .crossfade(true)
                                .build(),
                            contentDescription = null,
                            modifier = Modifier
                                .size(64.dp) // Kích thước ảnh to
                                .align(Alignment.TopStart) // Căn góc trên trái
                                .clip(RoundedCornerShape(12.dp)),
                            contentScale = ContentScale.Crop
                        )
                    }

                    // Ảnh phụ (Nhỏ hơn) - Nằm đè lên góc dưới
                    if (imageUrls.size > 1) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(imageUrls[1])
                                .crossfade(true)
                                .build(),
                            contentDescription = null,
                            modifier = Modifier
                                .size(36.dp)
                                .align(Alignment.BottomStart) // Căn dưới trái
                                .offset(y = 10.dp) // Đẩy xuống một chút
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.White) // Viền trắng giả
                                .padding(2.dp), // Độ dày viền
                            contentScale = ContentScale.Crop
                        )
                    }

                    // Số lượng còn lại (+3) - Nằm bên cạnh ảnh phụ
                    if (itemCount > 2) {
                        Surface(
                            modifier = Modifier
                                .size(36.dp)
                                .align(Alignment.BottomEnd) // Căn dưới phải của Box
                                .offset(x = 0.dp, y = 10.dp),
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFFF5F5F5) // Màu xám nhạt
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = "+${itemCount - 2}", // Trừ đi 2 ảnh đã hiện
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Gray
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                // 2. THÔNG TIN (Bên phải)
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    // Trạng thái (Đậm)
                    Text(
                        text = status,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    // Est. delivery
                    Row {
                        Text(
                            text = "Est. delivery",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = estimatedTime,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    // Summary
                    Row(modifier = Modifier.padding(top = 4.dp)) {
                        Text(
                            text = "Order summary:",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = summary,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis // ... nếu dài quá
                        )
                    }

                    // Total price
                    Row(modifier = Modifier.padding(top = 4.dp)) {
                        Text(
                            text = "Total price paid:",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = formattedPrice,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // --- PHẦN DƯỚI: CÁC NÚT ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Nút "Track order" (Chiếm phần lớn)
                Button(
                    onClick = onTrackOrderClick,
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
                ) {
                    Text(
                        text = "Track order",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Nút "Ba chấm" (More)
                Surface(
                    onClick = { /* TODO: Menu */ },
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.primary,
                    shadowElevation = 2.dp,
                    border = null, // Hoặc BorderStroke nếu cần
                    modifier = Modifier.size(48.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "More options",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true, name = "New Order Placed")
@Composable
fun NewOrderPreview() {
    CustomerMobileTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF7F0E5))
                .padding(16.dp)
        ) {
            CurrentOrder(
                // Trạng thái thay đổi thành "Đã đặt hàng"
                status = "Order Placed Successfully",

                // Thời gian dự kiến
                estimatedTime = "15mins",

                // Tóm tắt món
                summary = "1x Cold Coffee, 1x Croissant",

                // Tổng tiền
                totalPrice = 8.50,

                // Ảnh giả lập
                imageUrls = listOf("https://example.com/coffee.jpg"),

                // Số lượng món
                itemCount = 2,

                // Sự kiện nhấn nút Theo dõi
                onTrackOrderClick = {}
            )
        }
    }
}