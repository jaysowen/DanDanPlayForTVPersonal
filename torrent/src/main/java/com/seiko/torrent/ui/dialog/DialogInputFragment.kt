package com.seiko.torrent.ui.dialog

import android.os.Bundle
import android.util.TypedValue.COMPLEX_UNIT_SP
import android.util.TypedValue.applyDimension
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.FragmentManager
import com.blankj.utilcode.util.ToastUtils
import com.seiko.common.ui.dialog.BaseDialogFragment
import com.seiko.torrent.R
import com.seiko.torrent.databinding.TorrentDialogInputFragmentBinding
import kotlinx.android.synthetic.main.torrent_dialog_input_fragment.view.*


class DialogInputFragment : BaseDialogFragment()
    , View.OnFocusChangeListener
    , View.OnClickListener {

    private var onConfirm: ((String) -> Unit)? = null

    private lateinit var binding: TorrentDialogInputFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = TorrentDialogInputFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bundle = arguments
        if (bundle != null) {
            if (bundle.containsKey(ARGS_HINT)) {
                binding.torrentEdit.hint = bundle.getString(ARGS_HINT)
            }
            if (bundle.containsKey(ARGS_CONFIRM_TEXT)) {
                binding.btnCenter.text = bundle.getString(ARGS_CONFIRM_TEXT)
            }
            if (bundle.containsKey(ARGS_CANCEL_TEXT)) {
                binding.btnCancel.text = bundle.getString(ARGS_CANCEL_TEXT)
            }
        }
        binding.btnCancel.requestFocus()
        binding.btnCenter.setOnClickListener(this)
        binding.btnCancel.setOnClickListener(this)
        binding.btnCenter.onFocusChangeListener = this
        binding.btnCancel.onFocusChangeListener = this
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.btn_center -> {
                val text = binding.torrentEdit.text.toString()
                if (text.isEmpty()) {
                    ToastUtils.showShort("内容为空。")
                    return
                }
                onConfirm?.invoke(text.trim())
                dismissDialog()
            }
            R.id.btn_cancel -> {
                dismissDialog()
            }
        }
    }

    override fun onFocusChange(v: View?, hasFocus: Boolean) {
//        if (v is Button) {
//            v.textSize = customTextSize(if (hasFocus) LARGE else SMALL)
//        }
    }

    private fun setConfirmClickListener(listener: ((String) -> Unit)?) {
        onConfirm = listener
    }

    fun show(manager: FragmentManager) {
        show(manager, TAG)
    }

    fun dismissDialog() {
        dismissDialog(TAG)
    }

    class Builder {
        private val bundle = Bundle()
        private var confirmClickListener: ((String) -> Unit)? = null

        fun setHint(hint: String): Builder {
            bundle.putString(ARGS_HINT, hint)
            return this
        }

        fun setConfirmText(text: String): Builder {
            bundle.putString(ARGS_CONFIRM_TEXT, text)
            return this
        }

        fun setConfirmClickListener(listener: (String) -> Unit): Builder {
            confirmClickListener = listener
            return this
        }

        fun setCancelText(text: String): Builder {
            bundle.putString(ARGS_CANCEL_TEXT, text)
            return this
        }

        fun build(): DialogInputFragment {
            val fragment = DialogInputFragment()
            fragment.arguments = bundle
            fragment.setConfirmClickListener(confirmClickListener)
            return fragment
        }
    }

    companion object {
        const val TAG = "DialogInputFragment"

        private const val ARGS_HINT = "ARGS_HINT"
        private const val ARGS_CONFIRM_TEXT = "ARGS_CONFIRM_TEXT"
        private const val ARGS_CANCEL_TEXT = "ARGS_CANCEL_TEXT"
    }

}