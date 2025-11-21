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

// --- 1. WRAPPER FULL MÀN HÌNH (LỚP PHỦ) ---
@Composable
fun StatusScreen(
    icon: ImageVector,
    title: String,
    statusText: String,
    subtitle: String,
    onTimeout: () -> Unit = {},
    // Các tham số màu sắc (Mặc định là Nâu)
    iconBackgroundColor: Color = Color(0xFFC4B5A0),
    iconTint: Color = Color.White,
    titleColor: Color = Color(0xFFC4B5A0),
    decorativeDotsColor: Color = Color(0xFFD4C4B0)
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.6f)), // Nền tối mờ
        contentAlignment = Alignment.Center
    ) {
        StatusDialogContent(
            icon = icon,
            title = title,
            statusText = statusText,
            subtitle = subtitle,
            onTimeout = onTimeout,
            timeoutMillis = 2000,
            iconBackgroundColor = iconBackgroundColor,
            iconTint = iconTint,
            titleColor = titleColor,
            decorativeDotsColor = decorativeDotsColor
        )
    }
}

// --- 2. NỘI DUNG THẺ (CARD - PHIÊN BẢN CŨ) ---
@Composable
fun StatusDialogContent(
    icon: ImageVector,
    title: String,
    statusText: String,
    subtitle: String,
    modifier: Modifier = Modifier,
    iconBackgroundColor: Color,
    iconTint: Color,
    titleColor: Color,
    decorativeDotsColor: Color,
    cardElevation: Dp = 4.dp,
    onTimeout: (() -> Unit)? = null,
    timeoutMillis: Long = 3000
) {
    LaunchedEffect(key1 = Unit) {
        if (onTimeout != null) {
            delay(timeoutMillis)
            onTimeout()
        }
    }

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
                .padding(vertical = 60.dp), // Padding cũ rộng rãi
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp) // Khoảng cách đều nhau
        ) {
            // Icon & Dots
            StatusIcon(
                icon = icon,
                iconBackgroundColor = iconBackgroundColor,
                iconTint = iconTint,
                decorativeDotsColor = decorativeDotsColor
            )

            // Tiêu đề
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
                color = Color.Gray,
                textAlign = TextAlign.Center,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            CircularProgressIndicator(
                modifier = Modifier.size(48.dp),
                color = titleColor,
                strokeWidth = 4.dp
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
        DecorativeDots(color = decorativeDotsColor)
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
    // Vị trí chấm cũ (xa icon hơn một chút)
    val dotPositions = listOf(
        Offset(-90f, -30f), Offset(80f, -30f),
        Offset(-80f, 30f), Offset(80f, 30f),
        Offset(-100f, 0f), Offset(100f, 0f)
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

// --- 3. PREVIEWS (GIỮ NGUYÊN 3 CÁI ĐỂ BẠN SO SÁNH) ---

@Preview(showBackground = true, showSystemUi = true, name = "1. Login Success")
@Composable
fun PreviewLoginSuccess() {
    StatusScreen(
        icon = Icons.Default.Person,
        title = "Đăng nhập",
        statusText = "thành công !",
        subtitle = "Vui lòng chờ...\nBạn sẽ được chuyển hướng đến trang chủ."
    )
}

@Preview(showBackground = true, showSystemUi = true, name = "2. Reset Password")
@Composable
fun PreviewResetPassword() {
    StatusScreen(
        icon = Icons.Default.Lock,
        title = "Đặt lại mật khẩu",
        statusText = "thành công !",
        subtitle = "Vui lòng chờ...\nBạn sẽ sớm được chuyển hướng đến trang chủ."
    )
}

@Preview(showBackground = true, showSystemUi = true, name = "3. Sign Up Success")
@Composable
fun PreviewSignUpSuccess() {
    val greenPrimary = Color(0xFF4CAF50)
    val greenLight = Color(0xFF81C784)

    StatusScreen(
        icon = Icons.Default.Person,
        title = "Đăng ký",
        statusText = "thành công !",
        subtitle = "Chào mừng! Tài khoản của bạn đã được tạo.",
        iconBackgroundColor = greenPrimary,
        titleColor = greenPrimary,
        decorativeDotsColor = greenLight
    )
}