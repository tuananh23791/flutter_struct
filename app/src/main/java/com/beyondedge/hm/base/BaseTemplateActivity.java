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
    public static final String HOME = "home";
    public static final String PROD_CAT = "prod_cat";
    public static final String PROD_DETAIL = "prod_detail";
    public static final String ACCOUNT = "account";
    public static final String CHECKOUT = "checkout";

    private boolean isFULLToolBarSearch = true;
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
            canBack(true);
            showSearch();
        });
    }

    @Override
    public void onBackPressed() {
        if (isVisible() && !isFULLToolBarSearch) {
            hideSearch();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected Runnable initTemplate() {
        return () -> {
            if (isFULLToolBarSearch) {
                toolBarSearch();
            } else {
                menuSearch();
            }
        };
    }

    //* * logic code */

    public void updateTemplate(TemplateMessage templateMessage) {
        mTemplateMessage = templateMessage;

        String tempString = mTemplateMessage != null ? templateMessage.getPageTemplate() : "";

        isCanShare = mTemplateMessage != null && !TextUtils.isEmpty(templateMessage.getSharePageUrl());
        updateToolbarByTemplate(tempString);

        setTitleToolbar(templateMessage.getPageTitle());
    }

    protected void updateToolbarByTemplate(String template) {
        if (TextUtils.isEmpty(template)) {
            //HOME
            setFULLToolBarSearch(true);
        } else if (TemplateMessage.PROD_CAT.equals(template)) {
            //PROD_CAT
            setFULLToolBarSearch(false);
        } else if (TemplateMessage.PROD_DETAIL.equals(template)) {
            //PROD_DETAIL
            setFULLToolBarSearch(false);
        } else if (TemplateMessage.ACCOUNT.equals(template)) {
            //ACCOUNT
            hideAllSearch();
        } else if (TemplateMessage.CHECKOUT.equals(template)) {
            //ACCOUNT
            hideAllSearch();
        } else {
            //HOME
            setFULLToolBarSearch(true);
        }
    }


    protected void showHideSearchMenu(boolean isShow) {

        if (!isShow) {
            hideSearch();
            btSearch.setVisibility(View.GONE);
            btShare.setVisibility(View.GONE);
        } else {
            if (isFULLToolBarSearch) {
                toolBarSearch();
            } else {
                menuSearch();
            }
        }
    }

    private void toolBarSearch() {
        isFULLToolBarSearch = true;
        btSearch.setVisibility(View.GONE);
        btShare.setVisibility(View.GONE);
        canBack(false);
        showSearch();
    }

    private void menuSearch() {
        canBack(true);
        isFULLToolBarSearch = false;
        btSearch.setVisibility(View.VISIBLE);
        btShare.setVisibility(isCanShare ? View.VISIBLE : View.GONE);
//        if (!isFULLToolBarSearch) {
//           hideSearch();
//        } else {
//            searchHolder.hideKeyboard();
//        }
        hideSearch();
    }

    private void setFULLToolBarSearch(boolean FULLToolBarSearch) {
        isFULLToolBarSearch = FULLToolBarSearch;

        if (isFULLToolBarSearch) {
            toolBarSearch();
        } else {
            menuSearch();
        }
    }

    private void hideAllSearch() {
        btSearch.setVisibility(View.GONE);
        btShare.setVisibility(View.GONE);
        hideSearch();
    }

    @Override
    protected QueryTextListener getQueryTextListener() {
        return new QueryTextListener() {
            @Override
            public void onQueryTextSubmit(String query) {

                if (isFULLToolBarSearch) {
                    hideKeyboard();
                } else {
                    hideSearch();
                }

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


}
