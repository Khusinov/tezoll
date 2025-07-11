package uz.khusinov.karvon.presentation.shops.components

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.Window
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import uz.khusinov.karvon.R
import uz.khusinov.karvon.databinding.DialogShopsConfirmBinding
import uz.khusinov.karvon.utills.viewBinding

class ShopsConfirmDialog(
    private val title: String,
    private val message: String? = null,
    private val header: String? = null,
    private val action: Action,
    private val secondAction: Action,
) : DialogFragment(R.layout.dialog_shops_confirm) {

    private val binding by viewBinding(DialogShopsConfirmBinding::bind)

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(MATCH_PARENT, WRAP_CONTENT)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUi()
    }

    private fun setupUi() = with(binding) {
        dialogTitle.text = title
        dialogMessage.isVisible = message != null
        dialogMessage.text = message
        headerText.isVisible = header != null
        headerText.text = header
        confirmText.text = action.title
        cancelText.text = secondAction.title

        if (action.primary) {
            confirmButton.setCardBackgroundColor(requireContext().getColor(R.color.main_color))
            confirmText.setTextColor(Color.WHITE)
        } else {
            confirmButton.setCardBackgroundColor(requireContext().getColor(R.color.gray_color))
            confirmText.setTextColor(resources.getColor(R.color.black, null))
        }

        if (secondAction.primary) {
            cancelButton.setCardBackgroundColor(requireContext().getColor(R.color.main_color))
            cancelText.setTextColor(Color.WHITE)
        } else {
            cancelButton.setCardBackgroundColor(requireContext().getColor(R.color.gray_color))
            cancelText.setTextColor(resources.getColor(R.color.black, null))
        }

        confirmButton.setOnClickListener {
            dismiss()
            action.onClick.invoke()
        }

        cancelButton.setOnClickListener {
            dismiss()
            secondAction.onClick.invoke()
        }
    }
}

data class Action(val title: String, val primary: Boolean, val onClick: () -> Unit)