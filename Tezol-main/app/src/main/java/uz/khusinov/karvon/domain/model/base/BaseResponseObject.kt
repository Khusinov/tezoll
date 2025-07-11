package uz.khusinov.karvon.domain.model.base


data class BaseResponseObject<T>(
    val data: T,
    val message: String,
    val success: Boolean,
    val error: Error
)