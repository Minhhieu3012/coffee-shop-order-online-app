package vn.edu.ut.hieupm9898.customermobile.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import vn.edu.ut.hieupm9898.customermobile.ui.theme.CustomerMobileTheme

/**
 * Component Loading toàn màn hình đẹp mắt với animation
 *
 * @param modifier Tùy chỉnh từ bên ngoài
 * @param loadingText Text hiển thị dưới spinner (optional)
 * @param showCard Hiển thị card bo góc bọc xung quanh spinner (mặc định: true)
 * @param backgroundColor Màu nền overlay (mặc định: đen 60% opacity)
 * @param spinnerColor Màu của spinner (mặc định: primary color)
 * @param spinnerSize Kích thước spinner (mặc định: 56.dp)
 * @param cardElevation Độ cao của card (mặc định: 8.dp)
 */
@Composable
fun FullScreenLoading(
    modifier: Modifier = Modifier,
    loadingText: String? = null,
    showCard: Boolean = true,
    backgroundColor: Color = MaterialTheme.colorScheme.background.copy(alpha = 0.7f),
    spinnerColor: Color = MaterialTheme.colorScheme.primary,
    spinnerSize: Dp = 56.dp,
    cardElevation: Dp = 8.dp
) {
    // Animation fade in
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        visible = true
    }

    val alpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(durationMillis = 300),
        label = "alpha"
    )

    // Animation scale pulse
    val infiniteTransition = rememberInfiniteTransition(label = "scale")
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundColor)
            .alpha(alpha)
            .clickable(enabled = false, onClick = {}), // Chặn click
        contentAlignment = Alignment.Center
    ) {
        if (showCard) {
            // Hiển thị với card
            Surface(
                modifier = Modifier
                    .wrapContentSize()
                    .scale(scale),
                shape = RoundedCornerShape(24.dp),
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = cardElevation
            ) {
                Column(
                    modifier = Modifier.padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(spinnerSize),
                        color = spinnerColor,
                        strokeWidth = 4.dp
                    )

                    if (loadingText != null) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = loadingText,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        } else {
            // Hiển thị không có card
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.scale(scale)
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(spinnerSize),
                    color = spinnerColor,
                    strokeWidth = 4.dp
                )

                if (loadingText != null) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = loadingText,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true, name = "Loading with Card")
@Composable
fun FullScreenLoadingWithCardPreview() {
    CustomerMobileTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            // Nội dung giả lập bên dưới
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Nội dung phía sau", style = MaterialTheme.typography.headlineMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Đây là màn hình bị che mờ bởi loading overlay")
            }

            // Loading overlay
            FullScreenLoading(
                loadingText = "Loading...",
                showCard = true
            )
        }
    }
}
