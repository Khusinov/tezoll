package uz.khusinov.karvon.domain.model.shop

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("selectedProduct")
data class SelectedProduct(
    val category: Int,
    val description: String,
    val image: String,
    val is_active: Boolean,
    val is_liked: Boolean,
    val name: String,
    val percent: Int,
    val shop: Int,
    val shopName:String,
    val stars_count: Int,
    val count: Int,
    val discount: String?,
    val discountInPrice: Double?,
    val discount_type: String,
    @PrimaryKey
    val id: Int,
    val measurement_unit: String,
    val price: Int,
    val product: Int,
    val quantity_type: String,
    val selectedCount: Int
)
