// features/auth/LoginScreen.kt

package vn.edu.ut.hieupm9898.customermobile.features.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import vn.edu.ut.hieupm9898.customermobile.R
import vn.edu.ut.hieupm9898.customermobile.ui.theme.* import vn.edu.ut.hieupm9898.customermobile.navigation.AppRoutes

// Giả định các components của bạn đã được định nghĩa trong ui/components
// @Composable fun BrosTextField(...)
// @Composable fun BrosButton(...) 

@Composable
fun LoginScreen(navController: NavController) {
    // State quản lý form
    var email by remember { mutableStateOf("hieupm9898@ut.edu.vn") }
    var password by remember { mutableStateOf("••••••••") }
    var rememberMe by remember { mutableStateOf(false) }
    var isLoginSuccess by remember { mutableStateOf(false) } // State cho Dialog

    Surface(color = BrosBackground, modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Phần Header và Logo (Chiếm phần trên)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.3f), // Chiếm khoảng 30% màn hình
                contentAlignment = Alignment.Center
            ) {
                // Giả định Logo Cafe được đặt ở đây
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Bros Cafe Logo",
                    modifier = Modifier.size(120.dp)
                )
            }

            // Phần Form Đăng nhập (Chiếm phần dưới)
            Card(
                shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.7f) // Chiếm 70% màn hình còn lại
                    .padding(top = 0.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 30.dp, vertical = 40.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Tiêu đề
                    Text(text = "Login", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = BrosTitle)

                    // Link Đăng ký
                    Row(modifier = Modifier.padding(bottom = 20.dp)) {
                        Text(text = "Don't have an account? ", fontSize = 14.sp, color = BrosSubTitle)
                        Text(
                            text = "Sign Up",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = BrosBrown,
                            modifier = Modifier.clickable { navController.navigate(AppRoutes.REGISTER) }
                        )
                    }

                    // --- INPUTS ---
                    // Cần thay thế bằng component BrosTextField của bạn
                    OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") }, modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("Password") }, modifier = Modifier.fillMaxWidth())

                    // --- CHECKBOX và FORGOT PASSWORD ---
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp, bottom = 24.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(checked = rememberMe, onCheckedChange = { rememberMe = it }, colors = CheckboxDefaults.colors(checkedColor = BrosBrown))
                            Text("Remember me", fontSize = 12.sp, color = BrosSubTitle)
                        }
                        Text("Forgot Password?", fontSize = 12.sp, color = BrosBrown, modifier = Modifier.clickable { /* TBD */ })
                    }

                    // --- BUTTONS ---

                    // 1. Log In (Nút chính)
                    Button(
                        onClick = { isLoginSuccess = true }, // Giả lập thành công
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = BrosBrown),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Log In", fontSize = 18.sp, color = Color.White)
                    }

                    Text("Or", modifier = Modifier.padding(vertical = 16.dp), color = BrosSubTitle)

                    // 2. Sign Up (Nút phụ)
                    Button(
                        onClick = { navController.navigate(AppRoutes.REGISTER) },
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF986B3E)), // Màu nâu nhạt hơn
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Sign Up", fontSize = 18.sp, color = Color.White)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // 3. Continue with Google
                    Button(
                        onClick = { /* TBD */ },
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                        border = BorderStroke(1.dp, BrosSubTitle),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.google_icon), // Cần icon Google
                            contentDescription = "Google Sign-in",
                            modifier = Modifier.size(20.dp).padding(end = 8.dp)
                        )
                        Text("Continue with Google", fontSize = 16.sp, color = Color.Black)
                    }
                }
            }
        }
    }

    // Hiển thị Dialog khi đăng nhập thành công
    if (isLoginSuccess) {
        LoginSuccessDialog(
            onDismissRequest = { isLoginSuccess = false },
            onComplete = {
                isLoginSuccess = false
                navController.navigate(AppRoutes.HOME) {
                    popUpTo(AppRoutes.AUTH_FLOW) { inclusive = true } // Xóa stack Login
                }
            }
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun LoginScreenPreview() {
    LoginScreen(navController = rememberNavController())
}