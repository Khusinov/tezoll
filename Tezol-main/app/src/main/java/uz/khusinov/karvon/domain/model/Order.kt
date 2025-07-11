package uz.khusinov.karvon.domain.model

data class Order(
    val address_name: String,
    val comment: String?,
    val created_at: String,
    val discount: String?,
    val id: Int,
    val lang: Double,
    val lat: Double,
    val pay_type: String?,
    val status: Int,
    val supplier_id: Int?,
    val type: String?,
    val updated_at: String,
    val user_id: Int,
    val currentTime: Long = 0L
)