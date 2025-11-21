package vn.edu.ut.hieupm9898.customermobile.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import vn.edu.ut.hieupm9898.customermobile.ui.theme.BrosBrown
import vn.edu.ut.hieupm9898.customermobile.ui.theme.BrosSubTitle

@Composable
fun BrosTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String, // Dùng làm placeholder
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    isPassword: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    var passwordVisible by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        // Đổi từ label sang placeholder
        placeholder = {
            Text(
                text = label,
                color = BrosSubTitle
            )
        },
        singleLine = true,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = BrosBrown,
            unfocusedBorderColor = Color.LightGray,
            cursorColor = BrosBrown
        ),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        visualTransformation = if (isPassword && !passwordVisible) PasswordVisualTransformation() else VisualTransformation.None,
        // Icon bên trái
        leadingIcon = if (icon != null) {
            {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = BrosSubTitle
                )
            }
        } else null,
        // Icon bên phải (cho password)
        trailingIcon = if (isPassword) {
            {
                val image = if (passwordVisible)
                    Icons.Filled.Visibility
                else
                    Icons.Filled.VisibilityOff

                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = image,
                        contentDescription = null,
                        tint = BrosSubTitle
                    )
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
        // 1. Ô nhập thường với icon
        BrosTextField(
            value = "",
            onValueChange = {},
            label = "Nhập tên người dùng",
            icon = Icons.Default.Person
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 2. Ô nhập có giá trị
        BrosTextField(
            value = "hieupm9898@ut.edu.vn",
            onValueChange = {},
            label = "Nhập địa chỉ email"
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 3. Ô nhập mật khẩu
        BrosTextField(
            value = "password123",
            onValueChange = {},
            label = "Nhập mật khẩu",
            isPassword = true
        )
    }
}