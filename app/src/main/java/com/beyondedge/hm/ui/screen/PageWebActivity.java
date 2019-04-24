package com.beyondedge.hm.ui.screen;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.beyondedge.hm.R;
import com.beyondedge.hm.base.BaseActivitySingleFragment;
import com.beyondedge.hm.base.EmptyFragment;
import com.beyondedge.hm.utils.URLUtils;

/**
 * Created by Hoa Nguyen on Apr 24 2019.
 */
public class PageWebActivity extends BaseActivitySingleFragment {
    public static final String EXTRA_URL = "EXTRA_URL";
    public static final String EXTRA_TITLE = "EXTRA_TITLE";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        enableBackButtonToolbar();

        Intent intent = getIntent();
        String stringExtra = intent.getStringExtra(EXTRA_TITLE);
        if (TextUtils.isEmpty(stringExtra)) {
            stringExtra = getString(R.string.app_name);
        }
        setTitleToolbar(stringExtra);
    }

    @Override
    protected Fragment createFragment() {
        String url = getIntent().getStringExtra(EXTRA_URL);
        if (URLUtils.isURLValid(url)) {
            return ScreenWebFragment.newInstance(url);
        } else {
            showAlertDialog("URL invalid", false, (dialog, which) -> finish());
            return EmptyFragment.newInstance(getString(R.string.load_failed));
        }
    }
}
