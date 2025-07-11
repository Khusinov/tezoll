package uz.khusinov.karvon.presentation.profile.my_cards1.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import uz.khusinov.karvon.databinding.ItemCardBinding
import uz.khusinov.karvon.domain.model.card.CardResponse

class CardsAdapter(
    private val onItemClicked: (card: CardResponse) -> Unit,
    private val onMainCardClicked: (card: CardResponse) -> Unit
) : RecyclerView.Adapter<CardsAdapter.ViewHolder>() {

    private val differ = AsyncListDiffer(this, ITEM_DIFF)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = differ.currentList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind()

    inner class ViewHolder(private val binding: ItemCardBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind() = with(binding) {
            val card = differ.currentList[adapterPosition]
            cardNumber.text = card.number
            binding.mainCardIndicator.isVisible = card.verify

//            mainCardBtn.setOnClickListener {
//                if (!card.isMain) {
//                    card.isMain = true
//                    notifyItemChanged(adapterPosition)
//                    onMainCardClicked(card)
//                }
//            }

            root.setOnClickListener {
                onItemClicked(card)
            }
        }
    }

    fun submitList(cards: List<CardResponse>) {
        differ.submitList(cards)
    }

    companion object {
        private val ITEM_DIFF = object : DiffUtil.ItemCallback<CardResponse>() {
            override fun areItemsTheSame(oldItem: CardResponse, newItem: CardResponse): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: CardResponse, newItem: CardResponse): Boolean =
                oldItem == newItem
        }
    }
}