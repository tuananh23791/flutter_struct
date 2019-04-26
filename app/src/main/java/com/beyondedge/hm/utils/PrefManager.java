package com.beyondedge.hm.utils;

import android.text.TextUtils;

import com.beyondedge.hm.config.Constant;
import com.preference.PowerPreference;
import com.preference.Preference;

/**
 * Created by Hoa Nguyen on Apr 26 2019.
 */
public class PrefManager {
    private static final String CURRENT_LINK_CONFIG = "CURRENT_LINK_CONFIG";
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
    }
}
