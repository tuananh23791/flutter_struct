package com.beyondedge.hm;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationViewPager;
import com.aurelhubert.ahbottomnavigation.notification.AHNotification;
import com.beyondedge.hm.base.BaseTemplateActivity;
import com.beyondedge.hm.config.HMConfig;
import com.beyondedge.hm.config.LoadConfig;
import com.beyondedge.hm.ui.page.PageInterface;
import com.beyondedge.hm.ui.page.ViewPagerAdapter;

public class MainActivity extends BaseTemplateActivity {
    private PageInterface currentFragment;
    private ViewPagerAdapter adapterViewPager;
    private AHBottomNavigation bottomNavigation;
    private AHBottomNavigationViewPager viewPager;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPager = findViewById(R.id.view_pager);

        initSearchView();
        initViewPager();
        settingBottomNavigation();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }

    private void initViewPager() {
        viewPager.setOffscreenPageLimit(4);
        adapterViewPager = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapterViewPager);

        currentFragment = adapterViewPager.getCurrentFragment();

        //TODO
        handler.postDelayed(() -> {
            AHNotification notification = new AHNotification.Builder()
                    .setText("100")
                    .setBackgroundColor(ContextCompat.getColor(bottomNavigation.getContext(), R.color.colorNotification))
                    .setTextColor(ContextCompat.getColor(bottomNavigation.getContext(), R.color.colorNotificationText))
                    .build();
            bottomNavigation.setNotification(notification, ViewPagerAdapter.MENU_CART);

        }, 2000);
    }


    private void settingBottomNavigation() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        }

        bottomNavigation = findViewById(R.id.bottom_navigation);

        // Create items
        AHBottomNavigationItem item1 = new AHBottomNavigationItem("", R.drawable.ic_menu_hm);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem("", R.drawable.ic_menu_profile);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem("", R.drawable.ic_menu_favorite);
        AHBottomNavigationItem item4 = new AHBottomNavigationItem("", R.drawable.ic_menu_cart);
        AHBottomNavigationItem item5 = new AHBottomNavigationItem("", R.drawable.ic_menu_more);

        // Add items
        bottomNavigation.addItem(item1);
        bottomNavigation.addItem(item2);
        bottomNavigation.addItem(item3);
        bottomNavigation.addItem(item4);
        bottomNavigation.addItem(item5);

        // Disable the translation inside the CoordinatorLayout
        bottomNavigation.setBehaviorTranslationEnabled(true);

        // Manage titles
        bottomNavigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_HIDE);

        // Use colored navigation with circle reveal effect
        //        bottomNavigation.setColored(true);

        // Set current item programmatically
        bottomNavigation.setAccentColor(ContextCompat.getColor(bottomNavigation.getContext(), R.color.colorActive));
        bottomNavigation.setInactiveColor(ContextCompat.getColor(bottomNavigation.getContext(), R.color.colorInActive));

        bottomNavigation.setCurrentItem(0);
        bottomNavigation.post(() -> setSearchType(SEARCH_TYPE_FULL_TOOLBAR));

        HMConfig config = LoadConfig.getInstance().load();
        setTitleToolbar(config.getMainMenuList().get(0).getName());

        // OR
//        AHNotification notification = new AHNotification.Builder()
//                .setText("100")
//                .setBackgroundColor(ContextCompat.getColor(bottomNavigation.getContext(), R.color.colorNotification))
//                .setTextColor(ContextCompat.getColor(bottomNavigation.getContext(), R.color.colorNotificationText))
//                .build();
//        bottomNavigation.setNotification(notification, 1);

        // Enable / disable item & set disable color
        //        bottomNavigation.enableItemAtPosition(2);
        //        bottomNavigation.disableItemAtPosition(2);
        //        bottomNavigation.setItemDisableColor(Color.parseColor("#3A000000"));

        // Set listeners
        bottomNavigation.setOnTabSelectedListener((position, wasSelected) -> {

            if (currentFragment == null) {
                currentFragment = adapterViewPager.getCurrentFragment();
            }

            if (wasSelected) {
                currentFragment.refresh();
                return true;
            }

            if (currentFragment != null) {
                currentFragment.willBeHidden();
            }

            viewPager.setCurrentItem(position, false);

            if (currentFragment == null) {
                return true;
            }

            currentFragment = adapterViewPager.getCurrentFragment();
            currentFragment.willBeDisplayed();

            //TODO
//            setSearchType(position == ViewPagerAdapter.MENU_HOME ? SEARCH_TYPE_FULL_TOOLBAR : SEARCH_TYPE_HIDE_ALL);

            if (position == ViewPagerAdapter.MENU_MORE) {
                setSearchType(SEARCH_TYPE_HIDE_ALL);
            }
            return true;
        });

    }

    /**
     * Show or hide the bottom navigation with animation
     */
    public void showOrHideBottomNavigation(boolean show) {
        if (show) {
            bottomNavigation.restoreBottomNavigation(true);
        } else {
            bottomNavigation.hideBottomNavigation(true);
        }
    }
}
