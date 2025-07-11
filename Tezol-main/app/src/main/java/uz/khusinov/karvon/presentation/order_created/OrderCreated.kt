package uz.khusinov.karvon.presentation.order_created

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import uz.khusinov.karvon.R
import uz.khusinov.karvon.databinding.FragmentOrderCreatedBinding
import uz.khusinov.karvon.presentation.BaseFragment
import uz.khusinov.karvon.utills.viewBinding

class OrderCreated : BaseFragment(R.layout.fragment_order_created) {
    private val binding by viewBinding { FragmentOrderCreatedBinding.bind(it) }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
    }

    private fun setupUI() {
        binding.apply {
            val deliveryTime = 30

            val message = getString(R.string.order_message, deliveryTime)
            orderConfirmationMessage.text = message

            back.setOnClickListener {
                findNavController().navigate(R.id.action_orderCreated_to_mainFragment)
            }
        }
    }
}