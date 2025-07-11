package uz.khusinov.karvon.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import uz.khusinov.karvon.domain.model.shop.SelectedProduct

@Dao
interface ProductsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(products: SelectedProduct)

    @Query("SELECT * FROM selectedProduct")
    suspend fun getProductsOnBasket(): List<SelectedProduct>

    @Delete
    suspend fun removeProduct(products: SelectedProduct)

    @Update
    suspend fun updateProduct(products: SelectedProduct)

    @Query("DELETE FROM SelectedProduct")
    suspend fun deleteAllProducts()

    @Query("DELETE FROM SelectedProduct WHERE shop = :shopId")
    suspend fun deleteProductsByShop(shopId:Int)
}