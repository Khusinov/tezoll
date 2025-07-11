package uz.khusinov.karvon.presentation.profile


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import uz.khusinov.karvon.R
import uz.khusinov.karvon.databinding.FragmentSupportBinding
import uz.khusinov.karvon.utills.viewBinding

@AndroidEntryPoint
class SupportFragment : BottomSheetDialogFragment(R.layout.fragment_support) {

    private val binding by viewBinding(FragmentSupportBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
    }

    private fun setupUI() {
        binding.apply {

            technicalSupport.setOnClickListener {
                Intent(Intent.ACTION_DIAL, Uri.parse("tel:+998900771237")).also {
                    startActivity(it)
                }
            }

            operator.setOnClickListener {
                Intent(Intent.ACTION_DIAL, Uri.parse("tel:+998900771237")).also {
                    startActivity(it)
                }
            }
        }
    }
}