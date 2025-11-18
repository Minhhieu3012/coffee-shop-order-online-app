// features/auth/LoginSuccessDialog.kt

package vn.edu.ut.hieupm9898.customermobile.features.auth

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import vn.edu.ut.hieupm9898.customermobile.ui.components.StatusDialogContent // ⚠️ THAY THẾ
import vn.edu.ut.hieupm9898.customermobile.ui.theme.BrosBrown
import vn.edu.ut.hieupm9898.customermobile.ui.theme.CustomerMobileTheme
import vn.edu.ut.hieupm9898.customermobile.ui.theme.BrosTitle
import vn.edu.ut.hieupm9898.customermobile.ui.theme.BrosHeader // Thêm màu trang trí nếu cần

/**
 * Hiển thị Dialog thông báo đăng nhập thành công.
 *
 * @param onDismissRequest Hàm gọi khi Dialog bị hủy (nhấn ra ngoài).
 * @param onComplete Hàm gọi sau khi timeout, dùng để điều hướng sang HomeScreen.
 */
@Composable
fun LoginSuccessDialog(
    onDismissRequest: () -> Unit,
    onComplete: () -> Unit, // Điều hướng sang Home
    timeoutMillis: Long = 2000 // Tự động chuyển hướng sau 2 giây
) {
    Dialog(onDismissRequest = onDismissRequest) {
        // Tích hợp component StatusDialogContent đã tạo
        StatusDialogContent(
            // Dữ liệu cho Login Success
            icon = Icons.Default.Person,
            title = "Đăng nhập",
            statusText = "thành công !",
            subtitle = "Bạn sẽ sớm được chuyển hướng đến trang chủ.",

            // Màu sắc tùy chỉnh (dựa trên các màu Bros của bạn)
            iconBackgroundColor = BrosBrown, // Màu nâu đậm cho nền icon
            titleColor = BrosTitle,
            decorativeDotsColor = BrosHeader, // Dùng BrosHeader hoặc màu tương tự cho chấm trang trí

            // Logic tự động điều hướng
            onTimeout = onComplete,
            timeoutMillis = timeoutMillis
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun LoginSuccessDialogPreview() {
    CustomerMobileTheme {
        LoginSuccessDialog(
            onDismissRequest = {},
            onComplete = {},
            timeoutMillis = 99999 // Đặt timeout cao để xem Preview
        )
    }
}