package com.beyondedge.hm.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import java.util.ArrayList;

/**
 * Created by Hoa Nguyen on May 02 2019.
 */
public class AppVersion {
    public static boolean isLastVersion(Context context, ArrayList<String> listAvailableVersions) {
        String currentAppVer = getCurrentAppVer(context);
        return isLastVersion(currentAppVer, listAvailableVersions);
    }

    public static boolean isLastVersion(String currentAppVer, ArrayList<String> listAvailableVersions) {
        if (CollectionUtils.isEmpty(listAvailableVersions) || TextUtils.isEmpty(currentAppVer)) {
            return false;
        }
        String largestVer = currentAppVer;

        for (String ver : listAvailableVersions) {
            if (isLager(ver, largestVer)) {
                largestVer = ver;
            }
        }

        return !largestVer.equals(currentAppVer);
    }

    public static boolean versionContains(Context context, ArrayList<String> listAvailableVersions) {
        if (CollectionUtils.isEmpty(listAvailableVersions)) {
            return false;
        }

        String currentAppVer = getCurrentAppVer(context);

        return listAvailableVersions.contains(currentAppVer);
    }

    private static String getCurrentAppVer(Context context) {
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * check version is lager or equal other version
     */
    public static boolean isLager(String ver, String ver2) {
        if (TextUtils.isEmpty(ver) || TextUtils.isEmpty(ver2)) return false;

        String[] parse = ver.split("\\.");
        String[] parse2 = ver2.split("\\.");

        int num, num2;

        for (int i = 0; i < parse.length; i++) {
            num = valueOf(getStringNum(parse, i));
            num2 = valueOf(getStringNum(parse2, i));
            if (num > num2) {
                return true;
            } else if (num < num2) {
                return false;
            } else {//==
                //skip next
            }
        }

        //== ver is return true
        return true;
    }

    public static String getStringNum(String[] parse, int pos) {
        if (parse == null || parse.length == 0 || parse.length < pos) return "0";
        return parse[pos];

    }

    public static int valueOf(String num) {
        try {
            return Integer.valueOf(num);
        } catch (NumberFormatException ignored) {

        }
        return 0;
    }
}
