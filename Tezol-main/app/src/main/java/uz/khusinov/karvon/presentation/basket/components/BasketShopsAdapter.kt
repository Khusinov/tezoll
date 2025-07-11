package uz.khusinov.karvon.presentation.basket.components

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import uz.khusinov.karvon.R
import uz.khusinov.karvon.databinding.ItemShopsBasketBinding
import uz.khusinov.karvon.domain.model.shop.BasketShop
import uz.khusinov.karvon.utills.price

class BasketShopsAdapter(
    private val onClicked: (basketShop: BasketShop, isSelected: Boolean) -> Unit,
) : RecyclerView.Adapter<BasketShopsAdapter.ViewHolder>() {

    private val dif = AsyncListDiffer(this, ITEM_DIFF)
    private var selectedShopId: Int? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemShopsBasketBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, parent.context)
    }

    override fun getItemCount(): Int = dif.currentList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.setContent(position)

    inner class ViewHolder(private val binding: ItemShopsBasketBinding, val context: Context) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun setContent(position: Int) = with(binding) {
            val shop = dif.currentList[position]
            name.text = shop.name
            price.text = price(shop.price) + " so'm"

            // Set border color based on selection
            cv.strokeColor = ContextCompat.getColor(
                context,
                if (shop.id == selectedShopId) R.color.only_black else R.color.white
            )

            item.setOnClickListener {
                onClicked(shop, true)
            }
        }
    }

    fun submitList(orderProduct: List<BasketShop>, selectedShopId: Int? = null) {
        this.selectedShopId = selectedShopId
        dif.submitList(orderProduct) {
            notifyDataSetChanged()
        }
    }

    companion object {
        private val ITEM_DIFF = object : DiffUtil.ItemCallback<BasketShop>() {
            override fun areItemsTheSame(oldItem: BasketShop, newItem: BasketShop): Boolean =
                oldItem.id == newItem.id

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: BasketShop, newItem: BasketShop): Boolean =
                oldItem == newItem
        }
    }
}