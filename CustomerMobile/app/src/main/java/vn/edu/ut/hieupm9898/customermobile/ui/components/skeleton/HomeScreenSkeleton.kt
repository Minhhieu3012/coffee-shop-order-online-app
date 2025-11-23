package vn.edu.ut.hieupm9898.customermobile.ui.components.skeleton

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import vn.edu.ut.hieupm9898.customermobile.ui.theme.BrosBackground

/**
 * Skeleton loading cho HomeScreen
 * Match layout:
 *  - Header (avatar + tên + chuông)
 *  - Search bar
 *  - Categories
 *  - Grid sản phẩm 2 cột
 */
@Composable
fun HomeScreenSkeleton(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(BrosBackground)
            .padding(horizontal = 24.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        // --- HEADER ---
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            SkeletonCircle(size = 60.dp)

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                SkeletonLine(width = 120.dp, height = 16.dp)
                Spacer(modifier = Modifier.height(8.dp))
                SkeletonLine(width = 160.dp, height = 18.dp)
            }

            Spacer(modifier = Modifier.width(12.dp))

            SkeletonCircle(size = 32.dp)
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- SEARCH BAR ---
        SkeletonRoundedRect(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            corner = 16.dp
        )

        Spacer(modifier = Modifier.height(24.dp))

        // --- "Phân loại" title ---
        SkeletonLine(width = 120.dp, height = 20.dp)
        Spacer(modifier = Modifier.height(16.dp))

        // --- CATEGORY CHIPS ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            repeat(4) {
                SkeletonRoundedRect(
                    modifier = Modifier
                        .height(34.dp)
                        .weight(1f),
                    corner = 18.dp
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- "Đồ uống và món ăn đi kèm" title ---
        SkeletonLine(width = 200.dp, height = 22.dp)
        Spacer(modifier = Modifier.height(16.dp))

        // --- GRID SẢN PHẨM (2 cột) ---
        repeat(4) { // 4 hàng giả
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                repeat(2) {
                    ProductCardSkeleton(
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(60.dp))
    }
}

@Composable
private fun ProductCardSkeleton(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        // Ảnh
        SkeletonRoundedRect(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp),
            corner = 20.dp
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Tên
        SkeletonLine(width = 100.dp, height = 16.dp)
        Spacer(modifier = Modifier.height(4.dp))

        // Mô tả
        SkeletonLine(width = 80.dp, height = 14.dp)
        Spacer(modifier = Modifier.height(8.dp))

        // Giá
        SkeletonLine(width = 60.dp, height = 16.dp)
    }
}

/* ---------- SHIMMER HELPERS ---------- */

@Composable
private fun SkeletonCircle(
    size: Dp,
    modifier: Modifier = Modifier
) {
    ShimmerBox(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
    )
}

@Composable
private fun SkeletonLine(
    width: Dp,
    height: Dp,
    modifier: Modifier = Modifier
) {
    ShimmerBox(
        modifier = modifier
            .width(width)
            .height(height)
            .clip(RoundedCornerShape(8.dp))
    )
}

@Composable
private fun SkeletonRoundedRect(
    modifier: Modifier = Modifier,
    corner: Dp = 12.dp
) {
    ShimmerBox(
        modifier = modifier
            .clip(RoundedCornerShape(corner))
    )
}

@Composable
private fun ShimmerBox(
    modifier: Modifier = Modifier
) {
    val baseColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.06f)
    val highlightColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.15f)

    val transition = rememberInfiniteTransition(label = "skeleton_shimmer")
    val translateAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1200,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "skeleton_translate"
    )

    val brush = Brush.linearGradient(
        colors = listOf(
            baseColor,
            highlightColor,
            baseColor
        ),
        start = Offset(translateAnim - 2000f, 0f),
        end = Offset(translateAnim, 0f)
    )

    Box(
        modifier = modifier
            .background(brush)
    )
}
