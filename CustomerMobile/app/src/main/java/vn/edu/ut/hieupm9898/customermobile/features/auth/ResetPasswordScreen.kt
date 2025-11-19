package vn.edu.ut.hieupm9898.customermobile.features.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import vn.edu.ut.hieupm9898.customermobile.navigation.AppRoutes
import vn.edu.ut.hieupm9898.customermobile.ui.components.BrosButton
import vn.edu.ut.hieupm9898.customermobile.ui.components.BrosTextField
import vn.edu.ut.hieupm9898.customermobile.ui.components.StatusScreen
import vn.edu.ut.hieupm9898.customermobile.ui.theme.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResetPasswordScreen(navController: NavController) {
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isResetSuccess by remember { mutableStateOf(false) }

    // Xử lý chuyển trang sau khi thành công
    LaunchedEffect(isResetSuccess) {
        if (isResetSuccess) {
            // Chuyển về màn Login
            navController.navigate(AppRoutes.LOGIN) {
                popUpTo(AppRoutes.AUTH_FLOW) { inclusive = true }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {

        // LỚP 1: FORM
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {},
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = BrosBrown)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = BrosBackground)
                )
            },
            containerColor = BrosBackground
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Spacer(modifier = Modifier.height(10.dp))

                Text("Reset Your Password", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = BrosTitle)
                Spacer(modifier = Modifier.height(12.dp))
                Text("Please enter your new password", fontSize = 14.sp, color = Color.Gray)

                Spacer(modifier = Modifier.height(40.dp))

                // Ô nhập Mật khẩu mới
                Text("New Password", fontWeight = FontWeight.Bold, color = BrosTitle)
                Spacer(modifier = Modifier.height(8.dp))
                BrosTextField(
                    value = newPassword,
                    onValueChange = { newPassword = it },
                    label = "Enter Password",
                    isPassword = true
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Ô nhập Xác nhận mật khẩu
                Text("Confirm Password", fontWeight = FontWeight.Bold, color = BrosTitle)
                Spacer(modifier = Modifier.height(8.dp))
                BrosTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = "Confirm Password",
                    isPassword = true
                )

                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    "*NOTE: Choose a password that is distinctive but distinct from your old password.",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    lineHeight = 16.sp
                )

                Spacer(modifier = Modifier.weight(1f))

                // Nút Proceed
                BrosButton(
                    text = "Proceed",
                    onClick = {
                        // Kích hoạt StatusScreen
                        isResetSuccess = true
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(40.dp))
            }
        }

        // LỚP 2: STATUS SCREEN (Hiện lên khi Reset thành công)
        if (isResetSuccess) {
            StatusScreen(
                icon = Icons.Default.Lock, // Icon Ổ khóa
                title = "Reset Password",
                statusText = "Successful !",
                subtitle = "Please wait...\nYou will be directed to the homepage soon."
                // StatusScreen sẽ tự xử lý timeout (2s) và gọi onTimeout ở trên
            )
        }
    }
}
@Preview(showSystemUi = true)
@Composable
fun ResetPasswordPreview() {
    ResetPasswordScreen(navController = rememberNavController())
}