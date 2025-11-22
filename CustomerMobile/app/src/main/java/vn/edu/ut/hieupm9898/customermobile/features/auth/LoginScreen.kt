package vn.edu.ut.hieupm9898.customermobile.features.auth

import android.content.Context
import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

import vn.edu.ut.hieupm9898.customermobile.R
import vn.edu.ut.hieupm9898.customermobile.navigation.AppRoutes
import vn.edu.ut.hieupm9898.customermobile.ui.components.BrosButton
import vn.edu.ut.hieupm9898.customermobile.ui.components.BrosTextField
import vn.edu.ut.hieupm9898.customermobile.ui.components.StatusScreen
import vn.edu.ut.hieupm9898.customermobile.ui.theme.*

@Composable
fun LoginScreen(
    navController: NavHostController,
    onLoginSuccess: () -> Unit = {}
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var rememberMe by remember { mutableStateOf(false) }
    var isLoginSuccess by remember { mutableStateOf(false) }

    val context = LocalContext.current

    Box(modifier = Modifier.fillMaxSize()) {
        // Nền màu nâu nhạt
        Surface(color = BrosBackground, modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()), // Cho phép cuộn nếu màn hình nhỏ
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // 1. LOGO (Chiếm khoảng 30% màn hình phía trên)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.logo),
                        contentDescription = "Logo",
                        modifier = Modifier.size(180.dp)
                    )
                }

                // 2. KHỐI FORM TRẮNG (Bo tròn góc trên)
                Card(
                    shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier.fillMaxSize() // Lấp đầy phần còn lại
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 30.dp, vertical = 24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Đăng nhập", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = BrosTitle)

                        // Link Đăng ký (Ở ngay dưới tiêu đề)
                        Row(
                            modifier = Modifier.padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Bạn không có tài khoản? ", color = BrosSubTitle, fontSize = 16.sp)
                            Text(
                                "Đăng ký",
                                color = BrosBrown,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                modifier = Modifier.clickable {
                                    // [FIX] Điều hướng sang Đăng ký
                                    navController.navigate(AppRoutes.REGISTER)
                                }
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Ô nhập Email
                        BrosTextField(
                            value = email,
                            onValueChange = { email = it },
                            label = "Email",
                            keyboardType = KeyboardType.Email
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        // Ô nhập Password
                        BrosTextField(
                            value = password,
                            onValueChange = { password = it },
                            label = "Password",
                            isPassword = true
                        )

                        // Checkbox & Quên mật khẩu
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Checkbox(
                                    checked = rememberMe,
                                    onCheckedChange = { rememberMe = it },
                                    colors = CheckboxDefaults.colors(checkedColor = BrosBrown)
                                )
                                Text("Lưu đăng nhập", fontSize = 14.sp, color = BrosSubTitle)
                            }
                            Text(
                                "Quên mật khẩu?",
                                fontSize = 14.sp,
                                color = BrosBrown,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.clickable {
                                    // [FIX] Điều hướng sang Quên mật khẩu
                                    navController.navigate(AppRoutes.FORGOT_PASSWORD)
                                }
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // NÚT ĐĂNG NHẬP CHÍNH
                        BrosButton(
                            text = "Đăng nhập",
                            enabled = !isLoginSuccess,
                            onClick = {
                                val cleanEmail = email.trim()
                                val cleanPass = password.trim()

                                if (cleanEmail.isEmpty()) {
                                    Toast.makeText(context, "Vui lòng nhập Email!", Toast.LENGTH_SHORT).show()
                                } else if (!Patterns.EMAIL_ADDRESS.matcher(cleanEmail).matches()) {
                                    Toast.makeText(context, "Email không hợp lệ!", Toast.LENGTH_SHORT).show()
                                } else if (cleanPass.isEmpty()) {
                                    Toast.makeText(context, "Vui lòng nhập Mật khẩu!", Toast.LENGTH_SHORT).show()
                                } else {
                                    isLoginSuccess = true
                                }
                            },
                            modifier = Modifier.fillMaxWidth().height(50.dp)
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        // --- PHẦN CUỐI: HOẶC ĐĂNG NHẬP VỚI ---
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            HorizontalDivider(modifier = Modifier.weight(1f), color = Color.LightGray)
                            Text(" HOẶC ", modifier = Modifier.padding(horizontal = 8.dp), color = BrosSubTitle, fontSize = 12.sp)
                            HorizontalDivider(modifier = Modifier.weight(1f), color = Color.LightGray)
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        // Nút Google
                        Button(
                            onClick = { /* TODO: Google Login */ },
                            modifier = Modifier.fillMaxWidth().height(50.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, Color.LightGray)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.google),
                                contentDescription = "Google",
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text("Tiếp tục với Google", color = Color.Black, fontSize = 16.sp)
                        }

                        // Khoảng trống cuối cùng để không bị sát đáy màn hình
                        Spacer(modifier = Modifier.height(50.dp))
                    }
                }
            }
        }

        // --- LOGIC CHUYỂN TRANG KHI THÀNH CÔNG ---
        if (isLoginSuccess) {
            StatusScreen(
                icon = Icons.Default.Person,
                title = "Đăng nhập",
                statusText = "thành công !",
                subtitle = "Đang chuyển hướng...",
                onTimeout = {
                    // 1. Lưu trạng thái đăng nhập
                    val prefs = context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
                    prefs.edit().putBoolean("IS_LOGGED_IN", true).apply()

                    // 2. Chuyển sang Main, xóa sạch lịch sử Splash
                    try {
                        navController.navigate(AppRoutes.MAIN_APP_GRAPH) {
                            popUpTo(AppRoutes.SPLASH) { inclusive = true }
                        }
                    } catch (e: Exception) {
                        navController.navigate(AppRoutes.HOME)
                    }
                }
            )
        }
    }
}