package uz.khusinov.karvon.presentation.basket.components

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import uz.khusinov.karvon.databinding.BasketItemBinding
import uz.khusinov.karvon.domain.model.shop.SelectedProduct
import uz.khusinov.karvon.utills.price

class BasketAdapter(
    private val onDeleteClicked: (position: Int) -> Unit,
    private val onPlusClicked: (products: SelectedProduct) -> Unit,
    private val onMinusClicked: (positions: SelectedProduct) -> Unit
) :
    RecyclerView.Adapter<BasketAdapter.ViewHolder>() {
    private val dif = AsyncListDiffer(this, ITEM_DIFF)

    @SuppressLint("SuspiciousIndentation")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = BasketItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, parent.context)
    }

    override fun getItemCount(): Int = dif.currentList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.setContent(position)

    inner class ViewHolder(private val binding: BasketItemBinding, val context: Context) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
        fun setContent(position: Int) = with(binding) {
            val product = dif.currentList[position]
            name.text = product.name
            description.text = product.description
            price.text = price(product.price.toInt()) + " so'm"
            count.text = product.selectedCount.toString()

            if (product.image != null)
                if (product.image.isNotEmpty()) {
                    Picasso.get().load(product.image).into(binding.basketItemImage)
                }


            plus.setOnClickListener {
                if (count.text.toString().toInt() < product.count.toString().toInt()) {
                    minus.isVisible = true
                    count.text = (count.text.toString().toInt() + 1).toString()
                    val products = SelectedProduct(
                        category = product.category,
                        description = product.description,
                        image = product.image,
                        is_active = product.is_active,
                        is_liked = product.is_liked,
                        name = product.name,
                        percent = product.percent,
                        shop = product.shop,
                        shopName = product.shopName,
                        stars_count = product.stars_count,
                        count = product.count,
                        discount = product.discount,
                        discountInPrice = product.discountInPrice,
                        discount_type = product.discount_type,
                        id = product.id,
                        measurement_unit = product.measurement_unit,
                        price = product.price,
                        product = product.product,
                        quantity_type = product.quantity_type,
                        selectedCount = count.text.toString().toInt()
                    )

                    onPlusClicked(products)
                } else {
                    minus.isVisible = true
                    count.text = (count.text.toString().toInt()).toString()
                    val products = SelectedProduct(
                        category = product.category,
                        description = product.description,
                        image = product.image,
                        is_active = product.is_active,
                        is_liked = product.is_liked,
                        name = product.name,
                        percent = product.percent,
                        shop = product.shop,
                        shopName = product.shopName,
                        stars_count = product.stars_count,
                        count = product.count,
                        discount = product.discount,
                        discountInPrice = product.discountInPrice,
                        discount_type = product.discount_type,
                        id = product.id,
                        measurement_unit = product.measurement_unit,
                        price = product.price,
                        product = product.product,
                        quantity_type = product.quantity_type,
                        selectedCount = count.text.toString().toInt() + 1
                    )
                    onPlusClicked(products)
                }
            }

            minus.setOnClickListener {
                count.text = (count.text.toString().toInt() - 1).toString()
                val products = SelectedProduct(
                    category = product.category,
                    description = product.description,
                    image = product.image,
                    is_active = product.is_active,
                    is_liked = product.is_liked,
                    name = product.name,
                    percent = product.percent,
                    shop = product.shop,
                    shopName = product.shopName,
                    stars_count = product.stars_count,
                    count = product.count,
                    discount = product.discount,
                    discountInPrice = product.discountInPrice,
                    discount_type = product.discount_type,
                    id = product.id,
                    measurement_unit = product.measurement_unit,
                    price = product.price,
                    product = product.product,
                    quantity_type = product.quantity_type,
                    selectedCount = count.text.toString().toInt()
                )
                onMinusClicked(products)
            }
        }
    }

    fun submitList(orderProduct: List<SelectedProduct>) {
        dif.submitList(orderProduct)
    }

    companion object {
        private val ITEM_DIFF = object : DiffUtil.ItemCallback<SelectedProduct>() {
            override fun areItemsTheSame(
                oldItem: SelectedProduct,
                newItem: SelectedProduct
            ): Boolean =
                oldItem.id == newItem.id

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(
                oldItem: SelectedProduct,
                newItem: SelectedProduct
            ): Boolean =
                oldItem == newItem
        }
    }
}