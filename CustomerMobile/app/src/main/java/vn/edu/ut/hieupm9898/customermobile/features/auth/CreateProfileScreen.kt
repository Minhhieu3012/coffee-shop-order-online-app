package vn.edu.ut.hieupm9898.customermobile.features.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

// Import tài nguyên dự án
import vn.edu.ut.hieupm9898.customermobile.R
import vn.edu.ut.hieupm9898.customermobile.navigation.AppRoutes
// Import Components của bạn
import vn.edu.ut.hieupm9898.customermobile.ui.components.BrosButton
import vn.edu.ut.hieupm9898.customermobile.ui.components.BrosTextField
import vn.edu.ut.hieupm9898.customermobile.ui.components.StatusScreen
import vn.edu.ut.hieupm9898.customermobile.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateProfileScreen(navController: NavController) {
    // 1. State quản lý dữ liệu nhập vào
    var fullName by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var dateOfBirth by remember { mutableStateOf("") }

    // 2. State hiển thị StatusScreen (Màn hình thành công)
    var isSuccess by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {

        // --- LỚP 1: GIAO DIỆN CHÍNH ---
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text("Create Your Profile", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = BrosTitle)
                    },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = BrosBrown)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = BrosBackground)
                )
            },
            containerColor = BrosBackground // Nền màu kem cho phần trên
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = paddingValues.calculateTopPadding()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // --- PHẦN AVATAR ---
                Box(
                    contentAlignment = Alignment.BottomEnd,
                    modifier = Modifier.padding(vertical = 24.dp)
                ) {
                    // Ảnh đại diện tròn
                    Image(
                        painter = painterResource(id = R.drawable.logo), // Thay bằng ảnh user mặc định nếu có
                        contentDescription = "Profile Picture",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                            .border(2.dp, BrosBrown, CircleShape)
                    )

                    // Nút máy ảnh nhỏ để đổi avatar
                    Box(
                        modifier = Modifier
                            .offset(x = 4.dp, y = 4.dp)
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(BrosBrown)
                            .border(2.dp, BrosBackground, CircleShape)
                            .clickable { /* TODO: Mở thư viện ảnh */ },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.CameraAlt, contentDescription = "Edit", tint = Color.White, modifier = Modifier.size(18.dp))
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // --- PHẦN FORM NHẬP LIỆU (Card trắng bo góc trên) ---
                Card(
                    shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier.fillMaxSize() // Chiếm hết phần còn lại
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(30.dp)
                            .verticalScroll(rememberScrollState()), // Cho phép cuộn
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(modifier = Modifier.height(10.dp))

                        // 1. Nhập Tên (Dùng BrosTextField)
                        BrosTextField(
                            value = fullName,
                            onValueChange = { fullName = it },
                            label = "User Name",
                            icon = Icons.Default.Person // Truyền icon vào
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // 2. Nhập SĐT
                        BrosTextField(
                            value = phoneNumber,
                            onValueChange = { phoneNumber = it },
                            label = "Phone Number",
                            icon = Icons.Default.Phone,
                            keyboardType = KeyboardType.Phone
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // 3. Nhập Ngày sinh
                        BrosTextField(
                            value = dateOfBirth,
                            onValueChange = { dateOfBirth = it },
                            label = "Date of Birth",
                            icon = Icons.Default.CalendarToday
                        )

                        Spacer(modifier = Modifier.weight(1f)) // Đẩy nút xuống đáy nếu màn hình dài
                        Spacer(modifier = Modifier.height(30.dp))

                        // 4. Nút Hoàn tất (Dùng BrosButton)
                        BrosButton(
                            text = "Finish",
                            onClick = { isSuccess = true }, // Kích hoạt màn hình thành công
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(20.dp))
                    }
                }
            }
        }

        // --- LỚP 2: STATUS SCREEN (MÀU XANH LÁ - ĐÈ LÊN TRÊN) ---
        if (isSuccess) {
            val greenColor = Color(0xFF4CAF50)
            val lightGreenColor = Color(0xFF81C784)

            StatusScreen(
                icon = Icons.Default.Person,
                title = "Sign up", // Tiêu đề
                statusText = "Success !", // Trạng thái
                subtitle = "Your account has been created successfully.\nRedirecting to Home...",

                // Tùy chỉnh màu xanh cho trạng thái thành công
                iconBackgroundColor = greenColor,
                titleColor = greenColor,
                decorativeDotsColor = lightGreenColor,

                onTimeout = {
                    isSuccess = false
                    // Điều hướng sang Home, xóa luồng Auth cũ
                    navController.navigate(AppRoutes.HOME) {
                        popUpTo(AppRoutes.AUTH_GRAPH) { inclusive = true }
                    }
                }
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun CreateProfilePreview() {
    CreateProfileScreen(navController = rememberNavController())
}