package vn.edu.ut.hieupm9898.customermobile.features.auth

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

import vn.edu.ut.hieupm9898.customermobile.R
import vn.edu.ut.hieupm9898.customermobile.navigation.AppRoutes
import vn.edu.ut.hieupm9898.customermobile.ui.components.BrosButton
import vn.edu.ut.hieupm9898.customermobile.ui.components.BrosTextField
import vn.edu.ut.hieupm9898.customermobile.ui.components.StatusScreen
import vn.edu.ut.hieupm9898.customermobile.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateProfileScreen(navController: NavController, viewModel: AuthViewModel = hiltViewModel()) {
    var fullName by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var dateOfBirth by remember { mutableStateOf("") }
    var isSuccess by remember { mutableStateOf(false) }

    val context = LocalContext.current

    // Lắng nghe sự kiện từ ViewModel
    LaunchedEffect(Unit) {
        viewModel.navEvent.collect { event ->
            if (event is AuthNavEvent.NavigateToHome) {
                isSuccess = true
                kotlinx.coroutines.delay(2000)
                isSuccess = false
                navController.navigate(AppRoutes.HOME) {
                    popUpTo(AppRoutes.AUTH_GRAPH) { inclusive = true }
                }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = BrosBackground)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // --- TOP BAR ---
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
                text = "Tạo hồ sơ của bạn",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = BrosTitle
            )

            Text(
                text = "Tạo hồ sơ ban đầu của bạn để bắt đầu",
                fontSize = 16.sp,
                color = BrosSubTitle,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 4.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            // --- AVATAR ---
            Box(
                contentAlignment = Alignment.BottomEnd,
                modifier = Modifier.padding(bottom = 32.dp)
            ) {
                // Vòng tròn avatar với icon Person
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .background(color = BrosSubTitle), // Màu be nhạt
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = "Avatar",
                        tint = Color.Black,
                        modifier = Modifier.size(50.dp)
                    )
                }

                // Nút Edit nhỏ
                Box(
                    modifier = Modifier
                        .offset(x = 0.dp, y = 0.dp)
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                        .clickable { /* TODO: Mở thư viện ảnh */ },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Edit,
                        contentDescription = "Edit",
                        tint = BrosButton,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

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
                    value = fullName,
                    onValueChange = { fullName = it },
                    label = "Nhập tên",
                    icon = Icons.Default.Person
                )

                Spacer(modifier = Modifier.height(20.dp))

                // 2. Phone Number
                Text(
                    text = "Số điện thoại",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = BrosTitle,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                BrosTextField(
                    value = phoneNumber,
                    onValueChange = { phoneNumber = it },
                    label = "Nhập số điện thoại",
                    icon = Icons.Default.Phone,
                    keyboardType = KeyboardType.Phone
                )

                Spacer(modifier = Modifier.height(20.dp))

                // 3. Date of Birth
                Text(
                    text = "Ngày sinh",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = BrosTitle,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                BrosTextField(
                    value = dateOfBirth,
                    onValueChange = { dateOfBirth = it },
                    label = "DD/MM/YYYY",
                    icon = Icons.Default.CalendarToday
                )
            }

            Spacer(modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.height(40.dp))

            // --- NÚT SIGN UP ---
            // Thay đổi nút "Đăng ký":
            BrosButton(
                text = "Đăng ký",
                onClick = {
                    if (fullName.isBlank() || phoneNumber.isBlank() || dateOfBirth.isBlank()) {
                        Toast.makeText(
                            context,
                            "Vui lòng điền đầy đủ thông tin!",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        // ✅ GỌI VIEWMODEL
                        viewModel.onCompleteProfile(
                            displayName = fullName,
                            phoneNumber = phoneNumber,
                            dateOfBirth = dateOfBirth,
                            avatarUrl = ""
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            )

            Spacer(modifier = Modifier.height(80.dp))
        }

        // --- STATUS SCREEN (THÀNH CÔNG) ---
        // Status Screen
        if (isSuccess) {
            val greenColor = Color(0xFF4CAF50)
            val lightGreenColor = Color(0xFF81C784)

            StatusScreen(
                icon = Icons.Default.Person,
                title = "Sign up",
                statusText = "Success !",
                subtitle = "Your account has been created successfully.\nRedirecting to Home...",
                iconBackgroundColor = greenColor,
                titleColor = greenColor,
                decorativeDotsColor = lightGreenColor,
                onTimeout = {}
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun CreateProfilePreview() {
    CreateProfileScreen(navController = rememberNavController())
}