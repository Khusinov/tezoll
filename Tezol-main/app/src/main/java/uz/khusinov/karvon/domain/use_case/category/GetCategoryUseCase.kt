package uz.khusinov.karvon.domain.use_case.category

import kotlinx.coroutines.flow.Flow
import uz.khusinov.karvon.domain.model.shop.CategoryRespons
import uz.khusinov.karvon.domain.repository.CategoryRepository
import uz.khusinov.karvon.utills.UiStateObject

class GetCategoryUseCase(
    private val repository:CategoryRepository
) {
    suspend operator fun invoke(id:Int): Flow<UiStateObject<CategoryRespons>> {
        return repository.getCategory(id)
    }
}