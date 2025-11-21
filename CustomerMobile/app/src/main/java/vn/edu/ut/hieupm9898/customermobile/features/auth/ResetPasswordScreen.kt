package vn.edu.ut.hieupm9898.customermobile.features.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
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
import vn.edu.ut.hieupm9898.customermobile.R

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
                popUpTo(AppRoutes.AUTH_GRAPH) { inclusive = true }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {

        // LỚP 1: FORM
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = BrosBrown,
                                modifier = Modifier.size(60.dp)
                            )
                        }
                    },

                    title = {
                        Image(
                            painter = painterResource(id = R.drawable.logo),
                            contentDescription = "Bros Café Logo",
                            modifier = Modifier.height(80.dp)
                        )
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

                Text("Đặt lại mật khẩu của bạn", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = BrosTitle)
                Spacer(modifier = Modifier.height(12.dp))
                Text("Vui lòng nhập mật khẩu mới của bạn", fontSize = 16.sp, color = Color.Gray)

                Spacer(modifier = Modifier.height(40.dp))

                // Ô nhập Mật khẩu mới
                Text("Mật khẩu mới", fontWeight = FontWeight.Bold, color = BrosTitle, fontSize = 22.sp)
                Spacer(modifier = Modifier.height(8.dp))
                BrosTextField(
                    value = newPassword,
                    onValueChange = { newPassword = it },
                    label = "Nhập mật khẩu",
                    isPassword = true
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Ô nhập Xác nhận mật khẩu
                Text("Xác nhận mật khẩu", fontWeight = FontWeight.Bold, color = BrosTitle, fontSize = 22.sp)
                Spacer(modifier = Modifier.height(8.dp))
                BrosTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = "Nhập mật khẩu",
                    isPassword = true
                )

                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    "*LƯU Ý: Chọn mật khẩu dễ nhớ nhưng khác với mật khẩu cũ của bạn.",
                    fontSize = 16.sp,
                    color = Color.Gray,
                    lineHeight = 16.sp
                )

                Spacer(modifier = Modifier.weight(1f))

                // Nút Proceed
                BrosButton(
                    text = "Tiếp tục",
                    onClick = {
                        // Kích hoạt StatusScreen
                        isResetSuccess = true
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                )

                Spacer(modifier = Modifier.height(60.dp))
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