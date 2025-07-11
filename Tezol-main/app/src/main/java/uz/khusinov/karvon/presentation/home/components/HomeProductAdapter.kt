package uz.khusinov.karvon.presentation.home.components

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import uz.khusinov.karvon.databinding.ProductItemBinding
import uz.khusinov.karvon.domain.model.shop.Product
import uz.khusinov.karvon.utills.calculateDiscount

class HomeProductAdapter(
    private val onItemClicked: (product: Product) -> Unit
) : RecyclerView.Adapter<HomeProductAdapter.ViewHolder>() {
    private val dif = AsyncListDiffer(this, ITEM_DIFF)

    @SuppressLint("SuspiciousIndentation")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ProductItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, parent.context)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.setContent(position)

    override fun getItemCount(): Int = dif.currentList.size

    inner class ViewHolder(private val binding: ProductItemBinding, val context: Context) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
        fun setContent(position: Int) = with(binding) {
            val product = dif.currentList[position]
            Log.d("TAG", "setContent: image url ${product.image}")
            Picasso.get().load(product.image).into(image)
            name.text = product.name
            description.text = product.description
            oldPrice.text = product.product_type.first().price.toString()
            newPrice.text = calculateDiscount(price = product.product_type.first().price, discount = product.product_type.first().discount, discountType = product.product_type.first().discount_type ).toString()

            root.setOnClickListener {
                onItemClicked(product)
            }
        }
    }

    fun submitList(shop: List<Product>) {
        dif.submitList(shop)
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