package com.beyondedge.hm.config;

import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Base64;

import androidx.annotation.NonNull;

/**
 * Created by Hoa Nguyen on Apr 23 2019.
 */
public class Constant {
    public static final boolean IS_FORCE_LOCAL_CONFIG = false;
    public static final String LINK_CONFIG = "http://sharefile.beyondedge.com.sg/hm/setting/ID-EN.txt";
    public static final String FOLLOW_US_PATH = "subListMenuFolowUs";
    public static final String APP_SETTING_PATH = "app_setting";
    public static final String MENU_MORE_PATH = "subListMore";


    public static String getAuthorizationParam() {
        String username = "hm";
        String password = "Hm@123456";
        if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {
            String credentials = username + ":" + password;
            return "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
        }

        return "";
    }

    @NonNull
    private static String getSaveDir() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() + "/hm/";
    }

    @NonNull
    public static String getLinkSavedFile() {
        return getSaveDir() + Uri.parse(LINK_CONFIG).getLastPathSegment();
    }
}
