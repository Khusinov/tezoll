package uz.khusinov.karvon.domain.model.shop

data class Categorys(
    val count:Int?,
    val next:Int?,
    val previous:Int?,
    val results:List<CategoryRespons>,
)
