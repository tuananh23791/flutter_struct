package com.hm.gillcaptital.ui.screen

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.hm.gillcaptital.BuildConfig
import com.hm.gillcaptital.HMApplication
import com.hm.gillcaptital.R
import com.hm.gillcaptital.base.BaseFragment
import com.hm.gillcaptital.config.HMConfig
import com.hm.gillcaptital.config.LoadConfig
import com.hm.gillcaptital.databinding.SettingsLayoutBinding
import com.hm.gillcaptital.ui.SplashScreen
import com.hm.gillcaptital.utils.PrefManager

/**
 * Created by Hoa Nguyen on Apr 25 2019.
 */
class SettingsFragment : BaseFragment() {
    private lateinit var binding: SettingsLayoutBinding
    private var whichRegion = 1
    private var isForceRestartApp = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.settings_layout, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.appVersion = (view.context.applicationContext as HMApplication).versionName

        updateUIRegion()
        binding.pressRegion.setOnClickListener {
            val config = LoadConfig.getInstance(activity).load()
            val regions = config.region
            val arrs = arrayOfNulls<CharSequence>(regions.size)
            var region: HMConfig.Region
            val currentLinkConfig = PrefManager.getInstance(activity).currentLinkConfig
            for (i in regions.indices) {
                region = regions[i]
                arrs[i] = region.name

                if (region.propertyFile == currentLinkConfig) {
                    whichRegion = i
                }
            }
            AlertDialog.Builder(view.context)
                    .setSingleChoiceItems(arrs, whichRegion) { dialog, which ->
                        val selectedRegion = regions[which]
                        PrefManager.getInstance(activity).putCurrentLinkConfig(selectedRegion.propertyFile)
                        if (which != whichRegion) {
                            isForceRestartApp = true
                        }
                        dialog.dismiss()
                    }
                    .setPositiveButton(android.R.string.ok, null)
                    .setOnDismissListener {
                        if (isForceRestartApp) {
                            Handler().postDelayed({ this.restartApp() }, 200)
                        }
                    }
                    .show()
        }

        binding.pressPermission.setOnClickListener {
            val intent = Intent()
            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            val uri = Uri.fromParts("package", HMApplication.getInstance().packageName, null)
            intent.data = uri
            activity!!.startActivity(intent)
        }

        if (BuildConfig.DEBUG && BuildConfig.TEMPLATE) {
            binding.groupCheating.visibility = View.VISIBLE

            binding.checkboxTemplate.isChecked = PrefManager.getInstance(activity).cheatingShowHideTemplate

            binding.checkboxTemplate.setOnCheckedChangeListener { _, isChecked ->
                PrefManager.getInstance(activity).putCheatingShowHideTemplate(isChecked)
                Handler().postDelayed({ this.restartApp() }, 200)
            }

        } else {
            binding.groupCheating.visibility = View.GONE
        }
    }

    override fun onResume() {
        super.onResume()
        updateAppPermission()
    }

    private fun updateAppPermission() {
        val builder = StringBuilder()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(activity!!, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                builder.append("- Camera: On")
            } else {
                builder.append("- Camera: Off")
            }

            builder.append("\n")
            if (ContextCompat.checkSelfPermission(activity!!, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                builder.append("- Storage: On")
            } else {
                builder.append("- Storage: Off")
            }

            binding.permissionText = builder.toString()
        } else {
            builder.append("N/A")
            binding.permissionText = builder.toString()
        }
    }

    private fun updateUIRegion() {
        val config = LoadConfig.getInstance(activity).load()
        val regions = config.region
        var region: HMConfig.Region
        val currentLinkConfig = PrefManager.getInstance(activity).currentLinkConfig
        for (i in regions.indices) {
            region = regions[i]

            if (region.propertyFile == currentLinkConfig) {
                whichRegion = i
            }
        }
        val selectedRegion = regions[whichRegion]
        binding.region = selectedRegion.name

        binding.tvLang.text = config.getLanguageBy("search_product")
        binding.tvLangInfo.text = selectedRegion.propertyFile
    }

    private fun restartApp() {
        SplashScreen.startActivity(activity)
    }

    companion object {

        fun newInstance(): SettingsFragment {

            val args = Bundle()

            val fragment = SettingsFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
