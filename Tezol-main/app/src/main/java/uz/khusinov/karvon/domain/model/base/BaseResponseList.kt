package uz.khusinov.karvon.domain.model.base

data class BaseResponseList<T>(
    val data: List<T>,
    val message: String,
    val success: Boolean,
    val error: Error
)