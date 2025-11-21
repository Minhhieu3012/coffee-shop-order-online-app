package vn.edu.ut.hieupm9898.customermobile.features.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import vn.edu.ut.hieupm9898.customermobile.R
import vn.edu.ut.hieupm9898.customermobile.ui.theme.BrosBackground
import vn.edu.ut.hieupm9898.customermobile.ui.theme.BrosBrown
import vn.edu.ut.hieupm9898.customermobile.ui.theme.BrosSubTitle
import vn.edu.ut.hieupm9898.customermobile.ui.theme.BrosTitle


@Composable
fun Onboarding1Screen(
    onSkip: () -> Unit, onNext: () -> Unit,
    onGetStartedClick: () -> Unit
) {
    // 1. Dùng BrosBackground
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
                    .weight(0.5f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.onboarding1),
                    contentDescription = "Đặt hàng đơn giản",
                    modifier = Modifier
                        .size(420.dp)
                )
            }

            // --- 2. Nội dung Text và Điều hướng ---
            Column(
                modifier = Modifier
                    .weight(0.3f),
                verticalArrangement = Arrangement.Bottom
            ) {
                val title = "Đặt hàng đơn giản"
                val description = "Duyệt qua thực đơn đa dạng của chúng tôi và chọn đồ uống yêu thích của bạn, từ cà phê espresso đến trà sữa."

                Text(
                    text = title,
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    color = BrosTitle
                )
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = description,
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
                    // Dùng BrosBrown cho nút Skip (Text)
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

                        Text(
                            text = "Tiếp tục →",
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

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun Onboarding1ScreenPreview() {
    Onboarding1Screen(onSkip = {}, onNext = {}, onGetStartedClick = {})
}