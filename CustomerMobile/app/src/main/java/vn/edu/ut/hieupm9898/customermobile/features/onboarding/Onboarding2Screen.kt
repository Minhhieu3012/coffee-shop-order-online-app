// features/onboarding/Onboarding2Screen.kt

package vn.edu.ut.hieupm9898.customermobile.features.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import vn.edu.ut.hieupm9898.customermobile.ui.theme.BrosBrown      // Thay thế PrimaryBrown (Dùng cho Text và Nút)
import vn.edu.ut.hieupm9898.customermobile.ui.theme.BrosTitle      // Có thể dùng cho tiêu đề chính

@Composable
fun Onboarding2Screen(onSkip: () -> Unit, onNext: () -> Unit) {
    // SỬA: Dùng BrosBackground
    Surface(color = BrosBackground, modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 40.dp),
            horizontalAlignment = Alignment.Start
        ) {
            // --- 1. Vị trí ảnh ---
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.5f)
                    .padding(top = 50.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.image_18),
                    contentDescription = "Easy and Secure Payment",
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .aspectRatio(1f)
                )
            }

            // --- 2. Nội dung Text và Điều hướng ---
            Column(
                modifier = Modifier
                    .weight(0.5f)
                    .padding(vertical = 20.dp),
                verticalArrangement = Arrangement.Bottom
            ) {
                val title = "Easy and Secure Payment!"
                val description = "Choose from multiple payment options that are fast, safe, and convenient."

                // SỬA: Dùng BrosBrown cho Text
                Text(text = title, fontSize = 28.sp, fontWeight = FontWeight.Bold, color = BrosBrown)
                Spacer(modifier = Modifier.height(16.dp))
                // SỬA: Dùng BrosBrown cho Text
                Text(text = description, fontSize = 14.sp, color = BrosBrown, lineHeight = 20.sp)

                Spacer(modifier = Modifier.height(60.dp))

                // --- 3. Điều hướng (Skip/Next) ---
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // SỬA: Dùng BrosBrown cho Text
                    Text(
                        text = "Skip",
                        modifier = Modifier.clickable(onClick = onSkip),
                        color = BrosBrown,
                        fontWeight = FontWeight.SemiBold
                    )

                    Button(
                        onClick = onNext,
                        modifier = Modifier.height(48.dp),
                        // SỬA: Dùng BrosBrown cho màu nền nút
                        colors = ButtonDefaults.buttonColors(containerColor = BrosBrown),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        // SỬA: Dùng Color.White (vì White không được định nghĩa trong Color.kt mới)
                        Text(text = "Next →", color = Color.White)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Onboarding2ScreenPreview() {
    Onboarding2Screen(onSkip = {}, onNext = {})
}