package uz.khusinov.karvon.domain.model

data class LoginResponse(
    val name: String,
    val phone: Int,
    val role: Int,
    val token: String,
    val user_id: Int
)