package uz.khusinov.karvon.domain.model.shop

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Shop(
    val closed_at: String,
    val id: Int,
    val image: String,
    val lat: Double,
    val lon: Double,
    val name: String,
    val opened_at: String,
    val phone_number: String
) : Parcelable