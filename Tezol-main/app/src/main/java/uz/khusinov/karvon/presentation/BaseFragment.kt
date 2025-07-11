package uz.khusinov.karvon.presentation

import ProgressBarDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import uz.khusinov.karvon.SharedPref
import uz.khusinov.karvon.utills.Constants.ERROR_401
import uz.khusinov.karvon.utills.dialog.MessageDialog
import javax.inject.Inject

@AndroidEntryPoint
abstract class BaseFragment(private val layoutRes: Int) : Fragment() {

    @Inject
    lateinit var appPref: SharedPref
    private lateinit var loadingDialog: ProgressBarDialog
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return inflater.inflate(layoutRes, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadingDialog = ProgressBarDialog(requireContext())
    }

    protected fun showMessage(message: String) {
        val dialog = MessageDialog(message)
        dialog.onClickListener = {
            dialog.dismiss()
            if (message == ERROR_401) {
                appPref.access = ""
                openLogin()
            }
        }
        dialog.show(childFragmentManager, "message_dialog")
    }

    fun showProgress() {
        loadingDialog.show()
    }

    fun hideProgress() {
        loadingDialog.dismiss()
    }

    fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    private fun openLogin() {
        // findNavController().navigate(R.id.loginFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (loadingDialog.isShowing) {
            loadingDialog.dismiss()
        }
    }
}