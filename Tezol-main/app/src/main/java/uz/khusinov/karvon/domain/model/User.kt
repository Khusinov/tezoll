package uz.khusinov.karvon.domain.model

data class User(
    val id: Int,
    val full_name: String,
    val phone_number: String,
    val user_type: String
)
