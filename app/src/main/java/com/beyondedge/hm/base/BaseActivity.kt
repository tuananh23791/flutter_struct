package com.beyondedge.hm.base

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.beyondedge.hm.R
import com.google.android.material.appbar.AppBarLayout

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
            if (title.isNotEmpty()) {
                textTitle.visibility = View.VISIBLE
                textTitle.text = title
            } else {
                textTitle.visibility = View.GONE
            }
        }
    }

    fun validateTitle(isShow: Boolean) {
        val textTitle = findViewById<TextView>(R.id.txt_title)
        textTitle?.let {
            if (isShow) {
                textTitle.visibility = View.VISIBLE
            } else {
                textTitle.visibility = View.GONE
            }
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

    protected fun marginTopFrame(isMargin: Boolean) {
        val appBarLayout = findViewById<AppBarLayout>(R.id.appBarLayout)
        val toolbar = findViewById<Toolbar>(R.id.tool_bar)

        val layoutFrame = findViewById<View>(R.id.frame_content)
        val layoutParams = layoutFrame.layoutParams as RelativeLayout.LayoutParams
        val styledAttributes = theme.obtainStyledAttributes(
                intArrayOf(android.R.attr.actionBarSize))
        val actionBarSize = styledAttributes.getDimension(0, 0f).toInt()
        styledAttributes.recycle()
        layoutParams.topMargin = if (isMargin) actionBarSize else 0

        layoutFrame.layoutParams = layoutParams

        toolbar?.let {
            validateTitle(isMargin)
            if (isMargin) {
                appBarLayout.background = ContextCompat.getDrawable(this, R.color.colorBackground)
                toolbar.background = ContextCompat.getDrawable(this, R.color.colorBackground)
            } else {
                appBarLayout.background = null
                toolbar.background = null

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    appBarLayout.outlineProvider = null
                }
            }


        }
    }

}
