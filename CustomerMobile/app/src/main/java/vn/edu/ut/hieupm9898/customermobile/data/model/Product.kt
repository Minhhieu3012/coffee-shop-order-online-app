package vn.edu.ut.hieupm9898.customermobile.data.model

/**
 * Đại diện cho một sản phẩm trên Firestore (Collection "products")
 */
data class Product(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val price: Double = 0.0,
    val imageUrl: String = "",
    val category: String = "",
    val isFavorite: Boolean = false,
    // Đơn giản hóa options cho demo, thực tế có thể là List<Map<String, Any>>
    val isAvailable: Boolean = true
)