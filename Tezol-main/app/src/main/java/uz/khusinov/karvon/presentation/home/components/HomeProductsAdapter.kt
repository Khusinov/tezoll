package uz.khusinov.karvon.presentation.home.components

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import uz.khusinov.karvon.databinding.ProductItemBinding
import uz.khusinov.karvon.domain.model.shop.Product
import uz.khusinov.karvon.utills.calculateDiscount

class HomeProductsAdapter(
    private val onItemClicked: (product: Product) -> Unit
) : PagingDataAdapter<Product, HomeProductsAdapter.ViewHolder>(ITEM_DIFF) {


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setContent(getItem(position)!!)
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ProductItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    inner class ViewHolder(private val binding: ProductItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun setContent(product: Product) = with(binding) {
            root.setOnClickListener { onItemClicked(product) }
            Picasso.get().load(product.image).into(image)
            name.text = product.name
            description.text = product.description
            oldPrice.text = product.product_type.first().price.toString()
            newPrice.text = calculateDiscount(price = product.product_type.first().price, discount = product.product_type.first().discount, discountType = product.product_type.first().discount_type ).toString()
        }
    }

    companion object {
        private val ITEM_DIFF = object : DiffUtil.ItemCallback<Product>() {
            override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean =
                oldItem.name == newItem.name

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean =
                oldItem == newItem
        }
    }
}