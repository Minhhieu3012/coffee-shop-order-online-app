package vn.edu.ut.hieupm9898.customermobile.features.auth

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import vn.edu.ut.hieupm9898.customermobile.navigation.AppRoutes

fun NavGraphBuilder.authNavGraph() {
    composable(AppRoutes.LOGIN) {
        LoginScreen(navController = it.navController)
    }

    composable(AppRoutes.REGISTER) {
        RegisterScreen(navController = it.navController)
    }

    composable(AppRoutes.FORGOT_PASSWORD) {
        ForgotPasswordScreen(navController = it.navController)
    }

    composable(AppRoutes.OTP_VERIFICATION) {
        OTPVerificationScreen(navController = it.navController)
    }

    composable(AppRoutes.RESET_PASSWORD) {
        ResetPasswordScreen(navController = it.navController)
    }

    composable(AppRoutes.PROFILE) {
        CreateProfileScreen(navController = it.navController)
    }
}