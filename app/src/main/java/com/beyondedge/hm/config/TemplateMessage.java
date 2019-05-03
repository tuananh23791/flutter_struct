package com.beyondedge.hm.config;

/**
 * Created by Hoa Nguyen on May 02 2019.
 */

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.beyondedge.hm.HMApplication;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.SerializedName;

import java.util.Locale;

/**
 * var message = {
 * "pageTitle": 'Home',
 * "page_template": 'home',
 * "cart_count": 1,
 * "share_page_url": 'https://beyondedge.com.sg'
 * };
 * try {
 * window.Android.postMessage(JSON.stringify(message));
 * }
 * catch (e) {
 * }
 * <p>
 * try {
 * webkit.messageHandlers.process.postMessage(message);
 * }
 * catch (e) {
 * }
 */
public class TemplateMessage {
    public static final int CART_COUNT_UNDEFINE = -1;
    public static final String HOME = "home";
    public static final String PROD_CAT = "prod_cat";
    public static final String PROD_DETAIL = "prod_detail";
    public static final String ACCOUNT = "account";
    public static final String CHECKOUT = "checkout";

    @SerializedName("page_title")
    private String pageTitle;
    @SerializedName("page_template")
    private String pageTemplate;
    @SerializedName("cart_count")
    private int cartCount;
    @SerializedName("share_page_url")
    private String sharePageUrl;

    private TemplateMessage(String pageTitle, String pageTemplate, int cartCount, String sharePageUrl) {
        this.pageTitle = pageTitle;
        this.pageTemplate = pageTemplate;
        this.cartCount = cartCount;
        this.sharePageUrl = sharePageUrl;
    }

    public static TemplateMessage fromJson(String json) {
        if (TextUtils.isEmpty(json)) {
            return new TemplateMessage(HOME.toUpperCase(), HOME, CART_COUNT_UNDEFINE, "");
        }

        try {
            return HMApplication.getGson().fromJson(json, TemplateMessage.class);
        } catch (JsonSyntaxException e) {

        }

        return new TemplateMessage(HOME.toUpperCase(), HOME, CART_COUNT_UNDEFINE, "");
    }

    @NonNull
    @Override
    public String toString() {
        return String.format(Locale.getDefault(), "[title=%s \ntemplate=%s \ncart=%d \nshare=%s]", pageTitle, pageTemplate, cartCount, sharePageUrl);
    }

    public String getPageTitle() {
        return pageTitle;
    }

    public String getPageTemplate() {
        return pageTemplate;
    }

    public int getCartCount() {
        return cartCount;
    }

    public String getSharePageUrl() {
        return sharePageUrl;
    }
}
