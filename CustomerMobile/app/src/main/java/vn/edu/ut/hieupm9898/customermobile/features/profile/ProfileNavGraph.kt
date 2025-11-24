package vn.edu.ut.hieupm9898.customermobile.features.profile

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import vn.edu.ut.hieupm9898.customermobile.navigation.AppRoutes

/**
 * Navigation Graph cho tất cả màn hình liên quan đến Profile
 * Gọi hàm này trong AppNavigation để thêm tất cả routes
 */
fun NavGraphBuilder.profileNavGraph(navController: NavHostController) {

    // 1. Chỉnh sửa hồ sơ
    composable(AppRoutes.EDIT_PROFILE) {
        EditProfileScreen(
            onBackClick = { navController.navigateUp() }
        )
    }

    // 2. Danh sách địa chỉ
    composable(AppRoutes.ADDRESS_LIST) {
        AddressScreen(
            onBackClick = { navController.navigateUp() },
            onAddAddressClick = { navController.navigate(AppRoutes.ADD_ADDRESS) }
        )
    }

    // 3. Thêm địa chỉ mới
    composable(AppRoutes.ADD_ADDRESS) {
        AddAddressScreen(
            onBackClick = { navController.navigateUp() }
        )
    }

    // 4. Phương thức thanh toán
    composable(AppRoutes.PAYMENT_METHODS) {
        PaymentMethodScreen(
            onBackClick = { navController.navigateUp() }
        )
    }

    // 5. Đổi mật khẩu
    composable(AppRoutes.CHANGE_PASS) {
        ChangePasswordScreen(
            onBackClick = { navController.navigateUp() }
        )
    }

    // 6. Thông báo
    composable(AppRoutes.NOTIFICATIONS) {
        NotificationScreen(
            onBackClick = { navController.navigateUp() }
        )
    }

    // 7. Điểm thưởng
    composable(AppRoutes.REWARDS) {
        RewardsScreen(
            onBackClick = { navController.navigateUp() }
        )
    }

    // 8. Cài đặt
    composable(AppRoutes.SETTINGS) {
        SettingsScreen(
            onBackClick = { navController.navigateUp() }
        )
    }

    // 9. Phản hồi
    composable(AppRoutes.FEEDBACK) {
        FeedbackScreen(
            onBackClick = { navController.navigateUp() }
        )
    }

    // 10. Liên hệ
    composable(AppRoutes.CONTACT) {
        ContactUsScreen(
            onBackClick = { navController.navigateUp() }
        )
    }

    // 11. Xóa tài khoản (nếu cần)
    composable(AppRoutes.DELETE_ACCOUNT) {
        DeleteAccountScreen(
            onBackClick = { navController.navigateUp() }
        )
    }
}