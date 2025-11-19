package vn.edu.ut.hieupm9898.customermobile.features.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import vn.edu.ut.hieupm9898.customermobile.features.cart.CartScreen
import vn.edu.ut.hieupm9898.customermobile.features.home.HomeScreen
import vn.edu.ut.hieupm9898.customermobile.features.profile.ProfileScreen
import vn.edu.ut.hieupm9898.customermobile.navigation.AppRoutes
import vn.edu.ut.hieupm9898.customermobile.ui.components.BrosBottomNavBar

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: AppRoutes.HOME

    Scaffold(
        bottomBar = {
            // Tích hợp BrosBottomNavBar
            BrosBottomNavBar(
                currentRoute = currentRoute,
                onNavigate = { route ->
                    navController.navigate(route) {
                        popUpTo(AppRoutes.HOME) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    ) { paddingValues ->
        // NavHost chứa các tab chính của Bottom Bar
        NavHost(
            navController = navController,
            startDestination = AppRoutes.HOME,
            modifier = Modifier.padding(bottom = paddingValues.calculateBottomPadding() - 20.dp)
        ) {
            composable(AppRoutes.HOME) { HomeScreen(navController) }
            composable(AppRoutes.CART) { CartScreen(navController) }
            composable(AppRoutes.PROFILE) { ProfileScreen(navController) }
            // Thêm các composable khác nếu cần (ví dụ: màn hình Product Detail không có bottom bar)
        }
    }
}