package vn.edu.ut.hieupm9898.customermobile.features.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
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
import androidx.navigation.NavController
import vn.edu.ut.hieupm9898.customermobile.R
import vn.edu.ut.hieupm9898.customermobile.ui.theme.BrosBackground
import vn.edu.ut.hieupm9898.customermobile.ui.theme.BrosButton
import vn.edu.ut.hieupm9898.customermobile.ui.theme.BrosSubTitle
import vn.edu.ut.hieupm9898.customermobile.ui.theme.BrosTitle

@Composable
fun SplashScreen(
    navController: NavController,
    onGetStartedClick: () -> Unit,
) {
    // --- ĐÃ XÓA PHẦN KIỂM TRA TỰ ĐỘNG ĐĂNG NHẬP ---
    // Bây giờ màn hình này chỉ đơn thuần là hiển thị Logo

    Surface(color = BrosBackground, modifier = Modifier.fillMaxSize()) {
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
                Text(text = "Ngừng so sánh", fontSize = 40.sp, fontWeight = FontWeight.Bold, color = BrosTitle)
                Text(text = "Hãy tận hưởng", fontSize = 40.sp, fontWeight = FontWeight.Bold, color = BrosTitle )
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
                modifier = Modifier.fillMaxWidth().height(80.dp),
                colors = ButtonDefaults.buttonColors(containerColor = BrosButton),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(text = "Bắt đầu", fontSize = 30.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
            }
        }
    }
}