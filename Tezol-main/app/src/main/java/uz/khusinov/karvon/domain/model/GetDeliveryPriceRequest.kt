package uz.khusinov.karvon.domain.model

data class GetDeliveryPriceRequest(
    val lat: Double,
    val lon: Double,
    val shop_id: Int
)

data class DeliveryPrice(
    val standard_price:Int,
    val express_price:Int
)