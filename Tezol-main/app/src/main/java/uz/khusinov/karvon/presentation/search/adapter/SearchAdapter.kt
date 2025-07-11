package uz.khusinov.karvon.presentation.search.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import uz.khusinov.karvon.databinding.ItemSearchBinding
import uz.khusinov.karvon.domain.model.shop.Product
import uz.khusinov.karvon.utills.price

class SearchAdapter(
    private val onItemClicked: (product: Product) -> Unit,
) :
    RecyclerView.Adapter<SearchAdapter.ViewHolder>() {
    private val dif = AsyncListDiffer(this, ITEM_DIFF)

    @SuppressLint("SuspiciousIndentation")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, parent.context)
    }

    override fun getItemCount(): Int = dif.currentList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.setContent(position)

    inner class ViewHolder(private val binding: ItemSearchBinding, val context: Context) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
        fun setContent(position: Int) = with(binding) {
            val product = dif.currentList[position]
            name.text = product.name
            price.text = price(product.product_type.first().price) + " so'm"

            item.setOnClickListener {
                onItemClicked(product)
            }

            if (product.image != null)
                Picasso.get().load(product.image)
                    .into(binding.image)
        }
    }

    fun submitList(orderProduct: List<Product>) {
        dif.submitList(orderProduct)
    }

    companion object {
        private val ITEM_DIFF = object : DiffUtil.ItemCallback<Product>() {
            override fun areItemsTheSame(
                oldItem: Product,
                newItem: Product
            ): Boolean =
                oldItem.name == newItem.name

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(
                oldItem: Product,
                newItem: Product
            ): Boolean =
                oldItem == newItem
        }
    }
}