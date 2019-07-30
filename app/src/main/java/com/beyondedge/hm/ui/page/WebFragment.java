package com.beyondedge.hm.ui.page;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.HttpAuthHandler;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityOptionsCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.beyondedge.hm.BuildConfig;
import com.beyondedge.hm.MainActivity;
import com.beyondedge.hm.R;
import com.beyondedge.hm.base.BaseFragment;
import com.beyondedge.hm.base.BaseTemplateActivity;
import com.beyondedge.hm.config.HMConfig;
import com.beyondedge.hm.config.LoadConfig;
import com.beyondedge.hm.config.TemplateMessage;
import com.beyondedge.hm.ui.screen.PageWebActivity;
import com.beyondedge.hm.utils.PrefManager;
import com.beyondedge.hm.utils.URLUtils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import im.delight.android.webview.AdvancedWebView;
import timber.log.Timber;

/**
 * Created by Hoa Nguyen on Apr 22 2019.
 */
public abstract class WebFragment extends BaseFragment implements AdvancedWebView.Listener {
    protected boolean isDisplaying = false;
    protected TemplateMessage templateMessage;
    Boolean isShowTemplate;
    private AdvancedWebView myWebView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView textInfo;
    private ProgressBar progressHorizontal;

    private List<String> mExternalHostnames;

    private static boolean isHostnameAllowed(final List<String> listHostNames, final String url) {
        if (listHostNames.size() == 0) {
            return true;
        }

        final String actualHost = Uri.parse(url).getHost();

        if (!TextUtils.isEmpty(actualHost)) {
            for (String expectedHost : listHostNames) {
                assert actualHost != null;
                if (actualHost.equals(expectedHost) || actualHost.endsWith("." + expectedHost)) {
                    return true;
                }
            }
        }

        return false;
    }

    public abstract void refreshRootPage();

    protected void setDisplaying(boolean displaying) {
        isDisplaying = displaying;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.web_page_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        textInfo = view.findViewById(R.id.textInfo);
        progressHorizontal = view.findViewById(R.id.progress_horizontal);
        textInfo.setVisibility(View.GONE);
        initView(view);
    }

    protected boolean goBack() {
        if (myWebView != null && myWebView.canGoBack()) {
            myWebView.goBack();
            return true;
        }

        return false;
    }

    protected boolean canGoBack() {
        FragmentActivity activity = getActivity();
        if (activity instanceof PageWebActivity) {
            return true;
        }
        return myWebView != null && myWebView.canGoBack();
    }

    protected String getOriginalURL() {
        if (myWebView != null) {
            String originalUrl = myWebView.getOriginalUrl();
            return originalUrl != null ? originalUrl : "";
        }

        return "";
    }

    protected void handleTemplateUpdate() {
        Activity activity = getActivity();
        if (activity instanceof BaseTemplateActivity) {
            if (templateMessage == null) {
                templateMessage = TemplateMessage.fromJson("");
            }
            BaseTemplateActivity baseTemplateActivity = (BaseTemplateActivity) activity;
            baseTemplateActivity.setWebPageCanGoBack(canGoBack());
            baseTemplateActivity.updateTemplate(templateMessage);
        }
    }

    private void initView(View view) {
        mExternalHostnames = new ArrayList<>();
        mExternalHostnames.add("facebook.com");
        mExternalHostnames.add("line.me");
        mExternalHostnames.add("twitter.com");
        mExternalHostnames.add("instagram.com");
        mExternalHostnames.add("youtube.com");

        myWebView = view.findViewById(R.id.webview);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                myWebView.reload();
            }
        });
        myWebView.setListener(getActivity(), this);
//        myWebView.setGeolocationEnabled(false);
        myWebView.setMixedContentAllowed(true);
//        myWebView.setCookiesEnabled(true);
//        myWebView.setThirdPartyCookiesEnabled(true);

        WebSettings settings = myWebView.getSettings();
        settings.setAppCacheEnabled(false);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setJavaScriptEnabled(true);

        HMConfig config = LoadConfig.getInstance().load();
//        ArrayList<String> innerHosts = config.getPaymentUrlOpenInApp();
        String mainDomain = config.getVersion().getMainDomain();

        addInternalHost(mainDomain);
