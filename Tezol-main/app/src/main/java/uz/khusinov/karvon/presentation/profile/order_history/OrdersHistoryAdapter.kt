package uz.khusinov.karvon.presentation.profile.order_history

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import uz.khusinov.karvon.R
import uz.khusinov.karvon.databinding.ItemOrderHistoryBinding
import uz.khusinov.karvon.domain.model.OrderHistory
import uz.khusinov.karvon.utills.price

class OrdersHistoryAdapter(
    private val onItemClicked: (product: OrderHistory) -> Unit
) : RecyclerView.Adapter<OrdersHistoryAdapter.ViewHolder>() {
    private val dif = AsyncListDiffer(this, ITEM_DIFF)

    @SuppressLint("SuspiciousIndentation")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemOrderHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, parent.context)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.setContent(position)

    override fun getItemCount(): Int = dif.currentList.size

    inner class ViewHolder(private val binding: ItemOrderHistoryBinding, val context: Context) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
        fun setContent(position: Int) = with(binding) {
            val product = dif.currentList[position]
            storeName.text = product.address
            status.text = product.orderStatus
            time.text = product.created_at.substring(0, 10)
            price.text = price(product.amount) + " so'm"


            when (product.orderStatus) {
                "pending" -> {
                    status.text = "Jarayonda"
                    status.setTextColor(binding.root.context.getColor(R.color.main_color))
                }

                "delivery" -> {
                    status.text = "Yetkazilmoqda"
                    status.setTextColor(binding.root.context.getColor(R.color.main_color))
                }

                "completed" -> {
                    status.text = "Tugallangan"
                    status.setTextColor(binding.root.context.getColor(R.color.status_finished))
                }

                else -> "Kutilmoqda2"
            }

            root.setOnClickListener {
                onItemClicked(product)
            }
        }
    }

    fun submitList(shop: List<OrderHistory>) {
        dif.submitList(shop)
    }

    companion object {
        private val ITEM_DIFF = object : DiffUtil.ItemCallback<OrderHistory>() {
            override fun areItemsTheSame(oldItem: OrderHistory, newItem: OrderHistory): Boolean =
                oldItem.id == newItem.id

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: OrderHistory, newItem: OrderHistory): Boolean =
                oldItem == newItem
        }
    }
}