package vn.edu.ut.hieupm9898.customermobile.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import java.text.NumberFormat
import java.util.Locale

@Composable
fun OrderHistoryCard(
    title: String,
    description: String,
    size: String,
    price: Double,
    imageUrl: String,
    status: String = "", // 👈 Optional
    orderDate: String = "", // 👈 Optional
    itemCount: Int = 1, // 👈 Optional
    onReorderClick: () -> Unit,
    onCancelClick: (() -> Unit)? = null, // 👈 Optional
    modifier: Modifier = Modifier
) {
    val formatter = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Header: Order date & status
            if (orderDate.isNotEmpty() || status.isNotEmpty()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (orderDate.isNotEmpty()) {
                        Text(
                            text = orderDate,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                    }

                    if (status.isNotEmpty()) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = getStatusColor(status).copy(alpha = 0.1f)
                        ) {
                            Text(
                                text = status,
                                modifier = Modifier.padding(
                                    horizontal = 12.dp,
                                    vertical = 4.dp
                                ),
                                style = MaterialTheme.typography.bodySmall,
                                color = getStatusColor(status),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            // Product info
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Image
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(imageUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = title,
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.LightGray),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.width(16.dp))

                // Info
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    if (description.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = description,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                        ) {
                            Text(
                                text = size,
                                modifier = Modifier.padding(
                                    horizontal = 10.dp,
                                    vertical = 4.dp
                                ),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        if (itemCount > 1) {
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "+${itemCount - 1} món khác",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray
                            )
                        }
                    }
                }

                // Price
                Text(
                    text = formatter.format(price),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Action buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Reorder button
                Button(
                    onClick = onReorderClick,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text("Đặt lại")
                }

                // Cancel button (only for ongoing orders)
                if (onCancelClick != null) {
                    OutlinedButton(
                        onClick = onCancelClick,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        border = ButtonDefaults.outlinedButtonBorder.copy(
                            width = 1.dp
                        ),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Hủy")
                    }
                }
            }
        }
    }
}

/**
 * Get color based on order status
 */
private fun getStatusColor(status: String): Color {
    return when (status) {
        "Hoàn thành" -> Color(0xFF4CAF50)
        "Đang giao hàng" -> Color(0xFF2196F3)
        "Đang chuẩn bị" -> Color(0xFFFF9800)
        "Chờ xác nhận" -> Color(0xFFFFC107)
        "Đã hủy" -> Color(0xFFF44336)
        "Thất bại" -> Color(0xFFE91E63)
        else -> Color.Gray
    }
}