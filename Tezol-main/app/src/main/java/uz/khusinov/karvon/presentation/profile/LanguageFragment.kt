package uz.khusinov.karvon.presentation.profile


import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import uz.khusinov.karvon.Language
import uz.khusinov.karvon.MainActivity
import uz.khusinov.karvon.R
import uz.khusinov.karvon.databinding.FragmentLanguageBinding
import uz.khusinov.karvon.utills.viewBinding

class LanguageFragment : BottomSheetDialogFragment(R.layout.fragment_language) {

    private val binding by viewBinding(FragmentLanguageBinding::bind)
    private var language: Language = Language.UZ

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
    }

    private fun setupUI() {
        binding.apply {

            uz.setOnClickListener {
                language = Language.UZ
            }
            ru.setOnClickListener {
                language = Language.RU
            }
            ready.setOnClickListener {
                    (requireActivity() as MainActivity).changeLanguage(language)
                    dismiss()
            }

        }
    }
}