package uz.khusinov.karvon.domain.model

data class CreateOrderResponse(
    val address: String,
    val created_at: String,
    val deliveryType: String,
    val id: Int,
    val lat: Double,
    val lon: Double,
    val orderStatus: String,
    val order_item: List<OrderItemX>,
    val paymentMethod: String,
    val updated_at: String,
    val user: Int
)