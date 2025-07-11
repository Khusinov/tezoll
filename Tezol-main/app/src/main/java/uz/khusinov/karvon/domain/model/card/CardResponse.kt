package uz.khusinov.karvon.domain.model.card

import com.google.gson.annotations.SerializedName

data class CardResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("token") val token: String,
    @SerializedName("number") val number: String?,
    @SerializedName("is_verified") val verify: Boolean
)