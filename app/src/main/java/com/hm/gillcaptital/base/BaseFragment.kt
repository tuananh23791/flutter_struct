package com.hm.gillcaptital.base

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment

import com.hm.gillcaptital.R
import com.hm.gillcaptital.ui.view.LoadingDialogFragment

/**
 * Created by Hoa Nguyen on Apr 23 2019.
 */
open class BaseFragment : Fragment() {

    private val loadingDialog: DialogFragment by lazy {
        LoadingDialogFragment.newInstance()
    }

    protected fun startActivity(className: Class<*>) {
        startActivity(Intent(this.activity, className))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.web_page_layout, container, false)
    }

    protected fun showPopupError(error: String) {
        AlertDialog.Builder(context)
                .setMessage(error)
                .setPositiveButton(android.R.string.ok, null)
                .show()
    }

    fun showLoading() {
        if (loadingDialog.isVisible)
            loadingDialog.dismiss()

        loadingDialog.show(childFragmentManager, LoadingDialogFragment::class.java.simpleName)
    }

    fun hideLoading() {
        loadingDialog.dismiss()
    }
}
