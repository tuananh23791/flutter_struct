package com.beyondedge.hm.base

import android.content.DialogInterface
import android.content.Intent
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.beyondedge.hm.R

/**
 * Created by Hoa Nguyen on Apr 20 2019.
 */
abstract class BaseActivity : AppCompatActivity() {

    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)

        settingView()
    }

    private fun settingView() {
        val toolbar = findViewById<Toolbar>(R.id.tool_bar)

        toolbar?.let {
            setSupportActionBar(it)
        }
    }

    fun setTitleToolbar(title: String) {
        val textTitle = findViewById<TextView>(R.id.txt_title)
        textTitle?.let {
            textTitle.visibility = View.VISIBLE
            textTitle.text = title
        }
    }

    protected open fun enableBackButtonToolbar(clickListener: View.OnClickListener?) {
        val backBt = findViewById<View>(R.id.btn_back)
        backBt?.let {
            backBt.visibility = View.VISIBLE
            if (clickListener != null) {
                backBt.setOnClickListener(clickListener)
            } else {
                backBt.setOnClickListener { onBackPressed() }
            }
        }
    }

    protected fun startActivity(className: Class<*>) {
        startActivity(Intent(this, className))
    }

    protected fun showAlertDialog(mess: String, canCancel: Boolean,
                                  btListener: DialogInterface.OnClickListener) {
        AlertDialog.Builder(this)
                .setMessage(mess)
                .setCancelable(canCancel)
                .setPositiveButton(android.R.string.ok, btListener)
                .show()
    }

}
