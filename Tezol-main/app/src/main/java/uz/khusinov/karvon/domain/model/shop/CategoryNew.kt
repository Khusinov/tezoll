package uz.khusinov.karvon.domain.model.shop

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CategoryNew(
    val id: Int,
    val image: String,
    val name: String
):Parcelable