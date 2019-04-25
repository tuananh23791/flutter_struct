package com.beyondedge.hm.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

/**
 * Created by Hoa Nguyen on Apr 24 2019.
 */
public class URLUtils {

    public static boolean isURLValid(String url) {
        return !TextUtils.isEmpty(url) && url.startsWith("http");
    }

    public static void openInWebBrowser(Context activity, String url) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        activity.startActivity(browserIntent);
    }
}
