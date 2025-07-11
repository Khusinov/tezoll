package uz.khusinov.karvon.domain.model.shop

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProductType(
    val count: Int,
    val discount: Discount? = null,
    val discount_type: String?, // in_persent , in_price
    val id: Int,
    val measurement_unit: String,
    val price: Int,
    val product: Int,
    val quantity_type: String
):Parcelable