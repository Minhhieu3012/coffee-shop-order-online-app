package vn.edu.ut.hieupm9898.customermobile.navigation

object AppRoutes {
    // --- 1. LUỒNG KHỞI ĐỘNG ---
    const val SPLASH = "splash_screen"
    const val ONBOARDING_1 = "onboarding_1_screen"
    const val ONBOARDING_2 = "onboarding_2_screen"
    const val ONBOARDING_3 = "onboarding_3_screen"

    // --- 2. LUỒNG XÁC THỰC ---
    const val AUTH_FLOW = "auth_flow" // Dẫn đến LoginScreen/RegisterScreen
    const val REGISTER = "register_screen"

    // --- 3. LUỒNG CHÍNH ---
    const val HOME = "home_screen"
    const val CART = "cart_screen"
    const val PROFILE = "profile_screen"

    // --- 4. LUỒNG CHI TIẾT ---
    const val PRODUCT_DETAIL_ID = "productId"
    const val PRODUCT_DETAIL = "product_detail/{$PRODUCT_DETAIL_ID}" // Route có tham số
}