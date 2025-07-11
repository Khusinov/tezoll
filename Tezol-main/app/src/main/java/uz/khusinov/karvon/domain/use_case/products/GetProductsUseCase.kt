package uz.khusinov.karvon.domain.use_case.products

import kotlinx.coroutines.flow.Flow
import uz.khusinov.karvon.domain.model.shop.SelectedProduct
import uz.khusinov.karvon.domain.repository.ProductsRepository
import uz.khusinov.karvon.utills.UiStateList
import javax.inject.Inject

class GetProductsUseCase @Inject constructor(private val repository: ProductsRepository) {
    operator fun invoke(): Flow<UiStateList<SelectedProduct>> {
        return repository.getProductsOnBasket()
    }
}