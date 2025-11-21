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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import vn.edu.ut.hieupm9898.customermobile.R
import vn.edu.ut.hieupm9898.customermobile.ui.theme.BrosBackground
import vn.edu.ut.hieupm9898.customermobile.ui.theme.BrosButton
import vn.edu.ut.hieupm9898.customermobile.ui.theme.BrosSubTitle
import vn.edu.ut.hieupm9898.customermobile.ui.theme.BrosTitle

@Composable
fun SplashScreen(
    onGetStartedClick: () -> Unit,
    onTimeout: () -> Unit
) {
    Surface(color = BrosBackground, modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 100.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // 1. Logo
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Bros coffee Logo",
                    modifier = Modifier
                        .size(400.dp)
                )

                Spacer(modifier = Modifier.height(32.dp))

                // 2. Tiêu đề
                Text(text = "Skip the line", fontSize = 40.sp, fontWeight = FontWeight.Bold, color = BrosTitle)

                Text(text = "Keep the chill", fontSize = 40.sp, fontWeight = FontWeight.Bold, color = BrosTitle )

                Spacer(modifier = Modifier.height(16.dp))

                // 3. Mô tả
                Text(
                    text = "Order your favorite coffee anytime, anywhere. Just a few taps and your drink is ready to go.",
                    fontSize = 18.sp,
                    color = BrosSubTitle,
                    lineHeight = 20.sp,
                    textAlign = TextAlign.Center
                )
            }

            // 4. Nút "Get Started"
            Button(
                onClick = onGetStartedClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp),
                // SỬA: Dùng BrosButton cho màu nền nút
                colors = ButtonDefaults.buttonColors(containerColor = BrosButton),
                shape = RoundedCornerShape(12.dp)
            ) {
                // SỬA: Dùng Color.White
                Text(text = "Get Started", fontSize = 30.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SplashScreenPreview() {
    SplashScreen(onGetStartedClick = {}, onTimeout = {})
}