// features/onboarding/SplashScreen.kt

package vn.edu.ut.hieupm9898.customermobile.features.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color // Cần thiết cho Color.White
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import vn.edu.ut.hieupm9898.customermobile.R
// IMPORTS MÀU SẮC ĐÃ SỬA:
import vn.edu.ut.hieupm9898.customermobile.ui.theme.BrosBackground // Thay thế CreamBackground
import vn.edu.ut.hieupm9898.customermobile.ui.theme.BrosBrown      // Thay thế PrimaryBrown (cho Text)
import vn.edu.ut.hieupm9898.customermobile.ui.theme.BrosButton     // Thay thế ButtonBrown (cho Nút)
import vn.edu.ut.hieupm9898.customermobile.ui.theme.BrosTitle      // Có thể dùng cho tiêu đề nếu cần thiết

@Composable
fun SplashScreen(onGetStartedClick: () -> Unit) {
    // SỬA: Dùng BrosBackground cho màu nền
    Surface(color = BrosBackground, modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 40.dp)
                .padding(bottom = 50.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // ... bên trong Column cha
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                // 1. Logo
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Bros coffee Logo",
                    modifier = Modifier
                        .size(100.dp)
                        // SỬA LỖI TẠI ĐÂY: Dùng Alignment.Start
                        .align(Alignment.Start)
                )

                Spacer(modifier = Modifier.height(32.dp))
                // ...

                // 2. Tiêu đề
                // SỬA: Dùng BrosBrown
                Text(text = "Skip the line", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = BrosBrown )
                // SỬA: Dùng BrosBrown
                Text(text = "Keep the chill", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = BrosBrown )

                Spacer(modifier = Modifier.height(16.dp))

                // 3. Mô tả
                // SỬA: Dùng BrosBrown
                Text(
                    text = "Order your favorite coffee anytime, anywhere. Just a few taps and your drink is ready to go.",
                    fontSize = 14.sp,
                    color = BrosBrown,
                    lineHeight = 20.sp
                )
            }

            // 4. Nút "Get Started"
            Button(
                onClick = onGetStartedClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                // SỬA: Dùng BrosButton cho màu nền nút
                colors = ButtonDefaults.buttonColors(containerColor = BrosButton),
                shape = RoundedCornerShape(12.dp)
            ) {
                // SỬA: Dùng Color.White
                Text(text = "Get Started", fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SplashScreenPreview() {
    SplashScreen(onGetStartedClick = {})
}