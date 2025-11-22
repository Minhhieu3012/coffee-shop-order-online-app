package vn.edu.ut.hieupm9898.customermobile.data.repository

import vn.edu.ut.hieupm9898.customermobile.data.model.Product
import vn.edu.ut.hieupm9898.customermobile.R
import javax.inject.Inject

class ProductRepository @Inject constructor() {

    // [FIX 1] Khởi tạo danh sách sản phẩm MỘT LẦN DUY NHẤT
    private val allProducts = listOf(
        Product(
            id = "coffee_01",
            name = "Caffe Mocha",
            description = "Sự kết hợp hoàn hảo giữa Espresso, sữa nóng và sốt sô cô la.",
            price = 45000.0,
            category = "Coffee",
            imageRes = R.drawable.mocha_latte_recipe
        ),
        Product(
            id = "coffee_02",
            name = "Flat White",
            description = "Cà phê sữa kiểu Úc với lớp bọt sữa mỏng mịn.",
            price = 40000.0,
            category = "Coffee",
            imageRes = R.drawable.flat_white
        ),
        Product(
            id = "tea_01",
            name = "Trà Đào Cam Sả",
            description = "Thanh mát, giải nhiệt ngày hè.",
            price = 35000.0,
            category = "Tea",
            imageRes = R.drawable.peace_tea
        ),
        // Thêm tiếp các món Food, IceCream...
    )

    fun getAllProducts(): List<Product> {
        return allProducts // Trả về danh sách đã khởi tạo
    }

    fun getProductById(productId: String): Product? {
        // [FIX 2] Chỉ tìm kiếm trên danh sách đã khởi tạo (Hiệu suất cao hơn)
        return allProducts.find { it.id == productId }
    }
}