package uz.khusinov.karvon.domain.use_case.shops

data class ShopsUseCases(
    val getProductsUseCase: GetProductsUseCase,
    val getCategoryProductsUseCase: GetCategoryProductsUseCase
)