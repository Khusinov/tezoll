package uz.khusinov.karvon.domain.model.shop

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Product(
    val id:Int,
    val category: CategoryNew,
    val description: String,
    val image: String,
    val is_active: Boolean,
    val is_liked: Boolean,
    val name: String,
    val percent: Int,
    val product_type: List<ProductType>,
    val shop: ShopNew,
    val stars_count: Int
):Parcelable