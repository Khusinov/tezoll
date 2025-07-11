package uz.khusinov.karvon.domain.model

data class Data<T>(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<T>
)
