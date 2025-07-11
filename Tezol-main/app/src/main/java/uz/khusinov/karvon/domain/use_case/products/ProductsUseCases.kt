package uz.khusinov.karvon.domain.use_case.products

data class ProductsUseCases(
    val getProductsUseCase: GetProductsUseCase,
    val insertProductUseCase: InsertProductUseCase,
    val removeProductUseCase: RemoveProductUseCase,
    val updateProductUseCase: UpdateProductUseCase,
    val deleteAllProductUseCase: DeleteAllProductUseCase,
    val deleteProductByShopUseCase: DeleteProductByShopUseCase
)