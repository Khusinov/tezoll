package uz.khusinov.karvon.presentation.login

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import uz.khusinov.karvon.R
import uz.khusinov.karvon.SharedPref
import uz.khusinov.karvon.databinding.FragmentNumberConfirmBinding
import uz.khusinov.karvon.domain.model.ConfirmRequest
import uz.khusinov.karvon.presentation.BaseFragment
import uz.khusinov.karvon.utills.launchAndRepeatWithViewLifecycle
import uz.khusinov.karvon.utills.UiStateObject
import uz.khusinov.karvon.utills.viewBinding
import javax.inject.Inject

@AndroidEntryPoint
class NumberConfirmFragment : BaseFragment(R.layout.fragment_number_confirm) {

    private val binding by viewBinding { FragmentNumberConfirmBinding.bind(it) }
    private val viewModel by viewModels<LoginViewModel>()

    private var sec = 60
    private var secondJob: Job? = null

    @Inject
    lateinit var sharedPref: SharedPref

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        secondJob = perSecond()
        setupUI()
        setupObserver()
    }


    private fun setupUI() {
        binding.apply {

            confirmButton.setOnClickListener {
                if (otpView.text.toString().length == 5) {
                    val confirmRequest =
                        ConfirmRequest(otpView.text.toString().toInt(), sharedPref.phone)
                    viewModel.confirm(confirmRequest)
                    secondJob?.cancel()
                }
            }

            otpView.setOtpCompletionListener { value ->
                if (value.length == 5 && sec > 0) {
                    val confirmRequest =
                        ConfirmRequest(otpView.text.toString().toInt(), sharedPref.phone)
                    viewModel.confirm(confirmRequest)
                    secondJob?.cancel()
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun perSecond(): Job {
        return MainScope().launch {
            while (isActive) {
                sec--
                val min = sec / 60
                val s = sec - min * 60
                if (s < 10)
                    binding.resentCode.text = "Kodni qayta yuborish \"$min:0$s"
                else
                    binding.resentCode.text = "Kodni qayta yuborish $min:$s"
                if (sec == 0) {
                    binding.resentCode.isFocusable = true
                    cancel()
                }
                delay(1000)
            }
        }
    }

    private fun setupObserver() {
        launchAndRepeatWithViewLifecycle {
            viewModel.confirmState.collect {
                when (it) {
                    is UiStateObject.LOADING -> {
                        showProgress()
                    }

                    is UiStateObject.SUCCESS -> {
                        hideProgress()
                        binding.otpView.clearFocus()
                        sharedPref.isEntered = true
                        sharedPref.access = it.data.access
                        sharedPref.refresh = it.data.refresh
                        findNavController().navigate(R.id.action_numberConfirmFragment_to_introduction1Fragment)
                    }

                    is UiStateObject.ERROR -> {
                        binding.otpView.setText("")
                        hideProgress()
                        showToast(it.message)
                    }

                    else -> {}
                }
            }
        }
    }
}