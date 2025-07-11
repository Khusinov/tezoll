package uz.khusinov.karvon.domain.model.card

import com.google.gson.annotations.SerializedName

data class HasSms(
    @SerializedName("phone") val phone: String,
    @SerializedName("sent") val sent: Boolean,
    @SerializedName("wait") val wait: Int
)