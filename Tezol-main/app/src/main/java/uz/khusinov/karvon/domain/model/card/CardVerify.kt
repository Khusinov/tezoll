package uz.khusinov.karvon.domain.model.card

data class CardVerify(
    val card_id: Int,
    val code: Int
)

data class VerifyCardResponse(
    val message:String
)