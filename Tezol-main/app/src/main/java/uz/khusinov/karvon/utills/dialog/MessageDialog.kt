package uz.khusinov.karvon.utills.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import uz.khusinov.karvon.R
import uz.khusinov.karvon.databinding.DialogMessageBinding

class MessageDialog(private val message: String, private val dialogText: String? = null) :
    DialogFragment() {
    var onClickListener: (() -> Unit)? = null
    private var _bn: DialogMessageBinding? = null
    private val bn get() = _bn!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.CustomDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _bn = DialogMessageBinding.inflate(inflater, container, false)
        return bn.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isCancelable = false
        bn.apply {
            if (dialogText != null) {
                ok.text = dialogText
            } else {
                ok.text = getString(R.string.ok)
            }
            title.text = message
            ok.setOnClickListener {
                onClickListener?.invoke()
                dismiss()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _bn = null
    }
}