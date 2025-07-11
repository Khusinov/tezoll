package uz.khusinov.karvon.presentation.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.navigation.fragment.findNavController
import uz.khusinov.karvon.R
import uz.khusinov.karvon.databinding.FragmentIntroduction1Binding
import uz.khusinov.karvon.utills.viewBinding


class Introduction1Fragment : Fragment(R.layout.fragment_introduction1) {

    private val binding by viewBinding { FragmentIntroduction1Binding.bind(it) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
    }

    private fun setupUI() {
        binding.apply {


            next.setOnClickListener {
                findNavController().navigate(R.id.action_introduction1Fragment_to_introduction2Fragment)
            }

        }
    }
}