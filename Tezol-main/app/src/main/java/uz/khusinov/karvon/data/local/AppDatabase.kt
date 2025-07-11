package uz.khusinov.karvon.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import uz.khusinov.karvon.domain.model.shop.SelectedProduct
import uz.khusinov.marjonamarket.data.local.Convertors


@Database(
    entities = [SelectedProduct::class],
    version = 3,
    exportSchema = false
)

@TypeConverters(Convertors::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getProductsDao(): ProductsDao
}
