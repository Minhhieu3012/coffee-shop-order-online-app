package vn.edu.ut.hieupm9898.customermobile.features.auth

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import vn.edu.ut.hieupm9898.customermobile.navigation.AppRoutes

fun NavGraphBuilder.authNavGraph(navController: NavController) {
    composable(AppRoutes.LOGIN) {
        LoginScreen(navController = navController)
    }

    composable(AppRoutes.REGISTER) {
        RegisterScreen(navController = navController)
    }

    composable(AppRoutes.FORGOT_PASSWORD) {
        ForgotPasswordScreen(navController = navController)
    }

    composable(AppRoutes.OTP_VERIFICATION) {
        OTPVerificationScreen(navController = navController)
    }

    composable(AppRoutes.RESET_PASSWORD) {
        ResetPasswordScreen(navController = navController)
    }

    composable(AppRoutes.PROFILE) {
        CreateProfileScreen(navController = navController)
    }
}