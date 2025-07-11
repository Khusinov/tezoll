package uz.khusinov.karvon.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import uz.khusinov.karvon.data.remote.ApiService
import uz.khusinov.karvon.domain.model.shop.CategoryRespons
import uz.khusinov.karvon.domain.repository.CategoryRepository
import uz.khusinov.karvon.utills.userMessage
import uz.khusinov.karvon.utills.UiStateObject

class CategoryRepositoryImpl(
    private val apiService: ApiService
):CategoryRepository {
    override suspend fun getCategory(id: Int): Flow<UiStateObject<CategoryRespons>> = flow {
        emit(UiStateObject.LOADING)
        try {
            val response = apiService.getCategory(id)
            if (response.success) {
                emit(UiStateObject.SUCCESS(response.data))
            } else {
                emit(UiStateObject.ERROR(response.error.message))
            }
        } catch (e: Exception) {
            emit(UiStateObject.ERROR(e.userMessage()))
        }
    }
}