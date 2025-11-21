package vn.edu.ut.hieupm9898.customermobile.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import vn.edu.ut.hieupm9898.customermobile.ui.theme.CustomerMobileTheme

/**
 * Dialog xác nhận xóa tài khoản (Màn hình cuối cùng trong ảnh)
 */
@Composable
fun DeleteConfirmationDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        shape = RoundedCornerShape(16.dp),
        title = {
            Text(
                text = "Are you sure?",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        text = {
            Text(
                text = "You are about to delete your account. This action cannot be undone.",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Delete", color = Color.White, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color.Gray)
            ) {
                Text("Cancel", color = Color.Black)
            }
        }
    )
}

/**
 * Dialog thông báo gửi phản hồi thành công
 */
@Composable
fun FeedbackSuccessDialog(
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        shape = RoundedCornerShape(16.dp),
        title = {
            Text("Thank You!", fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
        },
        text = {
            Text("Your feedback has been sent successfully.", textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
        },
        confirmButton = {
            // Sử dụng BrosButton để đồng bộ style
            BrosButton(
                text = "OK",
                onClick = onDismiss,
                modifier = Modifier.fillMaxWidth()
            )
        }
    )
}

// --- PHẦN PREVIEW ---

@Preview(showBackground = true, name = "Delete Confirmation")
@Composable
fun DeleteDialogPreview() {
    CustomerMobileTheme {
        DeleteConfirmationDialog(
            onDismiss = {},
            onConfirm = {}
        )
    }
}

@Preview(showBackground = true, name = "Feedback Success")
@Composable
fun FeedbackDialogPreview() {
    CustomerMobileTheme {
        FeedbackSuccessDialog(
            onDismiss = {}
        )
    }
}