package vn.edu.ut.hieupm9898.customermobile.navigation

/**
 * Định nghĩa tất cả các tuyến đường (Routes) dưới dạng hằng số.
 */
object AppRoutes {

    // Khởi động
    const val SPLASH = "splash"
    const val ONBOARDING = "onboarding"

    // Authentication
    const val AUTH_GRAPH = "auth_graph"
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val OTP = "otp_verification"

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
    const val STORE_LOCATOR = "store_locator"

    // Cart Flow
    const val PAYMENT_QR = "payment_qr"
    const val ORDER_SUCCESS = "order_success"
    const val DELIVERY = "delivery"

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
    const val LANGUAGE = "language"
}