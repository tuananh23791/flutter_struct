package com.hm.gillcaptital.ui.screen;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hm.gillcaptital.ui.page.WebFragment;

/**
 * Created by Hoa Nguyen on Apr 24 2019.
 */
public class ScreenWebFragment extends WebFragment {
    public static final String EXTRA_URL = "EXTRA_URL";
    private String mURL = "";

    public static ScreenWebFragment newInstance(String url) {

        Bundle args = new Bundle();
        args.putString(EXTRA_URL, url);
        ScreenWebFragment fragment = new ScreenWebFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void refreshRootPage() {
        setDisplaying(true);
        loadPage(mURL);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            mURL = bundle.getString(EXTRA_URL);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        refreshRootPage();
    }
}
