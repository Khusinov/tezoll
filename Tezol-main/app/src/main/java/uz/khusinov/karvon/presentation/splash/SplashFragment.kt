package uz.khusinov.karvon.presentation.splash

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import uz.khusinov.karvon.R
import uz.khusinov.karvon.SharedPref
import uz.khusinov.karvon.databinding.FragmentSplashBinding
import uz.khusinov.karvon.utills.viewBinding
import javax.inject.Inject


@AndroidEntryPoint
class SplashFragment : Fragment(R.layout.fragment_splash) {

    private val binding by viewBinding { FragmentSplashBinding.bind(it) }

    @Inject
    lateinit var sharedPref: SharedPref

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun setupUI() {
        binding.apply {

            lifecycleScope.launch {
                delay(1500)
                if (sharedPref.isEntered)
                    findNavController().navigate(R.id.action_splashFragment_to_mainFragment)
                else
                    findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        setupUI()
    }


}