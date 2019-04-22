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

    private ArrayList<PageFragment> fragments = new ArrayList<>();
    private PageFragment currentFragment;

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);

        fragments.clear();
        fragments.add(PageFragment.newInstance(0));
        fragments.add(PageFragment.newInstance(1));
        fragments.add(PageFragment.newInstance(2));
        fragments.add(PageFragment.newInstance(3));
        fragments.add(PageFragment.newInstance(4));
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