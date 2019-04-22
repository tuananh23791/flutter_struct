package com.beyondedge.hm;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.aurelhubert.ahbottomnavigation.notification.AHNotification;
import com.beyondedge.hm.base.BaseSearchLibActivity;

public class MainActivity extends BaseSearchLibActivity {
    private TextView mTextMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextMessage = findViewById(R.id.message);
        Toolbar toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        settingBottomNavigation();
        initSearchView();
    }


    private void settingBottomNavigation() {

        AHBottomNavigation bottomNavigation = findViewById(R.id.bottom_navigation);

        // Create items
        AHBottomNavigationItem item1 = new AHBottomNavigationItem("", R.drawable.ic_menu_hm);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem("", R.drawable.ic_home_black_24dp);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem("", R.drawable.ic_dashboard_black_24dp);
        AHBottomNavigationItem item4 = new AHBottomNavigationItem("", R.drawable.ic_notifications_black_24dp);
        AHBottomNavigationItem item5 = new AHBottomNavigationItem("", R.drawable.ic_notifications_black_24dp);

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
        AHNotification notification = new AHNotification.Builder()
                .setText("100")
                .setBackgroundColor(ContextCompat.getColor(bottomNavigation.getContext(), R.color.colorNotification))
                .setTextColor(ContextCompat.getColor(bottomNavigation.getContext(), R.color.colorNotificationText))
                .build();
        bottomNavigation.setNotification(notification, 1);

        // Enable / disable item & set disable color
        //        bottomNavigation.enableItemAtPosition(2);
        //        bottomNavigation.disableItemAtPosition(2);
        //        bottomNavigation.setItemDisableColor(Color.parseColor("#3A000000"));

        // Set listeners
        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                // Do something cool here...
                return true;
            }
        });
        bottomNavigation.setOnNavigationPositionListener(new AHBottomNavigation.OnNavigationPositionListener() {
            @Override
            public void onPositionChange(int pos) {
                switch (pos) {
                    case 0:
                        mTextMessage.setText(R.string.title_home);
                        break;
                    case 1:
                        mTextMessage.setText(R.string.title_dashboard);
                        break;
                    case 2:
                        mTextMessage.setText(R.string.title_notifications);
                        break;
                }
            }
        });
    }

//    @Override
//    protected SearchView.OnCloseListener getSearchOnCloseListener() {
//        return new SearchView.OnCloseListener() {
//            @Override
//            public boolean onClose() {
//                Toast.makeText(MainActivity.this, "SearchCLOSE!", Toast.LENGTH_SHORT).show();
//                return false;
//            }
//        };
//    }
//
//    @Override
//    protected SearchView.OnQueryTextListener getSearchOnQueryTextListener() {
//        return new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                Toast.makeText(MainActivity.this, "Search: " + query, Toast.LENGTH_SHORT).show();
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                return false;
//            }
//        };
//    }

}
