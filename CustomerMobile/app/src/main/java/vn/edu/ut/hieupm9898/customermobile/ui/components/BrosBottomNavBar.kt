package vn.edu.ut.hieupm9898.customermobile.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import vn.edu.ut.hieupm9898.customermobile.ui.theme.CustomerMobileTheme

// 1. Định nghĩa các "Item" (Mục) cho thanh điều hướng
// (Người "Từ trên xuống" sẽ dùng cái này để định nghĩa các route)
private data class NavItem(
    val label: String,
    val icon: ImageVector,
    val route: String
)

// 2. Danh sách các item
private val navItems = listOf(
    NavItem("Home", Icons.Default.Home, "home"),
    NavItem("Favorite", Icons.Default.Favorite, "favorite"),
    NavItem("Cart", Icons.Default.ShoppingCart, "cart"),
    NavItem("Profile", Icons.Default.Person, "profile")
)

/**
 * Thanh điều hướng (Navigation Bar) "nổi" (floating) bo tròn.
 *
 * @param currentRoute Route (tuyến đường) của màn hình hiện tại (để highlight icon).
 * @param onNavigate Hàm (lambda) được gọi khi một item được nhấn.
 */
@Composable
fun BrosBottomNavBar(
    currentRoute: String,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier
            .padding(horizontal = 24.dp, vertical = 20.dp) // Tạo khoảng cách "nổi"
            .clip(RoundedCornerShape(30.dp)), // Bo tròn như trong ảnh
        containerColor = MaterialTheme.colorScheme.primary,
        tonalElevation = 8.dp // Đổ bóng
    ) {
        // 3. Lặp qua danh sách item và tạo icon
        navItems.forEach { item ->
            NavigationBarItem(
                // 4. Kiểm tra xem item này có đang được chọn không
                selected = (currentRoute == item.route),

                // 5. Hàm xử lý khi nhấn
                onClick = { onNavigate(item.route) },

                // 6. Icon
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label,
                        modifier = Modifier.size(36.dp),
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                },

                // 7. Ẩn nhãn (label)
                label = { Text(item.label) },
                alwaysShowLabel = false,

                // 8. Tùy chỉnh màu sắc
                colors = NavigationBarItemDefaults.colors(
                    // Màu icon khi *không* được chọn (màu tan/beige)
                    unselectedIconColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f),
                    unselectedTextColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f),

                    // Màu icon khi *được* chọn (màu tan/beige, nhưng sáng hơn)
                    selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                    selectedTextColor = MaterialTheme.colorScheme.onPrimary,

                    // Ẩn cái "vòng tròn" (indicator) của Material 3
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}


@Preview(showBackground = true, name = "Bottom Nav Bar")
@Composable
fun BrosBottomNavBarPreview() {
    CustomerMobileTheme {
        // Biến "nháp" để lưu trạng thái (test)
        var selectedRoute by remember { mutableStateOf("home") }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.BottomCenter // Đặt Bar ở dưới
        ) {
            BrosBottomNavBar(
                currentRoute = selectedRoute,
                onNavigate = { newRoute -> selectedRoute = newRoute }
            )
        }
    }
}