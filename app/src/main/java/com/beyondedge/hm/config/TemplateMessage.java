package com.beyondedge.hm.config;

/**
 * Created by Hoa Nguyen on May 02 2019.
 */

import android.text.TextUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.beyondedge.hm.HMApplication;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.SerializedName;

import java.util.Locale;

import timber.log.Timber;

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
    public static final String CART_COUNT_UNDEFINE = "0";
    public static final String HOME = "homepage";
    public static final String PROD_CAT = "prod_cat";
    public static final String PROD_DETAIL = "prod_detail";
    public static final String ACCOUNT = "account";
    public static final String CHECKOUT = "checkout";

    @SerializedName("page_title")
    private String pageTitle;
    @SerializedName("page_template")
    private String pageTemplate;
    @SerializedName("cart_count")
    private String cartCount;
    @SerializedName("share_page_url")
    private String sharePageUrl;

    private TemplateMessage(String pageTitle, String pageTemplate, String cartCount, String sharePageUrl) {
        this.pageTitle = pageTitle;
        this.pageTemplate = pageTemplate;
        this.cartCount = cartCount;
        this.sharePageUrl = sharePageUrl;
    }

    public static TemplateMessage fromJson(String json) {
        if (TextUtils.isEmpty(json)) {
            return new TemplateMessage("", HOME, CART_COUNT_UNDEFINE, "");
        }

        try {
            return HMApplication.getGson().fromJson(json, TemplateMessage.class);
        } catch (JsonSyntaxException e) {
//            Toast.makeText(HMApplication.getInstance(), e.toString(), Toast.LENGTH_SHORT).show();
            Timber.e(e.toString());
        }

        return new TemplateMessage("", HOME, CART_COUNT_UNDEFINE, "");
    }

    public boolean isHome() {
        return HOME.equals(this.pageTemplate);
    }

    @NonNull
    @Override
    public String toString() {
        return String.format(Locale.getDefault(), "[title=%s \ntemplate=%s \ncart=%s \nshare=%s]", pageTitle, pageTemplate, cartCount, sharePageUrl);
    }

    public String getPageTitle() {
        return pageTitle;
    }

    public String getPageTemplate() {
        return pageTemplate;
    }

    public int getCartCount() {
        try {
            return Integer.valueOf(cartCount);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public String getSharePageUrl() {
        return sharePageUrl;
    }
}
