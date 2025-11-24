package vn.edu.ut.hieupm9898.customermobile.features.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.delay

import vn.edu.ut.hieupm9898.customermobile.navigation.AppRoutes
import vn.edu.ut.hieupm9898.customermobile.ui.components.BrosButton
import vn.edu.ut.hieupm9898.customermobile.ui.components.BrosTextField
import vn.edu.ut.hieupm9898.customermobile.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordScreen(
    navController: NavHostController,
    viewModel: AuthViewModel = hiltViewModel()
) {
    var email by remember { mutableStateOf("") }
    val uiState by viewModel.uiState.collectAsState()

    // ✅ Theo dõi sự kiện điều hướng
    LaunchedEffect(Unit) {
        viewModel.navEvent.collect { event ->
            when (event) {
                is AuthNavEvent.NavigateToLogin -> {
                    // Quay về màn hình Login sau khi gửi email thành công
                    navController.navigate(AppRoutes.LOGIN) {
                        popUpTo(AppRoutes.FORGOT_PASSWORD) { inclusive = true }
                    }
                }
                else -> {}
            }
        }
    }

    // ✅ Hiển thị dialog thành công và chuyển trang
    LaunchedEffect(uiState.isEmailSent) {
        if (uiState.isEmailSent) {
            delay(3000) // Hiển thị thông báo 3 giây
            viewModel.resetEmailSentState()
            navController.navigate(AppRoutes.LOGIN) {
                popUpTo(AppRoutes.FORGOT_PASSWORD) { inclusive = true }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
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
                colors = TopAppBarDefaults.topAppBarColors(containerColor = BrosBackground)
            )
        },
        containerColor = BrosBackground
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 24.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.Start
            ) {
                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Quên mật khẩu?",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = BrosTitle
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Vui lòng nhập Email đã đăng ký để chúng tôi có thể gửi link đặt lại mật khẩu cho bạn.",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(40.dp))

                // Input Email
                Text(
                    text = "Email",
                    fontWeight = FontWeight.Bold,
                    color = BrosTitle,
                    fontSize = 20.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                BrosTextField(
                    value = email,
                    onValueChange = {
                        email = it
                        viewModel.clearError() // Xóa lỗi khi user nhập lại
                    },
                    label = "Nhập email của bạn",
                    icon = Icons.Default.Email,
                    keyboardType = KeyboardType.Email
                )

                Spacer(modifier = Modifier.height(16.dp))

                // ✅ Hiển thị thông báo lỗi
                uiState.errorMessage?.let { error ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFFFEBEE)
                        )
                    ) {
                        Text(
                            text = error,
                            color = Color(0xFFC62828),
                            fontSize = 14.sp,
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }

                // ✅ Hiển thị thông báo thành công
                uiState.successMessage?.let { message ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFE8F5E9)
                        )
                    ) {
                        Text(
                            text = message,
                            color = Color(0xFF2E7D32),
                            fontSize = 14.sp,
                            modifier = Modifier.padding(12.dp),
                            lineHeight = 20.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }

                Spacer(modifier = Modifier.weight(1f))

                // Button Gửi Email
                BrosButton(
                    text = if (uiState.isLoading) "Đang gửi..." else "Gửi email",
                    onClick = {
                        viewModel.sendResetPasswordEmail(email.trim())
                    },
                    enabled = !uiState.isLoading && !uiState.isEmailSent,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                )

                Spacer(modifier = Modifier.height(80.dp))
            }

            // ✅ Loading Overlay
            if (uiState.isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = BrosBrown)
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun ForgotPasswordPreview() {
    ForgotPasswordScreen(navController = rememberNavController())
}