package com.beyondedge.hm.ui.screen;

import androidx.fragment.app.Fragment;

import com.beyondedge.hm.base.BaseActivitySingleFragment;

/**
 * Created by Hoa Nguyen on Apr 25 2019.
 */
public class SettingsActivity extends BaseActivitySingleFragment {
    @Override
    protected Fragment createFragment() {
        return SettingsFragment.newInstance();
    }
}
