package vn.edu.ut.hieupm9898.customermobile.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import vn.edu.ut.hieupm9898.customermobile.ui.theme.CustomerMobileTheme

/**
 * Nút bấm chính (primary button) của ứng dụng.
 * Nó tự động sử dụng màu sắc và kiểu chữ từ Theme.
 *
 * @param text Văn bản hiển thị trên nút.
 * @param onClick Hàm (lambda) sẽ được gọi khi nút được nhấn.
 * @param modifier Tùy chỉnh (modifier) từ bên ngoài (ví dụ: padding).
 */
@Composable
fun BrosButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true // Cho phép tắt (disable) nút
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(54.dp),
        shape = RoundedCornerShape(16.dp), // Bo góc
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ),
        enabled = enabled
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge // Dùng kiểu chữ cho nút
        )
    }
}

@Preview(showBackground = true, name = "Enabled Button")
@Composable
fun BrosButtonPreview() {
    CustomerMobileTheme {
        BrosButton(text = "Đăng nhập", onClick = {})
    }
}

@Preview(showBackground = true, name = "Disabled Button")
@Composable
fun BrosButtonDisabledPreview() {
    CustomerMobileTheme {
        BrosButton(text = "Đăng nhập", onClick = {}, enabled = false)
    }
}