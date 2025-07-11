package uz.khusinov.karvon.domain.model

data class ConfirmResponse(
    val access: String,
    val refresh: String,
)