package uz.khusinov.karvon.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import uz.khusinov.karvon.data.local.ProductsDao
import uz.khusinov.karvon.domain.model.shop.SelectedProduct
import uz.khusinov.karvon.domain.repository.ProductsRepository
import uz.khusinov.karvon.utills.UiStateList

class ProductsRepositoryImpl(private val productsDao: ProductsDao) : ProductsRepository {
    override suspend fun insertProduct(products: SelectedProduct) {
        productsDao.insertProduct(products)
    }

    override fun getProductsOnBasket(): Flow<UiStateList<SelectedProduct>> = flow {
        emit(UiStateList.LOADING)
        try {
            val response = productsDao.getProductsOnBasket()
            emit(UiStateList.SUCCESS(response))
        } catch (e: Exception) {
        }
    }

    override suspend fun removeProduct(products: SelectedProduct) {
        productsDao.removeProduct(products)
    }

    override suspend fun updateProduct(products: SelectedProduct) {
        productsDao.updateProduct(products)
    }

    override suspend fun deleteAllProducts() {
        productsDao.deleteAllProducts()
    }

    override suspend fun deleteProductsByShop(shopId: Int) {
        println("deleteProductsByShop called here ")
        productsDao.deleteProductsByShop(shopId)
    }

}