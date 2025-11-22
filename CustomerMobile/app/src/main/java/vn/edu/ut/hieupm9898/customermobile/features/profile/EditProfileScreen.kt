package vn.edu.ut.hieupm9898.customermobile.features.profile

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel // Cần import này
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import vn.edu.ut.hieupm9898.customermobile.ui.components.BrosButton
import vn.edu.ut.hieupm9898.customermobile.ui.components.BrosTextField
import vn.edu.ut.hieupm9898.customermobile.ui.theme.CustomerMobileTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    navController: NavController, // Thêm NavController để navigate
    onBackClick: () -> Unit = {},
    // onSaveClick không cần nữa vì logic đã nằm trong ViewModel
    viewModel: ProfileViewModel = viewModel() // [QUAN TRỌNG] Inject ViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    // 1. Dùng các biến state cục bộ để chỉnh sửa (editing states)
    // Khởi tạo các biến này từ dữ liệu của ViewModel, chỉ chạy 1 lần khi khởi tạo
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }

    // Dùng LaunchedEffect để lắng nghe UI State và cập nhật các biến editing state
    LaunchedEffect(uiState.user) {
        if (!uiState.isLoading && uiState.user.name.isNotEmpty()) {
            name = uiState.user.name
            phone = uiState.user.phone
            email = uiState.user.email
            address = "KTX Khu B" // Giữ nguyên địa chỉ giả định
        }
    }

    // 2. Xử lý phản hồi từ ViewModel (SAVE SUCCESS/ERROR)
    LaunchedEffect(uiState.saveSuccess) {
        when (uiState.saveSuccess) {
            true -> {
                Toast.makeText(context, "Cập nhật hồ sơ thành công!", Toast.LENGTH_SHORT).show()
                viewModel.resetSaveStatus() // Reset cờ
                navController.popBackStack() // Quay về màn hình Profile chính
            }
            false -> {
                Toast.makeText(context, "Lỗi lưu dữ liệu! Vui lòng thử lại.", Toast.LENGTH_SHORT).show()
                viewModel.resetSaveStatus()
            }
            else -> {}
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Chỉnh sửa hồ sơ", fontWeight = FontWeight.Bold, fontSize = 30.sp) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) { Icon(Icons.Default.ArrowBack, contentDescription = "Back", modifier = Modifier.size(60.dp)) }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        },
        bottomBar = {
            Box(modifier = Modifier.padding(24.dp)) {
                BrosButton(
                    text = if (uiState.isLoading) "Đang lưu..." else "Lưu thay đổi",
                    // Chặn bấm nút khi đang loading
                    enabled = !uiState.isLoading,
                    onClick = {
                        // [QUAN TRỌNG] GỌI HÀM LƯU CỦA VIEWMODEL
                        viewModel.saveProfile(name, phone, email)
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            // 1. Avatar with Edit Icon
            Box(contentAlignment = Alignment.BottomEnd) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current).data("https://img.freepik.com/free-photo/portrait-handsome-smiling-young-man-model-wearing-casual-summer-pink-clothes-fashion-stylish-man-posing_158538-5350.jpg").crossfade(true).build(),
                    contentDescription = "Avatar",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(120.dp).clip(CircleShape)
                )
                Surface(shape = CircleShape, color = MaterialTheme.colorScheme.primary, modifier = Modifier.size(36.dp)) {
                    IconButton(onClick = { /* Pick Image */ }) { Icon(Icons.Default.CameraAlt, contentDescription = "Edit", tint = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.size(18.dp)) }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // 2. Form Fields (VALUE VÀ ONVALUECHANGE ĐÃ ĐƯỢC KẾT NỐI VỚI LOCAL STATE)
            BrosTextField(value = name, onValueChange = { name = it }, label = "Full Name")
            Spacer(modifier = Modifier.height(16.dp))

            BrosTextField(value = phone, onValueChange = { phone = it }, label = "Phone Number", keyboardType = KeyboardType.Phone)
            Spacer(modifier = Modifier.height(16.dp))

            BrosTextField(value = email, onValueChange = { email = it }, label = "Email", keyboardType = KeyboardType.Email)
            Spacer(modifier = Modifier.height(16.dp))

            BrosTextField(value = address, onValueChange = { address = it }, label = "Address")
        }
    }
}

@Preview
@Composable
fun EditProfilePreview() {
    CustomerMobileTheme {
        // Vì đây là Preview, ta dùng rememberNavController() và ProfileViewModel mock
        EditProfileScreen(navController = rememberNavController())
    }
}