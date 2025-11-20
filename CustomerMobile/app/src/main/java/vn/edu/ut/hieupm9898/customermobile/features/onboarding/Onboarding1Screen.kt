// features/onboarding/Onboarding1Screen.kt

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
// IMPORTS MỚI TỪ FILE CỦA BẠN:
import vn.edu.ut.hieupm9898.customermobile.ui.theme.BrosBackground
import vn.edu.ut.hieupm9898.customermobile.ui.theme.BrosBrown
import vn.edu.ut.hieupm9898.customermobile.ui.theme.BrosButton
import vn.edu.ut.hieupm9898.customermobile.ui.theme.BrosTitle


@Composable
fun Onboarding1Screen(
    onSkip: () -> Unit, onNext: () -> Unit,
    onGetStartedClick: () -> Unit
) {
    // 1. Dùng BrosBackground (thay cho CreamBackground)
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
                    painter = painterResource(id = R.drawable.image_17),
                    contentDescription = "Order Made Simple",
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
                val title = "Order Made Simple"
                val description = "Browse our wide menu and pick your favorite drink, from espresso to milk tea."

                // Dùng BrosTitle (hoặc BrosBrown) cho Text chính
                Text(text = title, fontSize = 28.sp, fontWeight = FontWeight.Bold, color = BrosTitle)
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = description, fontSize = 14.sp, color = BrosTitle, lineHeight = 20.sp)

                Spacer(modifier = Modifier.height(60.dp))

                // --- 3. Điều hướng (Skip/Next) ---
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Dùng BrosBrown cho nút Skip (Text)
                    Text(
                        text = "Skip",
                        modifier = Modifier.clickable(onClick = onSkip),
                        color = BrosBrown,
                        fontWeight = FontWeight.SemiBold
                    )

                    Button(
                        onClick = onNext,
                        modifier = Modifier.height(48.dp),
                        // Dùng BrosBrown cho màu nền nút Next
                        colors = ButtonDefaults.buttonColors(containerColor = BrosBrown),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        // Dùng Color.White hoặc định nghĩa White trong Color.kt nếu cần
                        Text(text = "Next →", color = Color.White)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Onboarding1ScreenPreview() {
    Onboarding1Screen(onSkip = {}, onNext = {}, onGetStartedClick = {})
}