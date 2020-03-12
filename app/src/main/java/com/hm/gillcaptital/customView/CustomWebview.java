package com.hm.gillcaptital.customView;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

import com.hm.gillcaptital.interfaces.OnScrollChangedListener;

public class CustomWebview extends WebView {
    OnScrollChangedListener onScrollChangedListener;

    public CustomWebview(Context context) {
        super(context);
    }

    public CustomWebview(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomWebview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (onScrollChangedListener != null)
            onScrollChangedListener.onScrollChanged(l, t, oldl, oldt);
    }

    public void setOnScrollChangedListener(OnScrollChangedListener onScrollChangedListener){
        this.onScrollChangedListener = onScrollChangedListener;
    }
}
