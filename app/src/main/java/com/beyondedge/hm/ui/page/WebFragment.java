package com.beyondedge.hm.ui.page;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.beyondedge.hm.R;

import im.delight.android.webview.AdvancedWebView;

/**
 * Created by Hoa Nguyen on Apr 22 2019.
 */
public class WebFragment extends Fragment implements AdvancedWebView.Listener {
    AdvancedWebView myWebView;

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
        initView(view);
    }

    private void initView(View view) {
        myWebView = view.findViewById(R.id.webview);
        myWebView.setListener(getActivity(), this);
//        myWebView.setGeolocationEnabled(false);
        myWebView.setMixedContentAllowed(true);
        myWebView.setCookiesEnabled(true);
        myWebView.setThirdPartyCookiesEnabled(true);
        myWebView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                Toast.makeText(getActivity(), "Finished loading", Toast.LENGTH_SHORT).show();
            }

        });
        myWebView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                Toast.makeText(getActivity(), title, Toast.LENGTH_SHORT).show();
            }

        });
//        myWebView.addHttpHeader("X-Requested-With", "");
        loadPage("https://www.android.com");
    }

    public void loadPage(String url) {
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
}
