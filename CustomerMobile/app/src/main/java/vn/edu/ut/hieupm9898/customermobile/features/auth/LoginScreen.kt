package vn.edu.ut.hieupm9898.customermobile.features.auth

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

import vn.edu.ut.hieupm9898.customermobile.R
import vn.edu.ut.hieupm9898.customermobile.navigation.AppRoutes
import vn.edu.ut.hieupm9898.customermobile.ui.components.BrosButton
import vn.edu.ut.hieupm9898.customermobile.ui.components.BrosTextField
import vn.edu.ut.hieupm9898.customermobile.ui.components.StatusScreen
import vn.edu.ut.hieupm9898.customermobile.ui.theme.*

@Composable
fun LoginScreen(navController: NavController) {
    var email by remember { mutableStateOf("hieupm9898@ut.edu.vn") }
    var password by remember { mutableStateOf("••••••••") }
    var rememberMe by remember { mutableStateOf(false) }
    var isLoginSuccess by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        // --- LỚP 1: FORM LOGIN ---
        Surface(color = BrosBackground, modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Logo
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.logo),
                        contentDescription = "Logo",
                        modifier = Modifier.size(120.dp)
                    )
                }

                // Form Card
                Card(
                    shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier.fillMaxSize()
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 30.dp, vertical = 24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "Login",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = BrosTitle
                        )

                        // Link Sign Up
                        Row(modifier = Modifier.padding(vertical = 8.dp)) {
                            Text(
                                "Don't have an account? ",
                                color = BrosSubTitle,
                                fontSize = 14.sp
                            )
                            Text(
                                "Sign up",
                                color = BrosBrown,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                modifier = Modifier.clickable {
                                    navController.navigate(AppRoutes.REGISTER)
                                }
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Email TextField
                        BrosTextField(
                            value = email,
                            onValueChange = { email = it },
                            label = "Email",
                            keyboardType = KeyboardType.Email
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Password TextField
                        BrosTextField(
                            value = password,
                            onValueChange = { password = it },
                            label = "Password",
                            isPassword = true
                        )

                        // Remember Me & Forgot Password
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp, bottom = 24.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Checkbox(
                                    checked = rememberMe,
                                    onCheckedChange = { rememberMe = it },
                                    colors = CheckboxDefaults.colors(checkedColor = BrosBrown)
                                )
                                Text("Remember me", fontSize = 12.sp, color = BrosSubTitle)
                            }
                            Text(
                                "Forgot Password?",
                                fontSize = 12.sp,
                                color = BrosBrown,
                                modifier = Modifier.clickable {
                                    navController.navigate(AppRoutes.FORGOT_PASSWORD)
                                }
                            )
                        }

                        // Nút Log In
                        BrosButton(
                            text = "Log In",
                            onClick = { isLoginSuccess = true },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Divider
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            HorizontalDivider(
                                modifier = Modifier.weight(1f),
                                color = Color.LightGray.copy(alpha = 0.7f)
                            )
                            Text(
                                "Or",
                                modifier = Modifier.padding(horizontal = 16.dp),
                                color = BrosSubTitle
                            )
                            HorizontalDivider(
                                modifier = Modifier.weight(1f),
                                color = Color.LightGray.copy(alpha = 0.7f)
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Sign Up Button
                        BrosButton(
                            text = "Sign up",
                            onClick = { navController.navigate(AppRoutes.REGISTER) },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Google Button
                        Button(
                            onClick = { /* TBD */ },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, BrosSubTitle)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.google),
                                contentDescription = "Google Sign-in",
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                "Continue with Google",
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.onSurface,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        Spacer(modifier = Modifier.height(40.dp))
                    }
                }
            }
        }

        // --- LỚP 2: STATUS SCREEN ---
        if (isLoginSuccess) {
            StatusScreen(
                icon = Icons.Default.Person,
                title = "Đăng nhập",
                statusText = "thành công !",
                subtitle = "Vui lòng chờ...\nBạn sẽ được chuyển hướng đến trang chủ.",
                onTimeout = {
                    isLoginSuccess = false
                    navController.navigate(AppRoutes.HOME) {
                        popUpTo(AppRoutes.LOGIN) { inclusive = true }
                    }
                }
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun LoginScreenPreview() {
    LoginScreen(navController = rememberNavController())
}