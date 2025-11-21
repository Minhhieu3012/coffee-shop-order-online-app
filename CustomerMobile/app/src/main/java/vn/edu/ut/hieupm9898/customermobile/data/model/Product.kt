package vn.edu.ut.hieupm9898.customermobile.data.model

import androidx.annotation.DrawableRes

data class Product(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val price: Double = 0.0,
    val imageUrl: String = "",    // Dùng khi có Server (chứa link ảnh https://...)
    val category: String = "",
    val isFavorite: Boolean = false,
    val isAvailable: Boolean = true,


    @DrawableRes val imageRes: Int? = null
)