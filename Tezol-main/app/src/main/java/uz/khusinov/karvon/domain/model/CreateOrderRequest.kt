package uz.khusinov.karvon.domain.model

data class CreateOrderRequest(
    val address: String,
    val deliveryType: String,
    val lat: Double,
    val lon: Double,
    val order_item: List<OrderItem>,
    val paymentMethod: String,
    val shop: Int
)