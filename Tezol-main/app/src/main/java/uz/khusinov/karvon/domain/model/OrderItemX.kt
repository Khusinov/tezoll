package uz.khusinov.karvon.domain.model

data class OrderItemX(
    val created_at: String,
    val id: Int,
    val order: Int,
    val product: Int,
    val product_type: Int,
    val quantity: Int,
    val updated_at: String
)