//        if (CollectionUtils.isNotEmpty(innerHosts)) {
//            for (String innerHost : innerHosts) {
//                addInternalHost(innerHost);
//            }
//        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (0 != (getActivity().getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE)) {
                WebView.setWebContentsDebuggingEnabled(true);
            }
        }

        myWebView.setWebViewClient(new WebViewClient() {

//            @Override
//            public void onPageStarted(WebView view, String url, Bitmap favicon) {
//                List<String> permittedHostnames = myWebView.getPermittedHostnames();
//                if (isHostnameAllowed(permittedHostnames, url)) {
//                    super.onPageStarted(view, url, favicon);
//                } else {
//                    //open external
//
//                    URLUtils.openInWebBrowser(myWebView.getContext(), url);
//                }
//            }


            @Override
            public void onPageCommitVisible(WebView view, String url) {
                super.onPageCommitVisible(view, url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
//                Toast.makeText(getActivity(), "Finished loading", Toast.LENGTH_SHORT).show();

                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onReceivedHttpAuthRequest(WebView view, HttpAuthHandler handler, String host, String realm) {
//                if ("https://hmthdev4.specom.io".contains(host)
////                        || "http://hm-media.s3-ap-southeast-1.amazonaws.com".contains(host)
//                ) {
//                    handler.proceed("devenv", "dev@singpost");
//                }

                handler.proceed("devenv", "dev@singpost");
                super.onReceivedHttpAuthRequest(view, handler, host, realm);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return super.shouldOverrideUrlLoading(view, url);
            }
        });
        myWebView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
//                FragmentActivity activity = getActivity();
//
//                if (activity != null && activity instanceof BaseActivity) {
//                    ((BaseActivity) activity).setTitleToolbar(title);
//                }
            }

            @Override
            public void onProgressChanged(WebView view, int progress) {
                if (progress < 100 && progressHorizontal.getVisibility() == ProgressBar.GONE) {
                    progressHorizontal.setVisibility(ProgressBar.VISIBLE);
                }

                progressHorizontal.setProgress(progress);
                if (progress >= 100) {
                    progressHorizontal.setVisibility(ProgressBar.GONE);
                }
            }

        });
//        myWebView.addHttpHeader("X-Requested-With", "");
//        loadPage("https://www.hm.com/vn/");

