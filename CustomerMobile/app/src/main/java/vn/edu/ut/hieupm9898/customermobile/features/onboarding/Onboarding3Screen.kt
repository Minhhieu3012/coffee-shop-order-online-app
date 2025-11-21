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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import vn.edu.ut.hieupm9898.customermobile.R
// IMPORTS MÀU SẮC ĐÃ SỬA:
import vn.edu.ut.hieupm9898.customermobile.ui.theme.BrosBackground
import vn.edu.ut.hieupm9898.customermobile.ui.theme.BrosBrown
import vn.edu.ut.hieupm9898.customermobile.ui.theme.BrosSubTitle
import vn.edu.ut.hieupm9898.customermobile.ui.theme.BrosTitle

@Composable
fun Onboarding3Screen(
    onSkip: () -> Unit, onNext: () -> Unit,
    onGetStartedClick: () -> Unit
) {
    Surface(color = BrosBackground, modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.Start
        ) {
            // --- 1. Vị trí ảnh ---
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.5f)
                    .padding(top = 200.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.onboarding3), // NỘI DUNG MÀN HÌNH 3
                    contentDescription = "Grab và Go",
                    modifier = Modifier
                        .size(420.dp)
                )
            }

            // --- 2. Nội dung Text và Điều hướng ---
            Column(
                modifier = Modifier
                    .weight(0.5f),
                verticalArrangement = Arrangement.Bottom
            ) {
                val title = "Grab và Go"
                val description = "Nhận ngay cà phê mới pha mà bạn yêu thích—không cần phải xếp hàng chờ đợi."

                Text(text = title,
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    color = BrosTitle
                )

                Spacer(modifier = Modifier.height(18.dp))
                
                Text(text = description,
                    fontSize = 18.sp,
                    color = BrosSubTitle,
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(100.dp))

                // --- 3. Điều hướng (Skip/Next) ---
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = "Bỏ qua",
                        modifier = Modifier.clickable(onClick = onSkip),
                        color = BrosBrown,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 22.sp
                    )

                    Button(
                        onClick = onNext,
                        modifier = Modifier.height(55.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = BrosBrown),
                        shape = RoundedCornerShape(12.dp)
                    ) {

                        Text(text = "Tiếp tục →",
                            color = Color.White,
                            fontSize = 22.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(70.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Onboarding3ScreenPreview() {
    Onboarding3Screen(onSkip = {}, onNext = {},  onGetStartedClick = {})
}