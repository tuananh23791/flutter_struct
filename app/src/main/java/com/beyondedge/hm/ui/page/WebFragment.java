package com.beyondedge.hm.ui.page;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.beyondedge.hm.BuildConfig;
import com.beyondedge.hm.R;
import com.beyondedge.hm.base.BaseFragment;
import com.beyondedge.hm.base.BaseTemplateActivity;
import com.beyondedge.hm.config.TemplateMessage;

import im.delight.android.webview.AdvancedWebView;

/**
 * Created by Hoa Nguyen on Apr 22 2019.
 */
public abstract class WebFragment extends BaseFragment implements AdvancedWebView.Listener {
    protected boolean isDisplaying = false;
    protected TemplateMessage templateMessage;

    private AdvancedWebView myWebView;
    private TextView textInfo;
    private ProgressBar progressHorizontal;

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

    protected String getOriginalURL() {
        if (myWebView != null) {
            String originalUrl = myWebView.getOriginalUrl();
            return originalUrl != null ? originalUrl : "";
        }

        return "";
    }

    private void initView(View view) {
        myWebView = view.findViewById(R.id.webview);
        myWebView.setListener(getActivity(), this);
//        myWebView.setGeolocationEnabled(false);
        myWebView.setMixedContentAllowed(true);
//        myWebView.setCookiesEnabled(true);
//        myWebView.setThirdPartyCookiesEnabled(true);

        WebSettings settings = myWebView.getSettings();
        settings.setAppCacheEnabled(false);
        settings.setJavaScriptEnabled(true);

        myWebView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
//                Toast.makeText(getActivity(), "Finished loading", Toast.LENGTH_SHORT).show();
            }

        });
        myWebView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
//                Toast.makeText(getActivity(), title, Toast.LENGTH_SHORT).show();

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

        myWebView.addJavascriptInterface(new WebAppInterface(myWebView.getContext()), "nativeJs");
        myWebView.addJavascriptInterface(new WebAppInterface(myWebView.getContext()), "Android");
    }

    public void loadPage(String url) {
//        if (BuildConfig.DEBUG && BuildConfig.LOG && textInfo != null) {
//            textInfo.setVisibility(View.VISIBLE);
//            textInfo.setText(url);
//        }
        myWebView.loadUrl(url);
    }

    @SuppressLint("NewApi")
    @Override
    public void onResume() {
        super.onResume();
        myWebView.onResume();
        // ...
    }

    @SuppressLint("NewApi")
    @Override
    public void onPause() {
        myWebView.onPause();
        // ...
        super.onPause();
    }

    @Override
    public void onDestroy() {
        myWebView.onDestroy();
        // ...
        super.onDestroy();
    }

    //TODO onActivityResult on Activity
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        myWebView.onActivityResult(requestCode, resultCode, intent);
        // ...
    }

    @Override
    public void onPageStarted(String url, Bitmap favicon) {
//        testJavascript(myWebView);

        if (BuildConfig.DEBUG && BuildConfig.LOG && textInfo != null) {
            textInfo.setVisibility(View.VISIBLE);
            textInfo.setText(url);
        }
    }

    @Override
    public void onPageFinished(String url) {
    }

    @Override
    public void onPageError(int errorCode, String description, String failingUrl) {
    }

    @Override
    public void onDownloadRequested(String url, String suggestedFilename, String mimeType, long contentLength, String contentDisposition, String userAgent) {
    }

    @Override
    public void onExternalPageRequest(String url) {
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

        /**
         * Show a toast from the web page
         */
        @JavascriptInterface
        public void process(String message) {
            handleMessage(message);
        }

        private void handleMessage(String message) {
            templateMessage = TemplateMessage.fromJson(message);

            if (isDisplaying) {
                Toast.makeText(mContext, templateMessage.toString(), Toast.LENGTH_SHORT).show();
                FragmentActivity activity = getActivity();

                //update template for change Title/Search toolbar setting
                if (activity instanceof BaseTemplateActivity) {
                    ((BaseTemplateActivity) activity).updateTemplate(templateMessage);
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
         *             nativeJs.process(message);
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
