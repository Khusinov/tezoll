package uz.khusinov.karvon.domain.model.base

data class BasePagination<T>(
    val count:Int?,
    val next:Int?,
    val previous:Int?,
    val results:List<T>,
)
