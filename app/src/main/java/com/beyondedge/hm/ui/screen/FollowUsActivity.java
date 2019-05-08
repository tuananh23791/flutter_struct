package com.beyondedge.hm.ui.screen;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.beyondedge.hm.base.BaseActivitySingleFragment;

/**
 * Created by Hoa Nguyen on Apr 24 2019.
 */
public class FollowUsActivity extends BaseActivitySingleFragment {
    @Override
    protected Fragment createFragment() {
        return FollowUsFragment.newInstance();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        enableBackButtonToolbar(null/*default finish*/);
    }
}
