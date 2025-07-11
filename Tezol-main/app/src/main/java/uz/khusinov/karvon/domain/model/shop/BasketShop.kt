package uz.khusinov.karvon.domain.model.shop

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class BasketShop(
    val id: Int,
    val name: String,
    val price: Int
) : Parcelable