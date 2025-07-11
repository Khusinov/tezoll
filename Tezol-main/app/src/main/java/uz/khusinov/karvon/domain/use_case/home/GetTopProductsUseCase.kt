package uz.khusinov.karvon.domain.use_case.home

import kotlinx.coroutines.flow.Flow
import uz.khusinov.karvon.domain.model.Data
import uz.khusinov.karvon.domain.model.shop.Product
import uz.khusinov.karvon.domain.repository.HomeRepository
import uz.khusinov.karvon.utills.UiStateObject

class GetTopProductsUseCase(
    private val repository: HomeRepository
) {

    operator fun invoke(): Flow<UiStateObject<Data<Product>>> {
        return repository.getTopProducts()
    }
}