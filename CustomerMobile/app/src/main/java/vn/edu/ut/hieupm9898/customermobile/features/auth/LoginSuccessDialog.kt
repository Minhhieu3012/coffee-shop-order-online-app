package vn.edu.ut.hieupm9898.customermobile.features.auth

import androidx.compose.runtime.Composable
import vn.edu.ut.hieupm9898.customermobile.ui.components.BrosSuccessDialog


@Composable
fun LoginSuccessDialog(
    showDialog: Boolean, // Tham số 1
    onDismiss: () -> Unit // Tham số 2
) {

    BrosSuccessDialog(
        showDialog = showDialog,
        onDismissRequest = onDismiss,
        title = "Login Successful",
        message = "Please wait... You will be directed to the homepage soon."
    )
}