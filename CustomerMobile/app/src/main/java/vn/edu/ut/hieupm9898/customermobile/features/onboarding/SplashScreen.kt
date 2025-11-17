package vn.edu.ut.hieupm9898.customermobile.features.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import vn.edu.ut.hieupm9898.customermobile.R
import vn.edu.ut.hieupm9898.customermobile.navigation.AppRoutes
import vn.edu.ut.hieupm9898.customermobile.ui.theme.BrosBrown
import vn.edu.ut.hieupm9898.customermobile.ui.theme.BrosCream

@Composable
fun SplashScreen(
    navController: NavController,
    viewModel: OnboardingViewModel = viewModel()
) {
    val isCompleted by viewModel.onboardingCompleted.collectAsState()

    // Logic LaunchedEffect không có delay, sẽ chạy ngay lập tức
    LaunchedEffect(key1 = true) {
        // Logica này sẽ chạy NGAY LẬP TỨC khi màn hình được compose
        if (isCompleted) {
            navController.popBackStack()
            navController.navigate(AppRoutes.AUTH_FLOW)
        } else {
            navController.popBackStack()
            navController.navigate(AppRoutes.ONBOARDING_1)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = BrosCream
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.weight(1f))

            // Logo và Tên
            Column(horizontalAlignment = Alignment.CenterHorizontally) {

                // ⚠️ SỬA: Thay thế Image bị lỗi bằng Text Placeholder để xác nhận UI hoạt động
                // Thao tác này ngăn ứng dụng bị crash do thiếu R.drawable.logo
                Text(
                    text = "BROS LOGO",
                    style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.ExtraBold),
                    color = BrosBrown
                )

                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Logo",
                    modifier = Modifier.size(120.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))
Text(
text = "BROS CAFE",
style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
color = BrosBrown
)
Spacer(modifier = Modifier.height(8.dp))
Text(
text = "Skip the line\nKeep the chill",
style = MaterialTheme.typography.bodyLarge,
textAlign = TextAlign.Center,
color = Color.Gray
)
Spacer(modifier = Modifier.height(16.dp))
Text(
text = "Order your favorite coffee, anywhere, anytime. Our app delivers fresh coffee to your door.",
style = MaterialTheme.typography.bodySmall,
textAlign = TextAlign.Center,
color = Color.DarkGray,
modifier = Modifier.padding(horizontal = 24.dp)
)
}

Spacer(modifier = Modifier.weight(1f))

// Nút này bây giờ chỉ đóng vai trò là "Skip" và chuyển ngay
Button(
onClick = {
    navController.popBackStack()
    navController.navigate(AppRoutes.ONBOARDING_1)
},
modifier = Modifier
.fillMaxWidth()
.height(56.dp)
.clip(RoundedCornerShape(12.dp)),
colors = ButtonDefaults.buttonColors(containerColor = BrosBrown)
) {
    Text(
        text = "Get Started",
        style = MaterialTheme.typography.titleMedium,
        color = Color.White
    )
}
}
}
}