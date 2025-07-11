package uz.khusinov.karvon.presentation.login

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import uz.khusinov.karvon.R
import uz.khusinov.karvon.SharedPref
import uz.khusinov.karvon.databinding.FragmentLoginBinding
import uz.khusinov.karvon.domain.model.LoginRequest
import uz.khusinov.karvon.presentation.BaseFragment
import uz.khusinov.karvon.utills.launchAndRepeatWithViewLifecycle
import uz.khusinov.karvon.utills.UiStateObject
import uz.khusinov.karvon.utills.viewBinding
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : BaseFragment(R.layout.fragment_login) {

    private val binding by viewBinding { FragmentLoginBinding.bind(it) }
    private val viewModel by viewModels<LoginViewModel>()
    private var phoneNumber = ""

    @Inject
    lateinit var sharedPref: SharedPref

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        setupObserver()
    }

    private fun setupUI() {
        binding.apply {

            loginBtn.setOnClickListener {
                var phone = phone.text.toString()
                phoneNumber =
                    getString(R.string.phone_prefix).substring(1) + binding.phone.unMaskedText.toString()
                phone = phoneNumber.substring(5, phoneNumber.lastIndex + 1)

                sharedPref.phone = "+998$phone"

                Log.d("TAG", "setupUI: $phone ")
                if (phone.length == 9) {
                    val loginRequest = LoginRequest("+998" + phone)
                    viewModel.login(loginRequest)
                }
            }
        }
    }

    private fun setupObserver() {

        launchAndRepeatWithViewLifecycle {
            viewModel.loginState.collect {

                when (it) {
                    is UiStateObject.LOADING -> {
                        showProgress()
                    }

                    is UiStateObject.SUCCESS -> {
                        hideProgress()
                        binding.phone.clearFocus()
                        findNavController().navigate(R.id.action_loginFragment_to_numberConfirmFragment)
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


}