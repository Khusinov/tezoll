package uz.khusinov.karvon.domain.repository

import kotlinx.coroutines.flow.Flow
import uz.khusinov.karvon.domain.model.AdsResponse
import uz.khusinov.karvon.domain.model.Data
import uz.khusinov.karvon.domain.model.shop.Product
import uz.khusinov.karvon.utills.UiStateObject

interface HomeRepository {
    fun getAds(): Flow<UiStateObject<AdsResponse>>

   // fun getNewProducts(): Flow<UiStateObject<BasePagination<Product>>>

    fun getTopProducts(): Flow<UiStateObject<Data<Product>>>

    fun getMostSoldProducts(): Flow<UiStateObject<Data<Product>>>

    fun searchProducts(query: String): Flow<UiStateObject<Data<Product>>>
}