package vn.edu.ut.hieupm9898.customermobile.navigation

object AppRoutes {
    const val SPLASH = "splash_screen"
    const val ONBOARDING_1 = "onboarding_1_screen"
    const val ONBOARDING_2 = "onboarding_2_screen"
    const val ONBOARDING_3 = "onboarding_3_screen"

    const val AUTH_FLOW = "auth_flow"
    const val LOGIN = "login_screen"
    const val REGISTER = "register_screen"
    const val FORGOT_PASSWORD = "forgot_password"
    const val OTP_VERIFICATION = "otp_verification"
    const val RESET_PASSWORD = "reset_password"

    const val HOME = "home_screen"
    const val CART = "cart_screen"
    const val PROFILE = "profile_screen"

    const val PRODUCT_DETAIL_ID = "productId"
    const val PRODUCT_DETAIL = "product_detail/{$PRODUCT_DETAIL_ID}"

    fun createProductDetailRoute(productId: String) = "product_detail/$productId"
}