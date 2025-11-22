package vn.edu.ut.hieupm9898.customermobile.features.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.delay

import vn.edu.ut.hieupm9898.customermobile.navigation.AppRoutes
import vn.edu.ut.hieupm9898.customermobile.ui.components.BrosButton

import vn.edu.ut.hieupm9898.customermobile.ui.theme.BrosBackground
import vn.edu.ut.hieupm9898.customermobile.ui.theme.BrosBrown
import vn.edu.ut.hieupm9898.customermobile.ui.theme.BrosTitle

enum class OtpState { IDLE, SUCCESS, ERROR }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OTPVerificationScreen(navController: NavController) {
    val otpLength = 6
    var otpCode by remember { mutableStateOf("") }
    var timeLeft by remember { mutableIntStateOf(59) }
    var isTimerRunning by remember { mutableStateOf(true) }
    var otpState by remember { mutableStateOf(OtpState.IDLE) }

    LaunchedEffect(key1 = isTimerRunning) {
        while (isTimerRunning && timeLeft > 0) {
            delay(1000L)
            timeLeft--
        }
        if (timeLeft == 0) isTimerRunning = false
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            Text("Xác minh OTP", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = BrosTitle)
            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Vui lòng nhập mã xác minh chữ số $otpLength được gửi đến số +84(123)-456-789",
                fontSize = 14.sp,
                color = Color.Gray,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(50.dp))

            BasicTextField(
                value = otpCode,
                onValueChange = {
                    if (it.length <= otpLength && it.all { char -> char.isDigit() }) {
                        otpCode = it
                        if (it.length == otpLength) {
                            otpState = if (it == "111111") OtpState.SUCCESS else OtpState.ERROR
                        } else {
                            otpState = OtpState.IDLE
                        }
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                decorationBox = {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        repeat(otpLength) { index ->
                            val char = if (index < otpCode.length) otpCode[index].toString() else ""
                            val isFocused = index == otpCode.length

                            val borderColor = when {
                                otpState == OtpState.ERROR -> Color.Red
                                otpState == OtpState.SUCCESS -> Color(0xFF4CAF50)
                                isFocused -> BrosBrown
                                else -> Color.Gray
                            }

                            Box(
                                modifier = Modifier
                                    .width(45.dp)
                                    .height(50.dp)
                                    .border(
                                        width = if (isFocused) 2.dp else 1.dp,
                                        color = borderColor,
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .background(Color.White, RoundedCornerShape(8.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = char,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = BrosTitle
                                )
                            }
                        }
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (otpState != OtpState.IDLE) {
                Text(
                    text = if (otpState == OtpState.ERROR) "Mã OTP không chính xác, vui lòng kiểm tra lại." else "Mã OTP chính xác, đang chuyển hướng...",
                    color = if (otpState == OtpState.ERROR) Color.Red else Color(0xFF4CAF50),
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(14.dp))


            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Text("Chưa nhận được mã?", color = Color.Gray, fontSize = 14.sp)
                if (timeLeft > 0) {
                    Text("Gửi lại trong 00:${if (timeLeft < 10) "0$timeLeft" else timeLeft}", color = BrosBrown, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                } else {
                    Text(
                        " Gửi lại ngay",
                        color = BrosBrown,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        modifier = Modifier.clickable { timeLeft = 59; isTimerRunning = true }
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // --- ĐÃ SỬA LẠI NÚT BẤM CHO KHỚP ---
            BrosButton(
                text = "Tiếp tục",
                onClick = {
                    if (otpState == OtpState.SUCCESS) {
                        navController.navigate(AppRoutes.RESET_PASSWORD)
                    }
                },
                // [SỬA LỖI] Đổi isEnabled -> enabled
                enabled = otpCode.length == otpLength,
                modifier = Modifier.fillMaxWidth().size(60.dp)
            )

            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun OTPVerificationPreview() {
    OTPVerificationScreen(navController = rememberNavController())
}