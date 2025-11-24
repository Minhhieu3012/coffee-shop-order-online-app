package vn.edu.ut.hieupm9898.customermobile.features.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import vn.edu.ut.hieupm9898.customermobile.ui.components.BrosButton
import vn.edu.ut.hieupm9898.customermobile.ui.components.BrosTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    onBackClick: () -> Unit = {},
    viewModel: EditProfileViewModel = hiltViewModel()
) {
    val currentUser by viewModel.currentUser.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val isSuccess by viewModel.isSuccess.collectAsState()

    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var dateOfBirth by remember { mutableStateOf("") }

    // Load user data khi có
    LaunchedEffect(currentUser) {
        currentUser?.let { user ->
            name = user.displayName
            phone = user.phoneNumber
            email = user.email
            dateOfBirth = user.dateOfBirth
        }
    }

    // Hiển thị thông báo lỗi
    LaunchedEffect(errorMessage) {
        errorMessage?.let {
            // Có thể show Snackbar ở đây
        }
    }

    // Khi update thành công
    LaunchedEffect(isSuccess) {
        if (isSuccess) {
            viewModel.clearSuccess()
            onBackClick() // Quay lại màn trước
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Chỉnh sửa hồ sơ",
                        fontWeight = FontWeight.Bold,
                        fontSize = 30.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            modifier = Modifier.size(60.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        bottomBar = {
            Box(modifier = Modifier.padding(24.dp)) {
                BrosButton(
                    text = if (isLoading) "Đang lưu..." else "Lưu thay đổi",
                    onClick = {
                        viewModel.updateProfile(
                            displayName = name,
                            phoneNumber = phone,
                            dateOfBirth = dateOfBirth,
                            avatarUrl = currentUser?.avatarUrl ?: ""
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    ) { paddingValues ->
        if (isLoading && currentUser == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 24.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(20.dp))

                // Avatar
                Box(contentAlignment = Alignment.BottomEnd) {
                    if (currentUser?.avatarUrl.isNullOrEmpty()) {
                        Box(
                            modifier = Modifier
                                .size(120.dp)
                                .clip(CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Avatar",
                                modifier = Modifier.size(60.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    } else {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(currentUser?.avatarUrl)
                                .crossfade(true)
                                .build(),
                            contentDescription = "Avatar",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(120.dp)
                                .clip(CircleShape)
                        )
                    }

                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(36.dp)
                    ) {
                        IconButton(onClick = { /* TODO: Pick Image */ }) {
                            Icon(
                                Icons.Default.CameraAlt,
                                contentDescription = "Edit",
                                tint = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Error Message
                if (errorMessage != null) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer
                        )
                    ) {
                        Text(
                            text = errorMessage ?: "",
                            modifier = Modifier.padding(16.dp),
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }

                // Form Fields
                BrosTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = "Họ và tên"
                )
                Spacer(modifier = Modifier.height(16.dp))

                BrosTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = "Số điện thoại",
                    keyboardType = KeyboardType.Phone
                )
                Spacer(modifier = Modifier.height(16.dp))

                BrosTextField(
                    value = email,
                    onValueChange = { }, // Email không cho sửa
                    label = "Email",
                    keyboardType = KeyboardType.Email,
                    enabled = false
                )
                Spacer(modifier = Modifier.height(16.dp))

                BrosTextField(
                    value = dateOfBirth,
                    onValueChange = { dateOfBirth = it },
                    label = "Ngày sinh (DD/MM/YYYY)"
                )

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}