package com.beyondedge.hm.base;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.beyondedge.hm.R;

/**
 * Created by Hoa Nguyen on Apr 24 2019.
 */
public abstract class BaseActivitySingleFragment extends BaseTemplateActivity {
    private Fragment currentFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_single_fragment);
        initSearchView();
        currentFragment = createFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.frame_content, currentFragment, "Single_Fragment")
                .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                .commit();
    }

    protected abstract Fragment createFragment();

    public Fragment getCurrentFragment() {
        return currentFragment;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        currentFragment = null;
    }
}
