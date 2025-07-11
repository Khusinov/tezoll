package uz.khusinov.karvon.domain.repository

import kotlinx.coroutines.flow.Flow
import uz.khusinov.karvon.domain.model.shop.CategoryRespons
import uz.khusinov.karvon.utills.UiStateObject


interface CategoryRepository {

    suspend fun getCategory(id:Int): Flow<UiStateObject<CategoryRespons>>
}