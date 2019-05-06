package com.beyondedge.hm.base;

import android.text.TextUtils;
import android.view.View;

import com.beyondedge.hm.R;
import com.beyondedge.hm.config.LoadConfig;
import com.beyondedge.hm.config.TemplateMessage;
import com.beyondedge.hm.ui.screen.PageWebActivity;
import com.beyondedge.hm.utils.URLUtils;

/**
 * Created by Hoa Nguyen on May 02 2019.
 */
public abstract class BaseTemplateActivity extends BaseSearchServerLibActivity {
    public static final int SEARCH_TYPE_HIDE_ALL = 0;
    public static final int SEARCH_TYPE_FULL_TOOLBAR = 1;
    public static final int SEARCH_TYPE_MENU = 2;
    public static final String HOME = "home";
    public static final String PROD_CAT = "prod_cat";
    public static final String PROD_DETAIL = "prod_detail";
    public static final String ACCOUNT = "account";
    public static final String CHECKOUT = "checkout";

    //    private boolean isFULLToolBarSearch = true;
    private int searchType = SEARCH_TYPE_HIDE_ALL;
    private boolean isCanShare = false;
    private View btSearch;
    private View btShare;

    private TemplateMessage mTemplateMessage;

    @Override
    protected void initSearchView() {
        super.initSearchView();

        btSearch = findViewById(R.id.btn_search);
        btShare = findViewById(R.id.btn_share);

        btShare.setOnClickListener(v -> {
            if (mTemplateMessage != null && URLUtils.isURLValid(mTemplateMessage.getSharePageUrl())) {
                URLUtils.share(BaseTemplateActivity.this, mTemplateMessage.getSharePageUrl());
            }
        });

        btSearch.setOnClickListener(v -> {
            settingBack(true);
            showSearch();
        });

        super.settingBack(false);

        validateSearch();
    }

    protected boolean isCanBack() {
        return searchType != SEARCH_TYPE_FULL_TOOLBAR;
    }

    @Override
    protected void enableBackButtonToolbar() {
//        super.enableBackButtonToolbar();

        View backBt = findViewById(R.id.btn_back);
        if (backBt != null) {
            backBt.setVisibility(View.VISIBLE);
            backBt.setOnClickListener(v -> {
                if (isCanBack() && isSearchVisible()) {
                    hideSearch();
                } else {
                    finish();
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        if (isVisible() && searchType == SEARCH_TYPE_MENU) {
            hideSearch();
        } else {
            super.onBackPressed();
        }
    }

    //* * logic code */

    public void updateTemplate(TemplateMessage templateMessage) {
        if (templateMessage == null) return;

        mTemplateMessage = templateMessage;

        String tempString = templateMessage.getPageTemplate();

        isCanShare = mTemplateMessage != null && !TextUtils.isEmpty(templateMessage.getSharePageUrl());
        updateToolbarByTemplate(tempString);

        if (mTemplateMessage.isHome()) {
            setTitleToolbar("");
        } else {
            setTitleToolbar(templateMessage.getPageTitle());
        }
    }

    protected void updateToolbarByTemplate(String template) {
        if (TextUtils.isEmpty(template)) {
            //HOME
            searchType = SEARCH_TYPE_FULL_TOOLBAR;
        } else if (TemplateMessage.PROD_CAT.equals(template)) {
            //PROD_CAT
            searchType = SEARCH_TYPE_MENU;
        } else if (TemplateMessage.PROD_DETAIL.equals(template)) {
            //PROD_DETAIL
            searchType = SEARCH_TYPE_MENU;
        } else if (TemplateMessage.ACCOUNT.equals(template)) {
            //ACCOUNT
            searchType = SEARCH_TYPE_HIDE_ALL;
        } else if (TemplateMessage.CHECKOUT.equals(template)) {
            //ACCOUNT
            searchType = SEARCH_TYPE_HIDE_ALL;
        } else {
            //HOME
            searchType = SEARCH_TYPE_FULL_TOOLBAR;
        }

        validateSearch();
    }

    protected void setSearchType(int searchType) {
        this.searchType = searchType;

        validateSearch();
    }

    private void validateSearch() {
        if (isSearchInit()) {
            switch (searchType) {

                case SEARCH_TYPE_FULL_TOOLBAR:
                    toolBarSearch();
                    break;

                case SEARCH_TYPE_MENU:
                    menuSearch();
                    break;

                case SEARCH_TYPE_HIDE_ALL:
                default:
                    hideAllSearch();
                    break;
            }
        }
    }


    private void toolBarSearch() {
        btSearch.setVisibility(View.GONE);
        btShare.setVisibility(View.GONE);
        settingBack(false);
        showSearch();
    }

    private void menuSearch() {
        settingBack(true);
        btSearch.setVisibility(View.VISIBLE);
        btShare.setVisibility(isCanShare ? View.VISIBLE : View.GONE);
        hideSearch();
    }

    private void hideAllSearch() {
        btSearch.setVisibility(View.GONE);
        btShare.setVisibility(View.GONE);
        hideSearch();
    }

    protected void handleSearchLink(String fullURL) {
        PageWebActivity.startScreen(BaseTemplateActivity.this, fullURL, "");
//                Snackbar.make(bottomNavigation, "Search:[" + query + "]",
//                        Snackbar.LENGTH_SHORT).show();
    }

    @Override
    protected QueryTextListener getQueryTextListener() {
        return new QueryTextListener() {
            @Override
            public void onQueryTextSubmit(String query) {

                if (searchType == SEARCH_TYPE_FULL_TOOLBAR) {
                    hideKeyboard();
                } else {
                    hideSearch();
                }

                String fullURL = LoadConfig.getInstance().load().getVersion().getMainDomain() +
                        "catalogsearch/result/?q=" + query;
                handleSearchLink(fullURL);
            }

            @Override
            public void onQueryTextChange(String newText) {

            }
        };
    }


}
