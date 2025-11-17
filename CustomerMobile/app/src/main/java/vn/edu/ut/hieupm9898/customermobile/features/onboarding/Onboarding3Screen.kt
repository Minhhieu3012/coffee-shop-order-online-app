package vn.edu.ut.hieupm9898.customermobile.features.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
import vn.edu.ut.hieupm9898.customermobile.R
import vn.edu.ut.hieupm9898.customermobile.navigation.AppRoutes
import vn.edu.ut.hieupm9898.customermobile.ui.theme.BrosCream
import vn.edu.ut.hieupm9898.customermobile.ui.theme.BrosBrown

@Composable
fun Onboarding3Screen(
    navController: NavController,
    viewModel: OnboardingViewModel = viewModel() // Cần ViewModel để gọi setOnboardingCompleted()
) {
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
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Spacer(modifier = Modifier.height(64.dp))

                Image(
                    painter = painterResource(id = R.drawable.image_19 ), // THAY ĐỔI
                    contentDescription = "Grab and Go",
                    modifier = Modifier
                        .fillMaxWidth(0.6f)
                        .height(160.dp)
                        .clip(RoundedCornerShape(16.dp)),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(32.dp))
                Text(
                    text = "Grab and Go", // THAY ĐỔI
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                    color = BrosBrown
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Pick up your healthy product offers faster at the nearest store.", // THAY ĐỔI
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    color = Color.DarkGray,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Dots Indicator (ĐÃ SỬA VỚI PLACEHOLDER)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Chấm 1 (Inactive)
                    Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(BrosCream.copy(alpha = 0.5f)))
                    Spacer(modifier = Modifier.width(8.dp))
                    // Chấm 2 (Inactive)
                    Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(BrosCream.copy(alpha = 0.5f)))
                    Spacer(modifier = Modifier.width(8.dp))
                    // Chấm 3 (Active)
                    Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(BrosBrown)) // Active
                }
                Spacer(modifier = Modifier.height(24.dp))

                // Nút Hoàn thành
                Button(
                    onClick = {
                        viewModel.setOnboardingCompleted() // Gọi logic lưu trạng thái
                        navController.popBackStack()
                        navController.navigate(AppRoutes.AUTH_FLOW) // Chuyển sang flow chính
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    colors = ButtonDefaults.buttonColors(containerColor = BrosBrown)
                ) {
                    Text(text = "Get Started", style = MaterialTheme.typography.titleMedium, color = Color.White) // THAY ĐỔI
                }
            }
        }
    }
}