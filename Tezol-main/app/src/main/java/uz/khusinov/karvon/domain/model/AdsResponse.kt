package uz.khusinov.karvon.domain.model

data class AdsResponse(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<String>
)
