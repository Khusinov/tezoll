package uz.khusinov.karvon.presentation.shops.components

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import uz.khusinov.karvon.databinding.ShopItemBinding
import uz.khusinov.karvon.domain.model.shop.Shop

class ShopsAdapter(
    val onItemClick: (bread: Shop) -> Unit,
) : PagingDataAdapter<Shop, ShopsAdapter.ViewHolder>(ITEM_DIFF) {


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setContent(getItem(position)!!)
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ShopItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    inner class ViewHolder(private val binding: ShopItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun setContent(restaurant: Shop) = with(binding) {
            shopItem.setOnClickListener { onItemClick(restaurant) }


            if (restaurant.image != null) {
                try {
                    Glide.with(binding.root)
                        .load(restaurant.image)
                        .into(shopImage)
                } catch (e: Exception) {
                    Log.d("TAG", "setContent: ${e.message} ")
                }
            }
        }
    }

    companion object {
        private val ITEM_DIFF = object : DiffUtil.ItemCallback<Shop>() {
            override fun areItemsTheSame(oldItem: Shop, newItem: Shop): Boolean =
                oldItem.name == newItem.name

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: Shop, newItem: Shop): Boolean =
                oldItem == newItem
        }
    }
}