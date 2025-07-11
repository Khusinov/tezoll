package uz.khusinov.karvon.domain.repository

import kotlinx.coroutines.flow.Flow
import uz.khusinov.karvon.domain.model.base.BasePagination
import uz.khusinov.karvon.domain.model.shop.Product
 import uz.khusinov.karvon.utills.UiStateObject

interface ShopsRepository {
    fun getShopsProducts(shopId: Int): Flow<UiStateObject<BasePagination<Product>>>

    fun getCategoryProducts(categoryId: Int): Flow<UiStateObject<BasePagination<Product>>>
}