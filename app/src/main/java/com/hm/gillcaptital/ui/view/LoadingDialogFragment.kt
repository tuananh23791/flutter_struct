package com.hm.gillcaptital.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.hm.gillcaptital.R

/**
 * Created by Hoa Nguyen on May 06 2019.
 */
class LoadingDialogFragment : DialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = false
    }

    //    @NonNull
    //    @Override
    //    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
    //        ProgressDialog dialog = new ProgressDialog(getActivity(), getTheme());
    //        dialog.setIndeterminate(true);
    //        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    //        return dialog;
    //    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.loading_dialog, container, false)
    }

    companion object {
        fun newInstance(): LoadingDialogFragment {
            val args = Bundle()
            val fragment = LoadingDialogFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
