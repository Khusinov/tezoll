package uz.khusinov.karvon.presentation.profile.my_cards1

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.Toast
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.textview.MaterialTextView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import uz.khusinov.karvon.R
import uz.khusinov.karvon.SharedPref
import uz.khusinov.karvon.databinding.FragmentVerifyCardBinding
import uz.khusinov.karvon.domain.model.card.CardVerify
import uz.khusinov.karvon.presentation.BaseFragment
import uz.khusinov.karvon.utills.UiStateObject
import uz.khusinov.karvon.utills.launchAndRepeatWithViewLifecycle
import uz.khusinov.karvon.utills.viewBinding
import javax.inject.Inject

@AndroidEntryPoint
class VerifyCardFragment : BaseFragment(R.layout.fragment_verify_card) {
    private val binding by viewBinding { FragmentVerifyCardBinding.bind(it) }
    private lateinit var job: Job
    private var timeOff = false
    private val viewModel by viewModels<CardViewModel>()
    private var cardId: String? = ""

    @Inject
    lateinit var sharedPref: SharedPref

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUserInterface()
        setupObserver()
        job = viewLifecycleOwner.lifecycleScope.launch {
            startTimer(binding.timer, 60)
        }
    }

    private fun setupObserver() {
        launchAndRepeatWithViewLifecycle {
            viewModel.confirmCardState.collect {
                when (it) {
                    is UiStateObject.LOADING -> {
                        showProgress()
                    }

                    is UiStateObject.SUCCESS -> {
                        hideProgress()
                        Toast.makeText(requireContext(), it.data.message, Toast.LENGTH_SHORT).show()
                        binding.code.clearFocus()
                        findNavController().navigateUp()
                        findNavController().navigateUp()
                    }

                    is UiStateObject.ERROR -> {
                        binding.code.setText("")
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
            cardId = arguments?.getString("cardId")

            back.setOnClickListener {
                findNavController().navigateUp()
            }

            verify.setOnClickListener {
                if (code.text.toString().length == 6 && !timeOff && cardId != null) {
                    viewModel.confirmCard(
                        CardVerify(card_id = cardId!!.toInt(), code.text.toString().toInt())
                    )
                } else {
                    errorText.isVisible = true
                    code.setHideLineWhenFilled(false)
                    code.setLineColor(ColorStateList.valueOf(resources.getColor(R.color.red)))
                }
            }

            timer.setOnClickListener {
                if (timeOff) {
                    job = viewLifecycleOwner.lifecycleScope.launch {
                        showProgress()
                        delay(1000)
                        hideProgress()
                        timeOff = false
                        errorText.isInvisible = true
                        code.text?.clear()
                        code.setHideLineWhenFilled(true)
                        code.setLineColor(ColorStateList.valueOf(resources.getColor(R.color.main_color)))
                        startTimer(timer, 60)
                    }
                }
            }
        }
    }

    @SuppressLint("DefaultLocale")
    private suspend fun startTimer(tvTimer: MaterialTextView, duration: Int) {
        tvTimer.setTextColor(resources.getColor(R.color.black_text_color))
        var remainingTime = duration
        while (remainingTime >= 0) {
            val minutes = remainingTime / 60
            val seconds = remainingTime % 60
            val formattedTime = String.format("%02d:%02d", minutes, seconds)

            val fullText = "Kodni $formattedTime daqiqa ichida kiriting"
            val spannable = SpannableString(fullText)

            val timeStartIndex = fullText.indexOf(formattedTime)
            val timeEndIndex = timeStartIndex + formattedTime.length
            spannable.setSpan(
                ForegroundColorSpan(resources.getColor(R.color.main_color)),
                timeStartIndex,
                timeEndIndex,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            tvTimer.text = spannable
            delay(1000)
            remainingTime--
        }

        tvTimer.text = "Kodni qayta yuborish"
        tvTimer.setTextColor(resources.getColor(R.color.main_color))
        timeOff = true
    }


    override fun onDestroyView() {
        super.onDestroyView()
        if (::job.isInitialized && job.isActive) {
            job.cancel()
        }
    }
}