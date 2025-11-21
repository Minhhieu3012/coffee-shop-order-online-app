package vn.edu.ut.hieupm9898.customermobile.features.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.delay

import vn.edu.ut.hieupm9898.customermobile.R
import vn.edu.ut.hieupm9898.customermobile.navigation.AppRoutes
import vn.edu.ut.hieupm9898.customermobile.ui.components.BrosButton
import vn.edu.ut.hieupm9898.customermobile.ui.components.BrosTextField
import vn.edu.ut.hieupm9898.customermobile.ui.theme.*

@Composable
fun RegisterScreen(navController: NavController) {
    var userName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var referralCode by remember { mutableStateOf("") }
    var isTermAccepted by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) }

    LaunchedEffect(showSuccessDialog) {
        if (showSuccessDialog) {
            delay(2000)
            showSuccessDialog = false
            navController.navigate(AppRoutes.PROFILE) {
                popUpTo(AppRoutes.REGISTER) { inclusive = true }
            }
        }
    }

    SignUpSuccessDialog(showDialog = showSuccessDialog, onDismiss = {})

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = BrosBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // --- TOP BAR với nút Back và Logo ---
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, start = 8.dp, end = 8.dp)
            ) {
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.align(Alignment.CenterStart)
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = BrosBrown,
                        modifier = Modifier.size(60.dp)
                    )
                }

                // Logo Bros Café ở giữa
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Bros Café Logo",
                    modifier = Modifier
                        .align(Alignment.Center)
                        .height(60.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // --- TIÊU ĐỀ ---
            Text(
                text = "Đăng ký",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = BrosTitle
            )

            Spacer(modifier = Modifier.height(24.dp))

            // --- FORM NHẬP LIỆU ---
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.Start
            ) {
                // 1. User Name
                Text(
                    text = "Tên người dùng",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = BrosTitle,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                BrosTextField(
                    value = userName,
                    onValueChange = { userName = it },
                    label = "Nhập tên người dùng",
                    icon = Icons.Default.Person
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 2. Email
                Text(
                    text = "Email",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = BrosTitle,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                BrosTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = "Nhập địa chỉ email",
                    icon = Icons.Default.Email,
                    keyboardType = KeyboardType.Email
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 3. Phone Number
                Text(
                    text = "Số điện thoại",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = BrosTitle,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                BrosTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = "Nhập số điện thoại",
                    icon = Icons.Default.Phone,
                    keyboardType = KeyboardType.Phone
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 4. Password
                Text(
                    text = "Mật khẩu",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = BrosTitle,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                BrosTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = "Nhập mật khẩu",
                    icon = Icons.Default.Lock,
                    isPassword = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 5. Referral Code (optional)
                Text(
                    text = "Mã giới thiệu (tùy chọn)",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = BrosTitle,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                BrosTextField(
                    value = referralCode,
                    onValueChange = { referralCode = it },
                    label = "Nhập mã giới thiệu",
                    icon = Icons.Default.Lock
                )

                Spacer(modifier = Modifier.height(12.dp))

                // --- Checkbox Terms & Condition ---
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = isTermAccepted,
                        onCheckedChange = { isTermAccepted = it },
                        colors = CheckboxDefaults.colors(
                            checkedColor = BrosBrown,
                            uncheckedColor = BrosSubTitle
                        )
                    )
                    Text(
                        text = buildAnnotatedString {
                            append("Chấp nhận ")
                            withStyle(style = SpanStyle(color = BrosBrown, fontWeight = FontWeight.Medium)) {
                                append("Điều khoản & Điều kiện")
                            }
                        },
                        fontSize = 14.sp,
                        color = BrosSubTitle
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // --- Link đến Login ---
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Nếu bạn có tài khoản!",
                        fontSize = 14.sp,
                        color = BrosSubTitle
                    )
                    Text(
                        text = " Đăng nhập ngay",
                        fontSize = 14.sp,
                        color = BrosTitle,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable {
                            navController.popBackStack()
                        }
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // --- NÚT PROCEED ---
                BrosButton(
                    text = "Tiếp tục",
                    onClick = { showSuccessDialog = true },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun RegisterPreview() {
    RegisterScreen(navController = rememberNavController())
}