package vn.edu.ut.hieupm9898.customermobile.navigation

/**
 * Định nghĩa tất cả các tuyến đường (Routes) dưới dạng hằng số.
 */
object AppRoutes {

    // Khởi động
    const val SPLASH = "splash"
    const val ONBOARDING = "onboarding"
    const val ONBOARDING_1 = "onboarding_1"
    const val ONBOARDING_2 = "onboarding_2"
    const val ONBOARDING_3 = "onboarding_3"

    // Authentication
    const val AUTH_FLOW = "auth_flow" // Đã sửa từ AUTH_GRAPH để khớp AppNavigation
    const val AUTH_GRAPH = "auth_graph" // Giữ lại nếu MainScreen dùng
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val OTP_VERIFICATION = "otp_verification"
    const val RESET_PASSWORD = "reset_password"
    const val FORGOT_PASSWORD = "forgot_password"

    // Main App
    const val MAIN_APP_GRAPH = "main_app_graph"

    // Main Tabs (Bottom Bar)
    const val HOME = "home"
    const val FAVORITE = "favorite"
    const val CART = "cart"
    const val PROFILE = "profile"

    // Sub Screens
    const val DETAIL = "detail/{id}"
    const val DETAIL_BASE = "detail"
    const val SEARCH = "search"

    // Product Detail (Đã sửa để khớp AppNavigation)
    const val PRODUCT_DETAIL_ID = "productId"
    const val PRODUCT_DETAIL = "product_detail/{$PRODUCT_DETAIL_ID}"

    // Cart Flow
    const val PAYMENT_QR = "payment_qr"
    const val ORDER_SUCCESS = "order_success"
    const val DELIVERY = "delivery"
    const val PAYMENT_METHODS = "payment_methods" // Đã thêm

    // Profile Sub Screens
    const val EDIT_PROFILE = "edit_profile"
    const val ADDRESS_LIST = "address_list"
    const val ADD_ADDRESS = "add_address"
    const val ORDER_HISTORY = "order_history"
    const val SETTINGS = "settings"
    const val CHANGE_PASS = "change_password"
    const val CONTACT = "contact_us"
    const val NOTIFICATIONS = "notifications"
    const val REWARDS = "rewards"
    const val FEEDBACK = "feedback"
    const val DELETE_ACCOUNT = "delete_account"

    // Helper functions
    fun createProductDetailRoute(productId: String): String {
        return "product_detail/$productId"
    }
}