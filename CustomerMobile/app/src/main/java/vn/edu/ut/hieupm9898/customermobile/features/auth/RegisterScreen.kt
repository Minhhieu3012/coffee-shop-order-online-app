package vn.edu.ut.hieupm9898.customermobile.features.auth

import android.util.Patterns // [QUAN TRỌNG] Thư viện kiểm tra Email
import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.delay

import vn.edu.ut.hieupm9898.customermobile.R
import vn.edu.ut.hieupm9898.customermobile.navigation.AppRoutes
import vn.edu.ut.hieupm9898.customermobile.ui.components.BrosButton
import vn.edu.ut.hieupm9898.customermobile.ui.components.BrosTextField
import vn.edu.ut.hieupm9898.customermobile.ui.theme.*

@Composable
fun RegisterScreen(navController: NavController, viewModel: AuthViewModel = hiltViewModel() ) {
    var userName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var referralCode by remember { mutableStateOf("") }

    // Checkbox điều khoản
    var isTermAccepted by remember { mutableStateOf(false) }

    var showSuccessDialog by remember { mutableStateOf(false) }

    val context = LocalContext.current

    // --- LOGIC ĐIỀU HƯỚNG: SAU KHI ĐĂNG KÝ THÀNH CÔNG ---
    LaunchedEffect(showSuccessDialog) {
        if (showSuccessDialog) {
            delay(2000) // Đợi 2 giây để người dùng đọc thông báo
            showSuccessDialog = false

            // 👇 ĐÃ SỬA: Chuyển hướng về màn hình LOGIN
            navController.navigate(AppRoutes.LOGIN) {
                // Xóa màn hình Register khỏi ngăn xếp (Back Stack)
                // Để khi người dùng bấm nút Back ở màn hình Login, nó không quay lại màn hình Đăng ký này nữa
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
            // --- TOP BAR ---
            Box(
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp, start = 8.dp, end = 8.dp)
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
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Logo",
                    modifier = Modifier.align(Alignment.Center).height(60.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("Đăng ký", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = BrosTitle)

            Spacer(modifier = Modifier.height(24.dp))

            // --- FORM NHẬP LIỆU ---
            Column(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.Start
            ) {
                // 1. Tên người dùng
                Text("Tên người dùng", fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = BrosTitle, modifier = Modifier.padding(bottom = 8.dp))
                BrosTextField(
                    value = userName,
                    onValueChange = { userName = it },
                    label = "Nhập tên người dùng",
                    icon = Icons.Default.Person
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 2. Email
                Text("Email", fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = BrosTitle, modifier = Modifier.padding(bottom = 8.dp))
                BrosTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = "Nhập địa chỉ email",
                    icon = Icons.Default.Email,
                    keyboardType = KeyboardType.Email
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 3. Số điện thoại (Chỉ cho nhập số)
                Text("Số điện thoại", fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = BrosTitle, modifier = Modifier.padding(bottom = 8.dp))
                BrosTextField(
                    value = phone,
                    onValueChange = { input ->
                        // Chỉ cho phép nhập ký tự số
                        if (input.all { char -> char.isDigit() }) {
                            phone = input
                        }
                    },
                    label = "Nhập số điện thoại",
                    icon = Icons.Default.Phone,
                    keyboardType = KeyboardType.Phone
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 4. Mật khẩu
                Text("Mật khẩu", fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = BrosTitle, modifier = Modifier.padding(bottom = 8.dp))
                BrosTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = "Nhập mật khẩu",
                    icon = Icons.Default.Lock,
                    isPassword = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 5. Mã giới thiệu
                Text("Mã giới thiệu (tùy chọn)", fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = BrosTitle, modifier = Modifier.padding(bottom = 8.dp))
                BrosTextField(
                    value = referralCode,
                    onValueChange = { referralCode = it },
                    label = "Nhập mã giới thiệu",
                    icon = Icons.Default.Lock
                )

                Spacer(modifier = Modifier.height(12.dp))

                // --- CHECKBOX ---
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = isTermAccepted,
                        onCheckedChange = { isTermAccepted = it },
                        colors = CheckboxDefaults.colors(checkedColor = BrosBrown, uncheckedColor = BrosSubTitle)
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

                // Link Login
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                    Text("Nếu bạn có tài khoản! ", fontSize = 14.sp, color = BrosSubTitle)
                    Text("Đăng nhập ngay", fontSize = 14.sp, color = BrosTitle, fontWeight = FontWeight.Bold, modifier = Modifier.clickable { navController.popBackStack() })
                }

                Spacer(modifier = Modifier.height(24.dp))

                // --- NÚT TIẾP TỤC (VALIDATION NÂNG CAO) ---
                // Tìm đến phần nút "Tiếp tục", thay thế logic cũ:

                BrosButton(
                    text = "Tiếp tục",
                    enabled = !showSuccessDialog,
                    onClick = {
                        val cleanName = userName.trim()
                        val cleanEmail = email.trim()
                        val cleanPhone = phone.trim()
                        val cleanPass = password.trim()

                        val vietnamPhoneRegex = Regex("^0\\d{9}$")

                        // Validate các trường
                        if (cleanName.isEmpty()) {
                            Toast.makeText(context, "Vui lòng nhập tên người dùng!", Toast.LENGTH_SHORT).show()
                        }
                        else if (cleanEmail.isEmpty()) {
                            Toast.makeText(context, "Vui lòng nhập Email!", Toast.LENGTH_SHORT).show()
                        }
                        else if (!Patterns.EMAIL_ADDRESS.matcher(cleanEmail).matches()) {
                            Toast.makeText(context, "Email không hợp lệ!", Toast.LENGTH_SHORT).show()
                        }
                        else if (cleanPhone.isEmpty()) {
                            Toast.makeText(context, "Vui lòng nhập số điện thoại!", Toast.LENGTH_SHORT).show()
                        }
                        else if (!cleanPhone.matches(vietnamPhoneRegex)) {
                            Toast.makeText(context, "Số điện thoại không hợp lệ", Toast.LENGTH_LONG).show()
                        }
                        else if (cleanPass.isEmpty()) {
                            Toast.makeText(context, "Vui lòng nhập mật khẩu!", Toast.LENGTH_SHORT).show()
                        }
                        else if (!isTermAccepted) {
                            Toast.makeText(context, "Bạn cần chấp nhận Điều khoản!", Toast.LENGTH_SHORT).show()
                        }
                        else {
                            // ✅ GỌI VIEWMODEL ĐỂ ĐĂNG KÝ (ĐÃ SỬA)
                            viewModel.onRegisterClicked(
                                userName = cleanName,           // 👈 THÊM TÊN
                                email = cleanEmail,
                                phoneNumber = cleanPhone,       // 👈 THÊM SĐT
                                password = cleanPass,
                                referralCode = referralCode.trim()
                            )
                            showSuccessDialog = true
                        }
                    },
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