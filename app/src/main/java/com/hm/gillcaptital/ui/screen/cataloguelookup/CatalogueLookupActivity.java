package com.hm.gillcaptital.ui.screen.cataloguelookup;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.hm.gillcaptital.R;
import com.hm.gillcaptital.base.BaseActivitySingleFragment;

/**
 * Created by Hoa Nguyen on Apr 24 2019.
 */
public class CatalogueLookupActivity extends BaseActivitySingleFragment {

    @Override
    protected Fragment createFragment() {
        return CatalogueLookupFragment.newInstance();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        enableBackButtonToolbar(null/*default finish*/);
        setTitleToolbar(getString(R.string.look_up_title));
    }
}
