package com.hm.gillcaptital.utils;

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

    public static void share(Context activity, String url) {
//        Intent browserIntent = new Intent(Intent.ACTION_SEND, Uri.parse(url));
//        activity.startActivity(browserIntent);

        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "H&M Products");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, url);
        activity.startActivity(Intent.createChooser(sharingIntent, "Sharing Option"));
    }
}
