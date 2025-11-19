package vn.edu.ut.hieupm9898.customermobile.ui.components

import androidx.compose.foundation.layout.Column // Import cho Preview
import androidx.compose.foundation.layout.Spacer // Import cho Preview
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height // Import cho Preview
import androidx.compose.foundation.layout.padding // Import cho Preview
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview // Import quan trọng
import androidx.compose.ui.unit.dp
import vn.edu.ut.hieupm9898.customermobile.ui.theme.BrosBrown

@Composable
fun BrosTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    isPassword: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    var passwordVisible by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(text = label) },
        singleLine = true,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = BrosBrown,
            focusedLabelColor = BrosBrown,
            cursorColor = BrosBrown
        ),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        visualTransformation = if (isPassword && !passwordVisible) PasswordVisualTransformation() else VisualTransformation.None,
        trailingIcon = if (isPassword) {
            {
                val image = if (passwordVisible)
                    Icons.Filled.Visibility
                else
                    Icons.Filled.VisibilityOff

                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(imageVector = image, contentDescription = null)
                }
            }
        } else null
    )
}

// --- PHẦN PREVIEW ---
@Preview(showBackground = true)
@Composable
fun BrosTextFieldPreview() {
    Column(modifier = Modifier.padding(16.dp)) {
        // 1. Ô nhập thường
        BrosTextField(
            value = "hieupm9898@ut.edu.vn",
            onValueChange = {},
            label = "Email"
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 2. Ô nhập mật khẩu
        BrosTextField(
            value = "password123",
            onValueChange = {},
            label = "Password",
            isPassword = true
        )
    }
}