package com.beyondedge.hm.ui.page;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by Hoa Nguyen on Apr 22 2019.
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {
    public static final int MENU_HOME = 0;
    public static final int MENU_PROFILE = 1;
    public static final int MENU_FAVORITE = 2;
    public static final int MENU_CART = 3;
    public static final int MENU_MORE = 4;

    private ArrayList<PageFragment> fragments = new ArrayList<>();
    private PageFragment currentFragment;

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);

        fragments.clear();
        fragments.add(PageFragment.newInstance(MENU_HOME));
        fragments.add(PageFragment.newInstance(MENU_PROFILE));
        fragments.add(PageFragment.newInstance(MENU_FAVORITE));
        fragments.add(PageFragment.newInstance(MENU_CART));
        fragments.add(PageFragment.newInstance(MENU_MORE));
    }

    @Override
    public PageFragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public void setPrimaryItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        if (getCurrentFragment() != object) {
            currentFragment = ((PageFragment) object);
        }
        super.setPrimaryItem(container, position, object);
    }

    /**
     * Get the current fragment
     */
    public PageFragment getCurrentFragment() {
        return currentFragment;
    }
}