package com.beyondedge.hm.utils;

import android.text.TextUtils;

/**
 * Created by Hoa Nguyen on Apr 24 2019.
 */
public class URLUtils {

    public static boolean isURLValid(String url) {
        return !TextUtils.isEmpty(url) && url.startsWith("http");
    }
}
