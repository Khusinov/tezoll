package uz.khusinov.karvon.presentation.catalog.components

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.squareup.picasso.Picasso
import uz.khusinov.karvon.databinding.CategoryItemBinding
import uz.khusinov.karvon.domain.model.shop.CategoryRespons
import uz.khusinov.karvon.domain.model.shop.Categorys

class CatalogsAdapter(
    val onItemClick: (category: CategoryRespons) -> Unit,
) : PagingDataAdapter<CategoryRespons, CatalogsAdapter.ViewHolder>(ITEM_DIFF) {


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setContent(getItem(position)!!)
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            CategoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    inner class ViewHolder(private val binding: CategoryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun setContent(category: CategoryRespons) = with(binding) {
            categoryItem.setOnClickListener { onItemClick(category) }
            categoryImage.setOnClickListener { onItemClick(category) }
            categoryName.text = category.name
            Glide.with(binding.root)
                .load(category.image)
                .apply(RequestOptions().override(1000, 1000))
                .into(categoryImage)

        }
    }

    companion object {
        private val ITEM_DIFF = object : DiffUtil.ItemCallback<CategoryRespons>() {
            override fun areItemsTheSame(
                oldItem: CategoryRespons,
                newItem: CategoryRespons
            ): Boolean =
                oldItem.name == newItem.name

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(
                oldItem: CategoryRespons,
                newItem: CategoryRespons
            ): Boolean =
                oldItem == newItem
        }
    }
}