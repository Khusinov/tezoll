package uz.khusinov.karvon.domain.model

data class OrderHistory(
    val address: String,
    val created_at: String,
    val deliveryType: String,
    val id: Int,
    val lat: Double,
    val lon: Double,
    val orderStatus: String,
    val paymentMethod: String,
    val updated_at: String,
    val amount: Int
)

