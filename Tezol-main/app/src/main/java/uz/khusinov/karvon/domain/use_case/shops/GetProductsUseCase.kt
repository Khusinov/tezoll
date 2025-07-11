package uz.khusinov.karvon.domain.use_case.shops

import kotlinx.coroutines.flow.Flow
import uz.khusinov.karvon.domain.model.base.BasePagination
import uz.khusinov.karvon.domain.model.shop.Product
import uz.khusinov.karvon.domain.repository.ShopsRepository
import uz.khusinov.karvon.utills.UiStateObject

class GetProductsUseCase(
    private val repository: ShopsRepository
) {
    operator fun invoke(shopId: Int): Flow<UiStateObject<BasePagination<Product>>> {
        return repository.getShopsProducts(shopId)
    }
}