package vn.edu.ut.hieupm9898.customermobile.data.model

// Nhớ import class Product nếu thấy báo đỏ chữ Product
data class CartItem(
    val product: Product,
    var quantity: Int
) {
    val totalPrice: Double
        get() = product.price * quantity
}