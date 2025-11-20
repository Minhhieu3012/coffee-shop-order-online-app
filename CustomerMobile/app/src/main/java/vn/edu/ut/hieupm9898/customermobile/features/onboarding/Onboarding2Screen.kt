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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import vn.edu.ut.hieupm9898.customermobile.R
import vn.edu.ut.hieupm9898.customermobile.ui.theme.BrosBackground
import vn.edu.ut.hieupm9898.customermobile.ui.theme.BrosBrown
import vn.edu.ut.hieupm9898.customermobile.ui.theme.BrosTitle
// import vn.edu.ut.hieupm9898.customermobile.ui.theme.BrosSubTitle // Có thể dùng nếu muốn màu nhạt hơn

@Composable
fun Onboarding2Screen(
    onSkip: () -> Unit, onNext: () -> Unit,
    onGetStartedClick: () -> Unit
) {
    // Sử dụng Surface và màu nền chuẩn
    Surface(color = BrosBackground, modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 40.dp),
            horizontalAlignment = Alignment.Start
        ) {
            // --- PHẦN 1: ẢNH (Chiếm 50% màn hình trên) ---
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.5f)
                    .padding(top = 50.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.image_18), // Đã sửa lỗi thiếu dấu ngoặc )
                    contentDescription = "Fast Delivery",
                    modifier = Modifier
                        .fillMaxWidth(0.8f) // Kích thước ảnh 80% chiều ngang
                        .aspectRatio(1f)
                )
            }

            // --- PHẦN 2: NỘI DUNG & ĐIỀU HƯỚNG (Chiếm 50% màn hình dưới) ---
            Column(
                modifier = Modifier
                    .weight(0.5f)
                    .padding(vertical = 20.dp),
                verticalArrangement = Arrangement.Bottom
            ) {
                val title = "Giao hàng nhanh chóng"
                val description = "Nhận đồ uống yêu thích của bạn chỉ trong vài phút với đội ngũ shipper chuyên nghiệp."

                // Tiêu đề
                Text(
                    text = title,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = BrosTitle
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Mô tả
                Text(
                    text = description,
                    fontSize = 14.sp,
                    color = BrosTitle, // Hoặc dùng BrosSubTitle nếu muốn nhạt hơn
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(60.dp))

                // --- Hàng nút bấm Skip / Next ---
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Nút Skip
                    Text(
                        text = "Skip",
                        modifier = Modifier.clickable(onClick = onSkip),
                        color = BrosBrown,
                        fontWeight = FontWeight.SemiBold
                    )

                    // Nút Next
                    Button(
                        onClick = onNext,
                        modifier = Modifier.height(48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = BrosBrown),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(text = "Next →", color = Color.White)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Onboarding2Preview() {
    Onboarding2Screen(onSkip = {}, onNext = {}, onGetStartedClick = {})
}