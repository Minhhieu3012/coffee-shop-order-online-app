package vn.edu.ut.hieupm9898.customermobile.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import vn.edu.ut.hieupm9898.customermobile.ui.theme.CustomerMobileTheme

@Composable
fun StatusDialogContent(
    icon: ImageVector,
    title: String,
    statusText: String,
    subtitle: String,
    modifier: Modifier = Modifier,
    iconBackgroundColor: Color = Color(0xFFC4B5A0),
    iconTint: Color = Color.White,
    titleColor: Color = Color(0xFFC4B5A0),
    decorativeDotsColor: Color = Color(0xFFD4C4B0),
    cardElevation: Dp = 4.dp,
    onTimeout: (() -> Unit)? = null,
    timeoutMillis: Long = 3000
) {
    // Xử lý timeout
    LaunchedEffect(key1 = Unit) {
        if (onTimeout != null) {
            delay(timeoutMillis)
            onTimeout()
        }
    }

    // Surface: Cái "hộp" màu trắng, bo góc
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp),
        shape = RoundedCornerShape(24.dp),
        color = Color.White,
        shadowElevation = cardElevation
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 60.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Icon với các chấm trang trí xung quanh (giống ảnh)
            StatusIcon(
                icon = icon,
                iconBackgroundColor = iconBackgroundColor,
                iconTint = iconTint,
                decorativeDotsColor = decorativeDotsColor
            )

            // Tiêu đề chính
            Text(
                text = title,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = titleColor,
                textAlign = TextAlign.Center
            )

            // Trạng thái
            Text(
                text = statusText,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = titleColor,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Mô tả phụ
            Text(
                text = subtitle,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                lineHeight = 16.sp
            )
        }
    }
}

@Composable
private fun StatusIcon(
    icon: ImageVector,
    iconBackgroundColor: Color,
    iconTint: Color,
    decorativeDotsColor: Color,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.size(140.dp)
    ) {
        // Các chấm tròn trang trí xung quanh (giống ảnh)
        DecorativeDots(color = decorativeDotsColor)

        // Vòng tròn chính chứa icon
        Box(
            modifier = Modifier
                .size(160.dp)
                .clip(CircleShape)
                .background(iconBackgroundColor),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(80.dp),
                tint = iconTint
            )
        }
    }
}

@Composable
private fun DecorativeDots(color: Color) {
    // Vị trí các chấm xung quanh icon (giống ảnh)
    val dotPositions = listOf(
        Offset(-90f, -30f),  // Trên trái
        Offset(80f, -30f),   // Trên phải
        Offset(-80f, 30f),   // Dưới trái
        Offset(80f, 30f),    // Dưới phải
        Offset(-100f, 0f),    // Trái giữa
        Offset(100f, 0f)      // Phải giữa
    )

    dotPositions.forEach { offset ->
        Box(
            modifier = Modifier
                .offset(offset.x.dp, offset.y.dp)
                .size(12.dp)
                .clip(CircleShape)
                .background(color)
        )
    }
}


@Preview(showBackground = true, name = "Login Successful")
@Composable
fun LoginStatusPreview() {
    CustomerMobileTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Gray.copy(alpha = 0.5f)),
            contentAlignment = Alignment.Center
        ) {
            StatusDialogContent(
                icon = Icons.Default.Person,
                title = "Đăng nhập",
                statusText = "thành công !",
                subtitle = "Vui lòng chờ...\n" +
                        "Bạn sẽ sớm được chuyển hướng đến trang chủ."
            )
        }
    }
}

@Preview(showBackground = true, name = "Reset Password Successful")
@Composable
fun ResetPasswordStatusPreview() {
    CustomerMobileTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Gray.copy(alpha = 0.5f)),
            contentAlignment = Alignment.Center
        ) {
            StatusDialogContent(
                icon = Icons.Default.Lock,
                title = "Đặt lại mật khẩu",
                statusText = "thành công !",
                subtitle = "Vui lòng chờ...\n" +
                        "Bạn sẽ sớm được chuyển hướng đến trang chủ."
            )
        }
    }
}

@Preview(showBackground = true, name = "Custom Color")
@Composable
fun CustomColorStatusPreview() {
    CustomerMobileTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Gray.copy(alpha = 0.5f)),
            contentAlignment = Alignment.Center
        ) {
            StatusDialogContent(
                icon = Icons.Default.Person,
                title = "Đăng ký",
                statusText = "thành công !",
                subtitle = "Chào mừng! Tài khoản của bạn đã được tạo.",
                iconBackgroundColor = Color(0xFF4CAF50),
                titleColor = Color(0xFF4CAF50),
                decorativeDotsColor = Color(0xFF81C784),
            )
        }
    }
}