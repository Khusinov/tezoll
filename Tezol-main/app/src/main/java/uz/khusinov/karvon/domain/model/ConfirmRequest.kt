package uz.khusinov.karvon.domain.model

data class ConfirmRequest(
    val code: Int,
    val phone_number: String
)