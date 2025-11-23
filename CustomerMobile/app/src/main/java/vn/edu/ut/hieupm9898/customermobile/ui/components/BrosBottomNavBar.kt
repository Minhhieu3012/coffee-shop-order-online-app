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
import vn.edu.ut.hieupm9898.customermobile.navigation.AppRoutes
import vn.edu.ut.hieupm9898.customermobile.ui.theme.CustomerMobileTheme

// Định nghĩa mỗi item trong bottom bar
private data class NavItem(
    val label: String,
    val icon: ImageVector,
    val route: String
)

// Danh sách item, DÙNG route trong AppRoutes để khớp với NavHost
private val navItems = listOf(
    NavItem("Trang chủ", Icons.Filled.Home, AppRoutes.HOME),         // "home"
    NavItem("Yêu thích", Icons.Filled.Favorite, AppRoutes.FAVORITE), // "favorite"
    NavItem("Giỏ hàng", Icons.Filled.ShoppingCart, AppRoutes.CART),  // "cart"
    NavItem("Hồ sơ", Icons.Filled.Person, AppRoutes.PROFILE)         // "profile"
)

/**
 * Thanh điều hướng (Navigation Bar) "nổi" bo tròn.
 *
 * @param currentRoute route hiện tại lấy từ NavController (destination.route)
 * @param onNavigate callback điều hướng: nhận đúng route string để NavController.navigate(route)
 */
@Composable
fun BrosBottomNavBar(
    currentRoute: String,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier
            .padding(horizontal = 24.dp, vertical = 60.dp)
            .clip(RoundedCornerShape(30.dp)), // Bo góc, tạo feeling "floating"
        containerColor = MaterialTheme.colorScheme.primary,
        tonalElevation = 8.dp
    ) {
        navItems.forEach { item ->
            val selected = currentRoute == item.route

            NavigationBarItem(
                selected = selected,
                // tránh navigate lại cùng route đang đứng
                onClick = {
                    if (!selected) {
                        onNavigate(item.route)
                    }
                },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label,
                        modifier = Modifier.size(36.dp),
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                },
                label = { Text(item.label) },
                alwaysShowLabel = false,
                colors = NavigationBarItemDefaults.colors(
                    unselectedIconColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f),
                    unselectedTextColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f),
                    selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                    selectedTextColor = MaterialTheme.colorScheme.onPrimary,
                    indicatorColor = Color.Transparent // không show cái "pill" M3
                )
            )
        }
    }
}

@Preview(showBackground = true, name = "Bottom Nav Bar")
@Composable
fun BrosBottomNavBarPreview() {
    CustomerMobileTheme {
        var selectedRoute by remember { mutableStateOf(AppRoutes.HOME) }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.BottomCenter
        ) {
            BrosBottomNavBar(
                currentRoute = selectedRoute,
                onNavigate = { newRoute -> selectedRoute = newRoute }
            )
        }
    }
}
