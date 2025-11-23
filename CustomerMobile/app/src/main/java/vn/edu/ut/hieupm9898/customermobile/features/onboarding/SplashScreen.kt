package vn.edu.ut.hieupm9898.customermobile.features.onboarding

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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import vn.edu.ut.hieupm9898.customermobile.R
import vn.edu.ut.hieupm9898.customermobile.features.auth.AuthViewModel
import vn.edu.ut.hieupm9898.customermobile.ui.theme.*

@Composable
fun SplashScreen(
    navController: NavController,
    onGetStartedClick: () -> Unit,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    var showContent by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(2000) // Hiển thị splash 2 giây

        // 🔥 KIỂM TRA XEM USER ĐÃ ĐĂNG NHẬP VÀ CHỌN "GHI NHỚ" CHƯA
        if (authViewModel.isUserLoggedIn()) {
            // Load thông tin user
            authViewModel.loadCurrentUser()
            // Chuyển thẳng đến Home
            navController.navigate("main_app_graph") {
                popUpTo("splash") { inclusive = true }
            }
        } else {
            // Chưa đăng nhập hoặc không chọn ghi nhớ -> Hiển thị nút Get Started
            showContent = true
        }
    }

    Surface(color = BrosBackground, modifier = Modifier.fillMaxSize()) {
        if (showContent) {
            // Hiển thị UI với nút Get Started
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 100.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.logo),
                        contentDescription = "Bros coffee Logo",
                        modifier = Modifier.size(400.dp)
                    )
                    Spacer(modifier = Modifier.height(32.dp))
                    Text(
                        text = "Ngừng so sánh",
                        fontSize = 40.sp,
                        fontWeight = FontWeight.Bold,
                        color = BrosTitle
                    )
                    Text(
                        text = "Hãy tận hưởng",
                        fontSize = 40.sp,
                        fontWeight = FontWeight.Bold,
                        color = BrosTitle
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Đặt ngay cà phê yêu thích của bạn mọi lúc, mọi nơi. Chỉ cần vài thao tác là đồ uống đã sẵn sàng.",
                        fontSize = 18.sp,
                        color = BrosSubTitle,
                        lineHeight = 20.sp,
                        textAlign = TextAlign.Center
                    )
                }

                Button(
                    onClick = onGetStartedClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = BrosButton),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "Bắt đầu",
                        fontSize = 30.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }
            }
        } else {
            // Hiển thị loading khi đang kiểm tra auth
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.logo),
                        contentDescription = "Bros Coffee Logo",
                        modifier = Modifier.size(200.dp)
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    CircularProgressIndicator(color = BrosBrown)
                }
            }
        }
    }
}