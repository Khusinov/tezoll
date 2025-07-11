package uz.khusinov.karvon.presentation.profile.my_cards1

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import uz.khusinov.karvon.R
import uz.khusinov.karvon.databinding.FragmentAddCardBinding
import uz.khusinov.karvon.domain.model.card.AddCardRequest
import uz.khusinov.karvon.presentation.BaseFragment
import uz.khusinov.karvon.utills.UiStateObject
import uz.khusinov.karvon.utills.launchAndRepeatWithViewLifecycle
import uz.khusinov.karvon.utills.viewBinding


class AddCardFragment : BaseFragment(R.layout.fragment_add_card) {
    private val binding by viewBinding { FragmentAddCardBinding.bind(it) }
    private val viewModel by activityViewModels<CardViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUserInterface()
        setupAddCardListeners()
    }

    private fun setupAddCardListeners() {
        launchAndRepeatWithViewLifecycle {
            viewModel.addCardState.collect {
                when (it) {
                    is UiStateObject.LOADING -> {
                        showProgress()
                    }

                    is UiStateObject.SUCCESS -> {
                        hideProgress()
                            if (it.data.card_id != null) {
                                val bundle = Bundle()
                                bundle.putString("cardId", it.data.card_id.toString())
                                bundle.putString("phone", "")
                                findNavController().navigate(
                                    R.id.action_addCardFragment_to_verifyCardFragment,
                                    bundle
                                )
                            } else showToast("SMS xabarnoma ulanmagan yoki telefon raqam aktiv emas!")
                    }

                    is UiStateObject.ERROR -> {
                        hideProgress()
                        showToast(it.message)
                    }

                    else -> {}
                }
            }
        }
    }

    private fun setupUserInterface() {
        binding.apply {


            next.setOnClickListener {

                if (inputCardNumber.unMaskedText?.length == 16 && inputExpiredDate.unMaskedText?.length == 4)
                    viewModel.addCard(
                        AddCardRequest(
                            expiry_date = inputExpiredDate.unMaskedText!!,
                            number = inputCardNumber.unMaskedText!!
                        )
                    )
                else showToast("Kart raqam yoki amal qilish muddati noto'g'ri kiritilgan")
            }

        }
    }


}