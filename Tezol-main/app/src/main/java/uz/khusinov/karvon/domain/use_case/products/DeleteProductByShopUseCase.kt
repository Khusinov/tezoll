package uz.khusinov.karvon.domain.use_case.products


import uz.khusinov.karvon.domain.repository.ProductsRepository
import javax.inject.Inject

class DeleteProductByShopUseCase @Inject constructor(private val repository: ProductsRepository) {
    suspend operator fun invoke(shopId:Int) {
        repository.deleteProductsByShop(shopId)
    }
}