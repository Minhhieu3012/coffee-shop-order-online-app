package vn.edu.ut.hieupm9898.customermobile.features.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import vn.edu.ut.hieupm9898.customermobile.ui.components.BrosButton
import vn.edu.ut.hieupm9898.customermobile.ui.theme.CustomerMobileTheme

@Composable
fun AuthSuccessDialog(
    title: String = "Success!",
    message: String,
    buttonText: String = "Continue",
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(24.dp),
        icon = {
            Icon(
                Icons.Default.CheckCircle,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(60.dp)
            )
        },
        title = {
            Text(title, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.headlineSmall)
        },
        text = {
            Text(message, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
        },
        confirmButton = {
            BrosButton(
                text = buttonText,
                onClick = onDismiss,
                modifier = Modifier.fillMaxWidth()
            )
        }
    )
}

@Composable
fun LoginSuccessDialog(onDismiss: () -> Unit = {}) {
    AuthSuccessDialog(title = "Login Successful", message = "Welcome back! You have successfully logged in.", onDismiss = onDismiss)
}

@Composable
fun RegisterSuccessDialog(onDismiss: () -> Unit = {}) {
    AuthSuccessDialog(title = "Account Created", message = "Your account has been created successfully.", onDismiss = onDismiss)
}

@Preview(showBackground = true, name = "Login Success")
@Composable
fun LoginSuccessPreview() {
    CustomerMobileTheme {
        LoginSuccessDialog()
    }
}

@Preview(showBackground = true, name = "Register Success")
@Composable
fun RegisterSuccessPreview() {
    CustomerMobileTheme {
        RegisterSuccessDialog()
    }
}