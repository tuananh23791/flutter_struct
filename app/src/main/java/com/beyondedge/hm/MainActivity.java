package com.beyondedge.hm;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationViewPager;
import com.aurelhubert.ahbottomnavigation.notification.AHNotification;
import com.beyondedge.hm.base.BaseSearchLibActivity;
import com.beyondedge.hm.ui.page.PageFragment;
import com.beyondedge.hm.ui.page.ViewPagerAdapter;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends BaseSearchLibActivity {
    //    private TextView mTextMessage;
    private PageFragment currentFragment;
    private ViewPagerAdapter adapterViewPager;
    private AHBottomNavigation bottomNavigation;
    private AHBottomNavigationViewPager viewPager;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        mTextMessage = findViewById(R.id.message);
        viewPager = findViewById(R.id.view_pager);

        Toolbar toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        settingBottomNavigation();
        initSearchView();
        initViewPager();
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

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                AHNotification notification = new AHNotification.Builder()
                        .setText("100")
                        .setBackgroundColor(ContextCompat.getColor(bottomNavigation.getContext(), R.color.colorNotification))
                        .setTextColor(ContextCompat.getColor(bottomNavigation.getContext(), R.color.colorNotificationText))
                        .build();
                bottomNavigation.setNotification(notification, 1);

                Snackbar.make(bottomNavigation, "App - ReLoad",
                        Snackbar.LENGTH_SHORT).show();

            }
        }, 3000);
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
        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {

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

                return true;
            }
        });

//        bottomNavigation.setOnNavigationPositionListener(new AHBottomNavigation.OnNavigationPositionListener() {
//            @Override
//            public void onPositionChange(int pos) {
//                switch (pos) {
//                    case 0:
//                        mTextMessage.setText(R.string.title_home);
//                        break;
//                    case 1:
//                        mTextMessage.setText(R.string.title_dashboard);
//                        break;
//                    case 2:
//                        mTextMessage.setText(R.string.title_notifications);
//                        break;
//                }
//            }
//        });
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
