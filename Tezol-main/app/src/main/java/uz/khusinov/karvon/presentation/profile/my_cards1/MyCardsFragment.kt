package uz.khusinov.karvon.presentation.profile.my_cards1

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import uz.khusinov.karvon.R
import uz.khusinov.karvon.databinding.FragmentMyCardsBinding
import uz.khusinov.karvon.domain.model.card.CardResponse
import uz.khusinov.karvon.presentation.BaseFragment
import uz.khusinov.karvon.presentation.profile.my_cards1.adapter.CardsAdapter
import uz.khusinov.karvon.utills.UiStateList
import uz.khusinov.karvon.utills.UiStateObject
import uz.khusinov.karvon.utills.launchAndRepeatWithViewLifecycle
import uz.khusinov.karvon.utills.viewBinding

@AndroidEntryPoint
class MyCardsFragment : BaseFragment(R.layout.fragment_my_cards) {
    private val binding by viewBinding { FragmentMyCardsBinding.bind(it) }
    private val viewModel by viewModels<CardViewModel>()
    private val cardsAdapter by lazy { CardsAdapter(::onItemClicked, ::onMainCardClicked) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUserInterface()
        setupObservers()
        viewModel.getCards()
    }

    private fun setupObservers() {
        launchAndRepeatWithViewLifecycle {
            viewModel.getCardState.collect {
                when (it) {
                    is UiStateList.LOADING -> {
                        showProgress()
                    }

                    is UiStateList.SUCCESS -> {
                        hideProgress()
                        if (it.data.isNotEmpty())
                        cardsAdapter.submitList(it.data)
                    }

                    is UiStateList.ERROR -> {
                        hideProgress()
                        showToast(it.message)
                    }

                    else -> {}
                }
            }
        }
    }

    private fun setupUserInterface() = with(binding) {

        binding.cardsRv.adapter = cardsAdapter

        back.setOnClickListener {
            findNavController().navigateUp()
        }


        addCardBtn.setOnClickListener{
            findNavController().navigate(R.id.action_myCardsFragment_to_addCardFragment)
        }

    }

    private fun onMainCardClicked(cardResponse: CardResponse) {

    }

    private fun onItemClicked(cardResponse: CardResponse) {

    }
}