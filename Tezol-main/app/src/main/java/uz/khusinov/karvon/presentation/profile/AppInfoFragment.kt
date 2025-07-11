package uz.khusinov.karvon.presentation.profile

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import uz.khusinov.karvon.R
import uz.khusinov.karvon.databinding.FragmentAppInfoBinding
import uz.khusinov.karvon.presentation.BaseFragment
import uz.khusinov.karvon.utills.viewBinding

class AppInfoFragment : BaseFragment(R.layout.fragment_app_info) {
    private val binding by viewBinding { FragmentAppInfoBinding.bind(it) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
    }

    private fun setupUI() = with(binding) {

        toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        termOfUse.setOnClickListener {
        }

        creator.setOnClickListener {
        }

    }
}