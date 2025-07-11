package uz.khusinov.karvon.domain.repository

import android.util.Log
import kotlinx.coroutines.flow.Flow
import uz.khusinov.karvon.domain.model.shop.SelectedProduct
import uz.khusinov.karvon.utills.UiStateList

interface ProductsRepository {
    suspend fun insertProduct(products: SelectedProduct)

    fun getProductsOnBasket(): Flow<UiStateList<SelectedProduct>>

    suspend fun removeProduct(products: SelectedProduct)

    suspend fun updateProduct(products: SelectedProduct)

    suspend fun deleteAllProducts() {
        Log.d("TAG", "deleteAllProducts: called ")
    }

    suspend fun deleteProductsByShop(shopId:Int)
}