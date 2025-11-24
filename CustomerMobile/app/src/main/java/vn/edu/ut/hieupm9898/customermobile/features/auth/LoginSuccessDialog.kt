package vn.edu.ut.hieupm9898.customermobile.features.auth

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import vn.edu.ut.hieupm9898.customermobile.ui.components.BrosSuccessDialog


@Composable
fun LoginSuccessDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit
) {

    BrosSuccessDialog(
        showDialog = showDialog,
        onDismissRequest = onDismiss,
        title = "Đăng nhập thành công",
        message = "Vui lòng chờ... Bạn sẽ sớm được chuyển hướng đến trang chủ."
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoginSuccessDialogPreview() {
    LoginSuccessDialog(
        showDialog = true,
        onDismiss = {}
    )
}


