package com.hm.gillcaptital.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.hm.gillcaptital.HMApplication;
import com.hm.gillcaptital.config.Constant;
import com.hm.gillcaptital.config.HMConfig;
import com.google.gson.JsonSyntaxException;

/**
 * Created by Hoa Nguyen on Apr 26 2019.
 */
public class PrefManager {
    public static final String SHARE_PRE_NAME = "SHARE_PRE_NAME2";
    private static final String CURRENT_LINK_CONFIG = "CURRENT_LINK_CONFIG";
    private static final String CURRENT_HM_CONFIG_JSON = "CURRENT_HM_CONFIG_JSON";
    private static final String CURRENT_HM_CHEATING_TEMPLATE = "CURRENT_HM_CHEATING_TEMPLATE";
    private static PrefManager instance;
    private Context mContext;
//    private Preference preference = PowerPreference.getDefaultFile();

    public PrefManager(Context context) {
        mContext = context;
    }

    public static PrefManager getInstance(Context context) {
        if (instance == null) {
            synchronized (PrefManager.class) {
                if (instance == null) {
                    instance = new PrefManager(context.getApplicationContext());
                }
            }
        }
        return instance;
    }

//    public void putCurrentLinkConfig(String url) {
//        preference.put(CURRENT_LINK_CONFIG, url);
//    }
//
//    public String getCurrentLinkConfig() {
//        String string = preference.getString(CURRENT_LINK_CONFIG);
//        if (TextUtils.isEmpty(string)) {
//            string = Constant.LINK_CONFIG;
//        }
//        return string;
//    }

    public void putCurrentLinkConfig(String url) {
        SharedPreferences sharedPref =
                mContext.getSharedPreferences(SHARE_PRE_NAME, Context.MODE_PRIVATE);
        sharedPref.edit().putString(CURRENT_LINK_CONFIG, url).commit();
    }

    public String getCurrentLinkConfig() {
        SharedPreferences sharedPref =
                mContext.getSharedPreferences(SHARE_PRE_NAME, Context.MODE_PRIVATE);
        String string = sharedPref.getString(CURRENT_LINK_CONFIG, null);

        if (TextUtils.isEmpty(string)) {
            string = Constant.LINK_CONFIG;
        }
        return string;
    }

    public void putCurrentHMConfigJson(HMConfig config) {
        String json = "";

        if (config != null) {
            try {
                json = HMApplication.getGson().toJson(config);
            } catch (JsonSyntaxException e) {

            }
        }

        SharedPreferences sharedPref =
                mContext.getSharedPreferences(SHARE_PRE_NAME, Context.MODE_PRIVATE);
        sharedPref.edit().putString(CURRENT_HM_CONFIG_JSON, json).commit();
    }

    public HMConfig getCurrentHMConfigJson() {
        HMConfig config = null;
        SharedPreferences sharedPref =
                mContext.getSharedPreferences(SHARE_PRE_NAME, Context.MODE_PRIVATE);
        String json = sharedPref.getString(CURRENT_HM_CONFIG_JSON, "");

        if (json != null) {
            try {
                config = HMApplication.getGson().fromJson(json, HMConfig.class);
            } catch (JsonSyntaxException e) {

            }
        }
        return config;
    }

    public void putCheatingShowHideTemplate(boolean isShow) {
        SharedPreferences sharedPref =
                mContext.getSharedPreferences(SHARE_PRE_NAME, Context.MODE_PRIVATE);
        sharedPref.edit().putBoolean(CURRENT_HM_CHEATING_TEMPLATE, isShow).commit();
    }

    public boolean getCheatingShowHideTemplate() {
        SharedPreferences sharedPref =
                mContext.getSharedPreferences(SHARE_PRE_NAME, Context.MODE_PRIVATE);
        return sharedPref.getBoolean(CURRENT_HM_CHEATING_TEMPLATE, true);
    }
}
