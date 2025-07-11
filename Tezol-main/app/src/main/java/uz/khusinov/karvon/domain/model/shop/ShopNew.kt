package uz.khusinov.karvon.domain.model.shop

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ShopNew(
    val address: String,
    val branch: Int,
    val closed_at: String,
    val delivery_price: Int,
    val delivery_time: Int,
    val description: String,
    val id: Int,
    val image: String,
    val is_active: Boolean,
    val lat: Double,
    val lon: Double,
    val name: String,
    val opened_at: String,
    val owner: Int,
    val phone_number: String,
    val telegram_chat_id: String
): Parcelable