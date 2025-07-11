package uz.khusinov.karvon.presentation.profile

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import dagger.hilt.android.AndroidEntryPoint
import uz.khusinov.karvon.R
import uz.khusinov.karvon.SharedPref
import uz.khusinov.karvon.databinding.FragmentProfileBinding
import uz.khusinov.karvon.domain.model.Refresh
import uz.khusinov.karvon.presentation.BaseFragment
import uz.khusinov.karvon.presentation.login.LoginViewModel
import uz.khusinov.karvon.utills.launchAndRepeatWithViewLifecycle
import uz.khusinov.karvon.utills.UiStateObject
import uz.khusinov.karvon.utills.viewBinding
import javax.inject.Inject


@AndroidEntryPoint
class ProfileFragment : BaseFragment(R.layout.fragment_profile) {
    private val binding by viewBinding { FragmentProfileBinding.bind(it) }
    private val profileViewModel by viewModels<ProfileViewModel>()
    private val authViewModel by viewModels<LoginViewModel>()

    @Inject
    lateinit var sharedPref: SharedPref

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        profileViewModel.getMe()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        setupObserver()
        setupLogoutObserver()
    }

    private fun setupUI() = with(binding) {
        cards.setOnClickListener {
            requireActivity().findNavController(R.id.main_container)
                .navigate(R.id.action_mainFragment_to_myCardsFragment)
        }

        logoutBtn.setOnClickListener {
            val refresh = Refresh(sharedPref.refresh)
            authViewModel.logout(refresh)
        }

        orders.setOnClickListener {
            requireActivity().findNavController(R.id.main_container)
                .navigate(R.id.action_mainFragment_to_orderHistoryFragment)
        }

        help.setOnClickListener {
            requireActivity().findNavController(R.id.main_container)
                .navigate(R.id.action_mainFragment_to_supportFragment)
        }

        aboutUS.setOnClickListener {

        }

        language.setOnClickListener {
            requireActivity().findNavController(R.id.main_container)
                .navigate(R.id.action_mainFragment_to_languageFragment)
        }

    }

    private fun setupObserver() {
        launchAndRepeatWithViewLifecycle {
            profileViewModel.getMeState.collect {
                when (it) {
                    is UiStateObject.LOADING -> showProgress()

                    is UiStateObject.SUCCESS -> {
                        hideProgress()
//                        Picasso.get().load(it.data.image).into(binding.image)
                        binding.username.text = it.data.full_name
                        binding.phone.text = it.data.phone_number
                    }

                    is UiStateObject.ERROR -> {
                        hideProgress()
                        showMessage(it.message)
                    }

                    else -> Unit
                }
            }
        }
    }

    private fun setupLogoutObserver() {
        launchAndRepeatWithViewLifecycle {
            authViewModel.logoutState.collect {
                when (it) {
                    is UiStateObject.LOADING -> showProgress()

                    is UiStateObject.SUCCESS -> {
                        hideProgress()
                        sharedPref.isEntered = false
                        requireActivity().findNavController(R.id.main_container)
                            .navigate(R.id.action_mainFragment_to_loginFragment)
                    }

                    is UiStateObject.ERROR -> {
                        hideProgress()
                        showMessage(it.message)
                    }

                    else -> Unit
                }
            }
        }
    }

}
