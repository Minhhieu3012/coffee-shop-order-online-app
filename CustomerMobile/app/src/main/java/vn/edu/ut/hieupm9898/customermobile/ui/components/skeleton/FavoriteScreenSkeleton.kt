package vn.edu.ut.hieupm9898.customermobile.ui.components.skeleton

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import vn.edu.ut.hieupm9898.customermobile.ui.theme.BrosBackground

@Composable
fun FavoriteScreenSkeleton() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BrosBackground)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
            .padding(top = 12.dp)
    ) {
        // Giả lập Grid 2 cột
        repeat(4) { // 4 hàng
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                repeat(2) { // 2 cột
                    FavoriteCardSkeleton(modifier = Modifier.weight(1f))
                }
            }
        }
        Spacer(modifier = Modifier.height(100.dp))
    }
}

@Composable
private fun FavoriteCardSkeleton(
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        // Ảnh sản phẩm
        SkeletonRoundedRect(
            modifier = Modifier
                .fillMaxWidth()
                .height(170.dp), // Chiều cao ảnh
            corner = 16.dp
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Tên sản phẩm
        SkeletonLine(width = 100.dp, height = 16.dp)
        Spacer(modifier = Modifier.height(4.dp))

        // Mô tả ngắn
        SkeletonLine(width = 80.dp, height = 14.dp)
        Spacer(modifier = Modifier.height(8.dp))

        // Giá và nút Add
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            SkeletonLine(width = 60.dp, height = 16.dp)
            SkeletonCircle(size = 32.dp)
        }
    }
}

/* --- CÁC HÀM HELPER COPY TỪ HOME SKELETON (ĐỂ ĐỘC LẬP) --- */

@Composable
private fun SkeletonCircle(size: Dp, modifier: Modifier = Modifier) {
    ShimmerBox(modifier = modifier.size(size).clip(CircleShape))
}

@Composable
private fun SkeletonLine(width: Dp, height: Dp, modifier: Modifier = Modifier) {
    ShimmerBox(modifier = modifier.width(width).height(height).clip(RoundedCornerShape(8.dp)))
}

@Composable
private fun SkeletonRoundedRect(modifier: Modifier = Modifier, corner: Dp = 12.dp) {
    ShimmerBox(modifier = modifier.clip(RoundedCornerShape(corner)))
}

@Composable
private fun ShimmerBox(modifier: Modifier = Modifier) {
    val baseColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.06f)
    val highlightColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.15f)

    val transition = rememberInfiniteTransition(label = "fav_shimmer")
    val translateAnim by transition.animateFloat(
        initialValue = 0f, targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = "fav_translate"
    )

    val brush = Brush.linearGradient(
        colors = listOf(baseColor, highlightColor, baseColor),
        start = Offset(translateAnim - 2000f, 0f),
        end = Offset(translateAnim, 0f)
    )

    Box(modifier = modifier.background(brush))
}