//        myWebView.addJavascriptInterface(new WebAppInterface(myWebView.getContext()), "nativeJs");
        myWebView.addJavascriptInterface(new WebAppInterface(myWebView.getContext()), "Android");

    }

    private void addInternalHost(String fullUrl) {
        if (!fullUrl.startsWith("http")) {
            fullUrl = "https://" + fullUrl;
        }
        URL url = null;
        try {
            url = new URL(fullUrl);
            String host = url.getHost();
            myWebView.addPermittedHostname(host);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public void loadPage(String url) {
        myWebView.loadUrl(url);
    }

    private void showDebugData(String url) {
        if (BuildConfig.DEBUG && BuildConfig.TEMPLATE && textInfo != null) {
            if (isShowTemplate == null) {
                isShowTemplate = PrefManager.getInstance().getCheatingShowHideTemplate();
            }

            if (isShowTemplate) {
                textInfo.setVisibility(View.VISIBLE);
                StringBuilder builder = new StringBuilder();
                builder.append("URL = ");
                builder.append(url);
                builder.append("\n\n");
                if (templateMessage != null) {
                    builder.append(templateMessage.toString());
                } else {
                    builder.append("TemplateMessage [null]");
                }

                textInfo.setText(builder.toString());
            }
        }
    }

    @SuppressLint("NewApi")
    @Override
    public void onResume() {
        super.onResume();
        myWebView.onResume();
    }

    @SuppressLint("NewApi")
    @Override
    public void onPause() {
        myWebView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        myWebView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        myWebView.onActivityResult(requestCode, resultCode, intent);
    }

    @Override
    public void onPageStarted(String url, Bitmap favicon) {
//        testJavascript(myWebView);

        Timber.d("TemplateMessage onPageStarted %s", url);

    }

    @Override
    public void onPageFinished(String url) {
        showDebugData(url);
        Timber.d("TemplateMessage onPageFinished %s", url);
        if (isDisplaying) {
            handleTemplateUpdate();
        }
    }

    @Override
    public void onPageError(int errorCode, String description, String failingUrl) {
    }

    @Override
    public void onDownloadRequested(String url, String suggestedFilename, String mimeType, long contentLength, String contentDisposition, String userAgent) {
    }

    //-----

    @Override
    public void onExternalPageRequest(String url) {
        if (isHostnameAllowed(mExternalHostnames, url)) {
            URLUtils.openInWebBrowser(myWebView.getContext(), url);
        } else {
            loadPage(url);
        }
    }

    private void testJavascript(WebView webView) {
        webView.loadUrl(
                "javascript:( " +
                        "function () {" +
                        "         var message = {\n" +
                        "            \"page_title\": 'Product Category',\n" +
                        "            \"page_template\": \"prod_cat\",\n" +
                        "            \"cart_count\": 1,\n" +
                        "            \n" +
                        "        };\n" +
                        "        try {\n" +
                        "            window.Android.postMessage(JSON.stringify(message));\n" +
                        "        }\n" +
                        "        catch (e) {\n" +
                        "        }\n" +
                        "\n" +
                        "        try {\n" +
                        "            webkit.messageHandlers.process.postMessage(message);\n" +
                        "        }\n" +
                        "        catch (e) {\n" +
                        "        } " +
                        "} ) ()"
        );
    }

    // Create an interface for validating int types
    public @interface WebTypeDef {
    }

    public class WebAppInterface {
        Context mContext;

        /**
         * Instantiate the interface and set the context
         */
        WebAppInterface(Context c) {
            mContext = c;
        }

        /**
         * Show a toast from the web page
         */
        @JavascriptInterface
        public void postMessage(String message) {
            handleMessage(message);
        }

//        /**
//         * Show a toast from the web page
//         */
//        @JavascriptInterface
//        public void process(String message) {
//            handleMessage(message);
//        }

        private void handleMessage(String message) {
            TemplateMessage localTemplateMessage = TemplateMessage.fromJson(message);

            if (localTemplateMessage == null || TextUtils.isEmpty(localTemplateMessage.getPageTemplate())) {
                Timber.i("TemplateMessage => null");
                return;
            }

//            if (BuildConfig.FLAVOR.equals("dev") && localTemplateMessage.getCartCount() == 2) {
//                localTemplateMessage = TemplateMessage.fakeLogout();
//            }

            if (localTemplateMessage.isLogout()) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                Bundle bundle = ActivityOptionsCompat.makeCustomAnimation(getContext(),
                        android.R.anim.fade_in, android.R.anim.fade_out).toBundle();
                startActivity(intent, bundle);
                return;
            }

            if (localTemplateMessage.isCheckoutOK()) {
                refreshRootPage();
                return;
            }

            templateMessage = localTemplateMessage;
            Timber.i("TemplateMessage => \n%s", WebFragment.this.templateMessage.toString());
            //TODO comment code to test

            if (isDisplaying) {
//                Toast.makeText(mContext, WebFragment.this.templateMessage.toString(), Toast.LENGTH_SHORT).show();
                FragmentActivity activity = getActivity();

                //update template for change Title/Search toolbar setting
                if (activity instanceof BaseTemplateActivity) {
                    ((BaseTemplateActivity) activity).updateTemplate(WebFragment.this.templateMessage);
                }
            } else {
                //will handled in next active page
            }
        }

        /**
         * <script type="text/javascript">
         *         var message = {
         *             "page_title": 'Shopping Cart',
         *             "page_template": 'Shopping Cart',
         *             "cart_count": 1,
         *             "share_page_url": 'https://beyondedge.com.sg'
         *         };
         *         try {
         *             // Android
         *             window.Android.postMessage(JSON.stringify(message));}
         *         }
         *         catch (e) {
         *         }
         *
         *         try {
         *             // iOS
         *             webkit.messageHandlers.process.postMessage(message);
         *         }
         *         catch (e) {
         *         }
         *     </script>
         */

    }
}
