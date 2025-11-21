package vn.edu.ut.hieupm9898.customermobile.ui.components

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview // <-- Đã thêm import này
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import vn.edu.ut.hieupm9898.customermobile.ui.theme.BrosBrown

@Composable
fun BrosButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(50.dp), // Chiều cao chuẩn
        enabled = isEnabled,
        shape = RoundedCornerShape(12.dp), // Bo góc chuẩn
        // --- SỬA MÀU TÍM THÀNH NÂU ---
        colors = ButtonDefaults.buttonColors(
            containerColor = BrosBrown, // Nền màu nâu
            contentColor = Color.White, // Chữ màu trắng
            disabledContainerColor = Color.Gray
        )
    ) {
        Text(
            text = text,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

// --- PHẦN PREVIEW ---
@Preview(showBackground = true)
@Composable
fun BrosButtonPreview() {
    BrosButton(
        text = "Log In",
        onClick = {}
    )
}