package vn.edu.ut.hieupm9898.customermobile.features.auth

import androidx.compose.runtime.Composable
import vn.edu.ut.hieupm9898.customermobile.ui.components.BrosSuccessDialog

@Composable
fun SignUpSuccessDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit
) {
    BrosSuccessDialog(
        showDialog = showDialog,
        onDismissRequest = onDismiss,
        title = "Sign up\nSuccessful",
        message = "Please wait...\nYou will be directed to the homepage soon."
    )
}