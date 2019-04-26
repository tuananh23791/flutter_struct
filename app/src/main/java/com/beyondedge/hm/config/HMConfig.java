package com.beyondedge.hm.config;

import android.text.TextUtils;

import com.beyondedge.hm.utils.CollectionUtils;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import timber.log.Timber;

/**
 * Created by Hoa Nguyen on Apr 22 2019.
 */
public class HMConfig {
    private Version version;
    private ArrayList<Menu> mainMenu;
    private ArrayList<Menu> subListMore;
    private ArrayList<Menu> subListMenuFolowUs;
    private ArrayList<Region> region;

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

    public static class Version {
        private String versionAndroid;
        private String versioniOS;
        private int status;
        @SerializedName("MainDomain")
        private String mainDomain;
        private String iconUrl;

        public String getVersionAndroid() {
            return versionAndroid;
        }

        public String getVersioniOS() {
            return versioniOS;
        }

        public int getStatus() {
            return status;
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

        public String getUrl() {
            if (isExternalURL()) {
                return url;
            }
            HMConfig config = LoadConfig.getInstance().load();
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
            HMConfig config = LoadConfig.getInstance().load();
            if (config != null) {
                try {
                    return config.version.getIconUrl() + iconName;
                } catch (Exception e) {
                    Timber.e(e);
                }
                return "";
            }
            return iconName;
        }


        public ArrayList<Menu> getSubListMenu() {
            return subListMenu;
        }

        public boolean isMoreMenu() {
            return "More".equals(name);
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
}
