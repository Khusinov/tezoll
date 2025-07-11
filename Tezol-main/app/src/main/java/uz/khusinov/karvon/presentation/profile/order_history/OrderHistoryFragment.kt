package uz.khusinov.karvon.presentation.profile.order_history

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import uz.khusinov.karvon.R
import uz.khusinov.karvon.databinding.FragmentOrderHistoryBinding
import uz.khusinov.karvon.domain.model.OrderHistory
import uz.khusinov.karvon.presentation.BaseFragment
import uz.khusinov.karvon.presentation.home.OrdersViewModel
import uz.khusinov.karvon.utills.launchAndRepeatWithViewLifecycle
import uz.khusinov.karvon.utills.UiStateList
import uz.khusinov.karvon.utills.UiStateObject
import uz.khusinov.karvon.utills.viewBinding

@AndroidEntryPoint
class OrderHistoryFragment : BaseFragment(R.layout.fragment_order_history) {
    private val binding by viewBinding { FragmentOrderHistoryBinding.bind(it) }
    private val ordersViewModel by viewModels<OrdersViewModel>()
    private val adapter by lazy { OrdersHistoryAdapter(::onItemClicked) }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ordersViewModel.getOrdersHistory()
        setupUI()
        setupOrderHistoryObserver()
    }

    private fun setupUI() = with(binding) {
        toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        historyRecycler.adapter = adapter
    }

    private fun setupOrderHistoryObserver() {
        launchAndRepeatWithViewLifecycle {
            ordersViewModel.ordersHistoryState.collect {
                when (it) {
                    is UiStateObject.LOADING -> {
                        showProgress()
                    }

                    is UiStateObject.SUCCESS -> {
                        hideProgress()
                        Log.d("TAG", "setupOrderHistoryObserver: ${it.data} ")
                        if (it.data.results.isNotEmpty())
                            adapter.submitList(it.data.results)
                    }

                    is UiStateObject.ERROR -> {
                        hideProgress()
                        showToast(it.message)
                    }

                    else -> {
                    }
                }
            }
        }
    }

    private fun onItemClicked(orderHistory: OrderHistory) {

    }

}