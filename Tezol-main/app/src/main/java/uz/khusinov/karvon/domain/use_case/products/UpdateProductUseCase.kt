package uz.khusinov.karvon.domain.use_case.products

import uz.khusinov.karvon.domain.model.shop.SelectedProduct
import uz.khusinov.karvon.domain.repository.ProductsRepository
import javax.inject.Inject

class UpdateProductUseCase @Inject constructor(private val repository: ProductsRepository) {
    suspend operator fun invoke(address: SelectedProduct) {
        repository.updateProduct(address)
    }
}