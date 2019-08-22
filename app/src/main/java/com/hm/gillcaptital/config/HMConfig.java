package com.hm.gillcaptital.config;

import android.content.Context;
import android.text.TextUtils;

import com.hm.gillcaptital.utils.CollectionUtils;
import com.google.gson.JsonElement;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import timber.log.Timber;

import static com.hm.gillcaptital.config.Constant.MENU_CATALOGUE_LOOKUP_PATH;
import static com.hm.gillcaptital.config.Constant.MENU_MORE_PATH;

/**
 * Created by Hoa Nguyen on Apr 22 2019.
 * <p>
 * Note from Jul 17 2019
 * QAT:  https://hm-uat-media.s3-ap-southeast-1.amazonaws.com/mobileapp/hmqat/setting/TH-EN.txt
 * UAT:  https://hm-uat-media.s3-ap-southeast-1.amazonaws.com/mobileapp/hmuat/setting/TH-EN.txt
 * Pro:  https://hm-uat-media.s3-ap-southeast-1.amazonaws.com/mobileapp/hm/setting/TH-EN.txt
 * <p>
 * App name:
 * HM QAT
 * HM UAT
 * HM
 * <p>
 * Release Noted
 * //TODO enable BuildConfig.ENABLE_CHECK_VERSION, when release
 */

public class HMConfig {
    @SerializedName("lang")
    public JsonElement jsonLanguage;

    @SerializedName("version")
    private Version version;
    @SerializedName("mainMenu")
    private ArrayList<Menu> mainMenu;
    @SerializedName("subListMore")
    private ArrayList<Menu> subListMore;
    @SerializedName("subListFolowUs")
    private ArrayList<Menu> subListMenuFolowUs;
    @SerializedName("region")
    private ArrayList<Region> region;

//    @SerializedName("PaymentUrlOpenInApp")
//    private ArrayList<String> paymentUrlOpenInApp;

    private JsonElement getLang() {
        return jsonLanguage;
    }

    public String getLanguageBy(String param) {
        try {
            JsonElement jsonElement = jsonLanguage.getAsJsonObject().get(param);

            if (jsonElement != null) {
                return jsonElement.getAsString();
            }
        } catch (Exception ignored) {

        }

        return "";
    }

    public ArrayList<Region> getRegion() {
        return region;
    }

    public Version getVersion() {
        return version;
    }

    public ArrayList<Menu> getMainMenuList() {
        return mainMenu;
    }

    public ArrayList<Menu> getMoreSubListMenuList() {
        return subListMore;
    }

    public ArrayList<Menu> getSubListMenuFolowUs() {
        return subListMenuFolowUs;
    }

    public Menu getMoreMenu() {
        try {
            for (Menu menu : mainMenu) {
                if (Constant.MENU_MORE_PATH.equals(menu.url)) {
                    return menu;
                }
            }
        } catch (Exception e) {

        }

        return null;
    }

    public Menu getFollowUsMenu() {
        try {
            for (Menu menu : subListMore) {
                if (Constant.FOLLOW_US_PATH.equals(menu.url)) {
                    return menu;
                }
            }
        } catch (Exception e) {

        }

        return null;
    }

    boolean isValidConfig() {
        return version != null && !TextUtils.isEmpty(version.mainDomain) && CollectionUtils.isNotEmpty(mainMenu);
    }

//    public ArrayList<String> getPaymentUrlOpenInApp() {
//        if (paymentUrlOpenInApp == null) {
//            return new ArrayList<>();
//        }
//        return paymentUrlOpenInApp;
//    }

    public static class Version {
        @SerializedName("versionAndroidCode")
        private ArrayList<String> versionAndroidForceUpdate;

        @SerializedName("MainDomain")
        private String mainDomain;

        @SerializedName("iconUrl")
        private String iconUrl;

        public ArrayList<String> getVersionAndroidForceUpdate() {
            return versionAndroidForceUpdate;
        }

        public String getMainDomain() {
            return mainDomain;
        }

        public String getIconUrl() {
            return iconUrl;
        }
    }

    public static class Menu {
        private String url;
        private String name;
        private String iconName;
        private ArrayList<Menu> subListMenu;

        public boolean isExternalURL() {
            return !TextUtils.isEmpty(url) && url.startsWith("http");
        }

        public String getUrl(Context context) {
            if (isExternalURL()) {
                return url;
            }
            HMConfig config = LoadConfig.getInstance(context).load();
            if (config != null) {
                try {
                    return config.version.getMainDomain() + url;
                } catch (Exception e) {
                    Timber.e(e);
                }
                return "";
            }
            return url;
        }

        public String getRelativeUrl() {
            return url;
        }

        public String getName() {
            return name;
        }

        //        public String getIconName() {
//            return iconName;
//        }
        public String getIconFullUrl() {
//            HMConfig config = LoadConfig.getInstance(context).load();
//            if (config != null) {
//                try {
//                    return config.version.getIconUrl() + iconName;
//                } catch (Exception e) {
//                    Timber.e(e);
//                }
//                return "";
//            }
            return iconName;
        }


        public ArrayList<Menu> getSubListMenu() {
            return subListMenu;
        }

        public boolean isMoreMenu() {
            return MENU_MORE_PATH.equals(url);
        }

        public boolean isCatalogueLookupMenu() {
            return MENU_CATALOGUE_LOOKUP_PATH.equals(url);
        }

        public boolean hasSubMenu() {
            return !CollectionUtils.isEmpty(subListMenu);
        }
    }

    public static class Region {
        private String name;
        private String propertyFile;

        public String getName() {
            return name;
        }

        public String getPropertyFile() {
            return propertyFile;
        }
    }

    public static class Lang {
        private String search_product;

        public String getSearchProduct() {
            return search_product;
        }
    }
}
