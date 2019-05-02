package com.beyondedge.hm.base;

import android.text.TextUtils;
import android.view.View;

import com.beyondedge.hm.R;
import com.beyondedge.hm.config.LoadConfig;
import com.beyondedge.hm.config.TemplateMessage;
import com.beyondedge.hm.ui.screen.PageWebActivity;

/**
 * Created by Hoa Nguyen on May 02 2019.
 */
public abstract class BaseTemplateActivity extends BaseSearchServerLibActivity {
    public static final String HOME = "home";
    public static final String PROD_CAT = "prod_cat";
    public static final String PROD_DETAIL = "prod_detail";
    public static final String ACCOUNT = "account";
    public static final String CHECKOUT = "checkout";

    private boolean isToolBarSearch = true;
    private boolean isShowSearchMenu = true;
    private View btSearch;
    private View btShare;

    @Override
    protected void initSearchView() {
        super.initSearchView();

        btSearch = findViewById(R.id.btn_search);
        btShare = findViewById(R.id.btn_share);

        btShare.setOnClickListener(v -> {
            //TODO share
        });

        btSearch.setOnClickListener(v -> {
            canBack(true);
            showSearch();
        });
    }

    @Override
    public void onBackPressed() {
        if (isVisible() && !isToolBarSearch) {
            hideSearch();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected Runnable initTemplate() {
        return () -> {
            if (isShowSearchMenu) {
                toolBarSearch();
            } else {
                menuSearch();
            }
        };
    }

    protected void showHideSearchMenu(boolean isShow) {

        if (!isShow) {
            hideSearch();
            btSearch.setVisibility(View.GONE);
            btShare.setVisibility(View.GONE);
        } else {
            if (isToolBarSearch) {
                toolBarSearch();
            } else {
                menuSearch();
            }
        }
    }

    private void toolBarSearch() {
        isToolBarSearch = true;
        btSearch.setVisibility(View.GONE);
        btShare.setVisibility(View.GONE);
        canBack(false);
        showSearch();
    }

    private void menuSearch() {
        canBack(true);
        isToolBarSearch = false;
        btSearch.setVisibility(View.VISIBLE);
        //TODO
        btShare.setVisibility(View.GONE);
//        if (!isToolBarSearch) {
//           hideSearch();
//        } else {
//            searchHolder.hideKeyboard();
//        }
        hideSearch();
    }

    private void setToolBarSearch(boolean toolBarSearch) {
        isToolBarSearch = toolBarSearch;

        if (isToolBarSearch) {
            toolBarSearch();
        } else {
            menuSearch();
        }
    }

    @Override
    protected QueryTextListener getQueryTextListener() {
        return new QueryTextListener() {
            @Override
            public void onQueryTextSubmit(String query) {

                String fullURL = LoadConfig.getInstance().load().getVersion().getMainDomain() +
                        "catalogsearch/result/?q=" + query;

                PageWebActivity.startScreen(BaseTemplateActivity.this, fullURL, "");
//                Snackbar.make(bottomNavigation, "Search:[" + query + "]",
//                        Snackbar.LENGTH_SHORT).show();
            }

            @Override
            public void onQueryTextChange(String newText) {

            }
        };
    }

    public void updateToolbarByTemplate(String template) {
        if (TextUtils.isEmpty(template)) {
            //HOME
            setToolBarSearch(true);
        } else if (TemplateMessage.PROD_CAT.equals(template)) {
            //ACCOUNT
        } else if (TemplateMessage.PROD_DETAIL.equals(template)) {
            //ACCOUNT
        } else if (TemplateMessage.ACCOUNT.equals(template)) {
            //ACCOUNT
        } else if (TemplateMessage.CHECKOUT.equals(template)) {
            //ACCOUNT
        } else {
            //HOME
        }
    }
}
