package com.beyondedge.hm.config;

import android.net.Uri;
import android.os.Environment;

import androidx.annotation.NonNull;

/**
 * Created by Hoa Nguyen on Apr 23 2019.
 */
public class Constant {
    public static final boolean IS_FORCE_LOCAL_CONFIG = true;
    public static final String LINK_CONFIG = "http://sharefile.beyondedge.com.sg/HM/setting/id_en.txt";

    @NonNull
    private static String getSaveDir() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() + "/fetch";
    }

    @NonNull
    public static String getLinkSavedFile() {
        return getSaveDir() + Uri.parse(LINK_CONFIG).getLastPathSegment();
    }
}
