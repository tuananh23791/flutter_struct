package com.beyondedge.hm;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationViewPager;
import com.aurelhubert.ahbottomnavigation.notification.AHNotification;
import com.beyondedge.hm.base.BaseTemplateActivity;
import com.beyondedge.hm.config.HMConfig;
import com.beyondedge.hm.config.LoadConfig;
import com.beyondedge.hm.config.TemplateMessage;
import com.beyondedge.hm.ui.page.PageInterface;
import com.beyondedge.hm.ui.page.ViewPagerAdapter;

public class MainActivity extends BaseTemplateActivity {
    /**
     * Show or hide the bottom navigation with animation
     */
    int height;
    private PageInterface currentFragment;
    private ViewPagerAdapter adapterViewPager;
    private AHBottomNavigation bottomNavigation;
    private AHBottomNavigationViewPager viewPager;
    private Handler handler = new Handler();
    private boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPager = findViewById(R.id.view_pager);

        settingBottomNavigation();
        initSearchView();
        initViewPager();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    public void onBackPressed() {
        if (!isHandledHideSearchBarInBackPressed()) {
            if (currentFragment != null) {
//                Stack<String> stackPage = currentFragment.getStackPage();
//                if (stackPage != null && !stackPage.isEmpty()) {
//                    String previousURL = stackPage.pop();
//                    currentFragment.goBack();
//
//                    Toast.makeText(this, previousURL, Toast.LENGTH_SHORT).show();
//                    return;
//                }

                if (currentFragment.goBack()) {
                    return;
                }
            }
//            super.onBackPressed();

            handledDoubleBackWarning();
        }
    }

    @Override
    public void updateTemplate(TemplateMessage templateMessage) {
        super.updateTemplate(templateMessage);

        handler.postDelayed(() -> {
            String cartCount = templateMessage != null ? String.valueOf(templateMessage.getCartCount()) : "";
            AHNotification notification = new AHNotification.Builder()
                    .setText(cartCount)
                    .setBackgroundColor(ContextCompat.getColor(bottomNavigation.getContext(), R.color.colorNotification))
                    .setTextColor(ContextCompat.getColor(bottomNavigation.getContext(), R.color.colorNotificationText))
                    .build();
            bottomNavigation.setNotification(notification, ViewPagerAdapter.MENU_CHECKOUT);

        }, 500);
    }

    private void handledDoubleBackWarning() {
        if (doubleBackToExitPressedOnce) {
            finish();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler()
                .postDelayed(
                        () -> doubleBackToExitPressedOnce = false,
                        2000);
    }

    private void initViewPager() {
        viewPager.setOffscreenPageLimit(4);
        adapterViewPager = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapterViewPager);

        viewPager.post(() -> {
            currentFragment = adapterViewPager.getCurrentFragment();
            currentFragment.willBeDisplayed();
        });
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

        HMConfig config = LoadConfig.getInstance(this).load();
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

        bottomNavigation.post(new Runnable() {
            @Override
            public void run() {
                height = bottomNavigation.getHeight();
            }
        });

    }

    public boolean isCheckOutTab() {
        return bottomNavigation != null && bottomNavigation.getCurrentItem() == ViewPagerAdapter.MENU_CHECKOUT;
    }

    public void showOrHideBottomNavigation(boolean show) {
        if (bottomNavigation != null) {
            if (show) {
//                bottomNavigation.restoreBottomNavigation(true);

                ViewCompat.animate(bottomNavigation)
                        .translationY(0)
                        .setInterpolator(new LinearOutSlowInInterpolator())
                        .setDuration(300)
                        .withStartAction(new Runnable() {
                            @Override
                            public void run() {
                                bottomNavigation.setVisibility(View.VISIBLE);
                            }
                        })
                        .start();

            } else {
//                bottomNavigation.hideBottomNavigation(true);

                ViewCompat.animate(bottomNavigation)
                        .translationY(height)
                        .setInterpolator(new LinearOutSlowInInterpolator())
                        .setDuration(300)
                        .withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                bottomNavigation.setVisibility(View.GONE);
                            }
                        })
                        .start();
            }
        }
    }
}
