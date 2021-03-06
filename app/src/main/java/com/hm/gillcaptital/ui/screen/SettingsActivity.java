package com.hm.gillcaptital.ui.screen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.hm.gillcaptital.R;
import com.hm.gillcaptital.base.BaseActivitySingleFragment;

/**
 * Created by Hoa Nguyen on Apr 25 2019.
 */
public class SettingsActivity extends BaseActivitySingleFragment {
    public static final String EXTRA_TITLE = "EXTRA_TITLE";

    public static void startScreen(Activity from, String title){
        Intent intent = new Intent(from,SettingsActivity.class);
        intent.putExtra(EXTRA_TITLE,title);

        from.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        enableBackButtonToolbar(null/*default finish*/);

        Intent intent = getIntent();
        String stringExtra = intent.getStringExtra(EXTRA_TITLE);
        if (TextUtils.isEmpty(stringExtra)) {
            stringExtra = getString(R.string.app_name);
        }
        setTitleToolbar(stringExtra);
    }

    @Override
    protected Fragment createFragment() {
        return SettingsFragment.Companion.newInstance();
    }
}
