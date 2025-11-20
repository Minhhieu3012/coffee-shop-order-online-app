package vn.edu.ut.hieupm9898.customermobile.features.auth

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import vn.edu.ut.hieupm9898.customermobile.navigation.AppRoutes

fun NavGraphBuilder.authNavGraph(
    navController: NavHostController,
    onLoginSuccess: () -> Unit
) {
    navigation(
        route = AppRoutes.AUTH_GRAPH,
        startDestination = AppRoutes.LOGIN
    ) {

        // 1. Màn hình Login
        composable(AppRoutes.LOGIN) {
            LoginScreen(
                navController = navController,
                onLoginSuccess = onLoginSuccess
            )
        }

        // 2. Màn hình Register
        composable(AppRoutes.REGISTER) {
            RegisterScreen(
                navController = navController
            )
        }

        // 3. Màn hình Forgot Password
        composable(AppRoutes.FORGOT_PASSWORD) {
            ForgotPasswordScreen(
                navController = navController
            )
        }

        // 4. OTP Verification
        composable(AppRoutes.OTP_VERIFICATION) {
            OTPVerificationScreen(
                navController = navController
            )
        }

        // 5. Reset Password
        composable(AppRoutes.RESET_PASSWORD) {
            ResetPasswordScreen(
                navController = navController
            )
        }

        // 6. Create Profile
        composable(AppRoutes.PROFILE) {
            CreateProfileScreen(
                navController = navController
            )
        }
    }
}