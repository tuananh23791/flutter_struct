package com.beyondedge.hm.utils;

import android.text.TextUtils;

import com.beyondedge.hm.HMApplication;
import com.beyondedge.hm.config.Constant;
import com.beyondedge.hm.config.HMConfig;
import com.google.gson.JsonSyntaxException;
import com.preference.PowerPreference;
import com.preference.Preference;

/**
 * Created by Hoa Nguyen on Apr 26 2019.
 */
public class PrefManager {
    private static final String CURRENT_LINK_CONFIG = "CURRENT_LINK_CONFIG";
    private static final String CURRENT_HM_CONFIG_JSON = "CURRENT_HM_CONFIG_JSON";
    private static final String CURRENT_HM_CHEATING_TEMPLATE = "CURRENT_HM_CHEATING_TEMPLATE";
    private static PrefManager instance;
    private Preference preference = PowerPreference.getDefaultFile();

    public static PrefManager getInstance() {
        if (instance == null) {
            synchronized (PrefManager.class) {
                if (instance == null) {
                    instance = new PrefManager();
                }
            }
        }
        return instance;
    }

    public void putCurrentLinkConfig(String url) {
        preference.put(CURRENT_LINK_CONFIG, url);
    }

    public String getCurrentLinkConfig() {
        String string = preference.getString(CURRENT_LINK_CONFIG);
        if (TextUtils.isEmpty(string)) {
            string = Constant.LINK_CONFIG;
        }
        return string;

//        return Constant.LINK_CONFIG;
    }

    public void putCurrentHMConfigJson(HMConfig config) {
        String json = "";

        if (config != null) {
            try {
                json = HMApplication.getGson().toJson(config);
            } catch (JsonSyntaxException e) {

            }
        }
        preference.put(CURRENT_HM_CONFIG_JSON, json);
    }

    public HMConfig getCurrentHMConfigJson() {
        HMConfig config = null;
        String json = preference.getString(CURRENT_HM_CONFIG_JSON);

        if (json != null) {
            try {
                config = HMApplication.getGson().fromJson(json, HMConfig.class);
            } catch (JsonSyntaxException e) {

            }
        }
        return config;
    }

    public void putCheatingShowHideTemplate(boolean isShow) {
        preference.put(CURRENT_HM_CHEATING_TEMPLATE, isShow);
    }

    public boolean getCheatingShowHideTemplate() {
        return preference.getBoolean(CURRENT_HM_CHEATING_TEMPLATE, true);
    }
}
