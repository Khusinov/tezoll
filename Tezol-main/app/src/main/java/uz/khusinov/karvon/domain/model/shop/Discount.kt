package uz.khusinov.karvon.domain.model.shop

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Discount(
    val discount: String? = null,
    val discount_in_percent: String? = null,
    val discount_in_price: Double? = null,
) : Parcelable