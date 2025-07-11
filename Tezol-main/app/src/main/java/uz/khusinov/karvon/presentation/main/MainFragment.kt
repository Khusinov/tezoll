package uz.khusinov.karvon.presentation.main

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import uz.khusinov.karvon.R
import uz.khusinov.karvon.SharedPref
import uz.khusinov.karvon.databinding.FragmentMainBinding
import uz.khusinov.karvon.presentation.BaseFragment
import uz.khusinov.karvon.utills.viewBinding
import javax.inject.Inject

@AndroidEntryPoint
class MainFragment : BaseFragment(R.layout.fragment_main) {


    private lateinit var navController: NavController
    private var lastBackPressedTime = 0L
    private val binding by viewBinding { FragmentMainBinding.bind(it) }
    @Inject
    lateinit var sharedPref: SharedPref


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            onBackPressedCallback
        )
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            val currentTime = System.currentTimeMillis()
            if (currentTime - lastBackPressedTime < 2000) {
                activity?.finish()
            } else {
                lastBackPressedTime = currentTime
                showToast(getString(R.string.exit_app))
            }
        }
    }

    private fun setupUI() = with(binding) {
        val navHostFragment =
            childFragmentManager.findFragmentById(R.id.home_container) as NavHostFragment
        navController = navHostFragment.navController
        bottomNavBar.setupWithNavController(navController)
//        if (sharedPref.toBasket) {
//            bottomNavBar.selectedItemId = R.id.basket
//            //sharedPref.toBasket = false
//        }
    }


    override fun onResume() {
        super.onResume()
        if (sharedPref.toBasket) {
            binding.bottomNavBar.selectedItemId = R.id.basket
            sharedPref.toBasket = false
        }
    }
}