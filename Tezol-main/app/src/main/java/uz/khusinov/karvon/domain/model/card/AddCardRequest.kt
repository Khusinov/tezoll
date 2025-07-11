package uz.khusinov.karvon.domain.model.card

data class AddCardRequest(
    val expiry_date: String,
    val number: String
)

data class AddCardResponse(
    val card_id: Int?
)