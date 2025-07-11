package uz.khusinov.karvon.presentation.shops.selectedShop

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import uz.khusinov.karvon.databinding.ProductItemBinding
import uz.khusinov.karvon.domain.model.shop.Product
import uz.khusinov.karvon.utills.calculateDiscount

class SelectedProductsAdapter(private val onItemClicked: (product: Product) -> Unit) :
    RecyclerView.Adapter<SelectedProductsAdapter.ViewHolder>() {
    private val dif = AsyncListDiffer(this, ITEM_DIFF)

    @SuppressLint("SuspiciousIndentation")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ProductItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, parent.context)
    }

    override fun getItemCount(): Int = dif.currentList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.setContent(position)

    inner class ViewHolder(private val binding: ProductItemBinding, val context: Context) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
        fun setContent(position: Int) = with(binding) {
            val shop = dif.currentList[position]
//            if (shop.img != null){
//                Log.d("TAG", "setContent: image is not null ")
//                Picasso.get().load("${Constants.IMAGE_URL}${shop.img}")
//                    .into(binding.catalogImage)
//            } else  Log.d("TAG", "setContent: image is null ")

            if (shop.image != null)
                Picasso.get().load(shop.image).into(binding.image)

            name.text = shop.name
            description.text = shop.description
            oldPrice.text = shop.product_type[0].price.toString()
            newPrice.text = shop.product_type[0].price.toString()
            newPrice.text = calculateDiscount(price = shop.product_type.first().price, discount = shop.product_type.first().discount, discountType = shop.product_type.first().discount_type ).toString()

            item.setOnClickListener {
                onItemClicked(shop)
            }
        }
    }

    fun submitList(shop: List<Product>) {
        dif.submitList(shop)
    }

    companion object {
        private val ITEM_DIFF = object : DiffUtil.ItemCallback<Product>() {
            override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean =
                oldItem.shop == newItem.shop

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean =
                oldItem == newItem
        }
    }
}