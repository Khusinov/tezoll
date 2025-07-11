package uz.khusinov.karvon.presentation.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.navigation.fragment.findNavController
import uz.khusinov.karvon.R
import uz.khusinov.karvon.databinding.FragmentIntroduction3Binding
import uz.khusinov.karvon.utills.viewBinding


class Introduction3Fragment : Fragment(R.layout.fragment_introduction3) {

    private val binding by viewBinding { FragmentIntroduction3Binding.bind(it) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
    }

    private fun setupUI() {
        binding.apply {

            next.setOnClickListener {
                findNavController().navigate(R.id.action_introduction3Fragment_to_mainFragment)
            }
        }
    }
}