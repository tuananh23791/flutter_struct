package com.beyondedge.hm.base;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.beyondedge.hm.MainActivity;
import com.beyondedge.hm.R;
import com.beyondedge.hm.config.HMConfig;
import com.beyondedge.hm.config.LoadConfig;
import com.beyondedge.hm.config.TemplateMessage;
import com.beyondedge.hm.ui.page.ViewPagerAdapter;
import com.beyondedge.hm.ui.screen.PageWebActivity;
import com.beyondedge.hm.utils.URLUtils;

/**
 * Created by Hoa Nguyen on May 02 2019.
 */
public abstract class BaseTemplateActivity extends BaseSearchServerLibActivity {
    public static final int SEARCH_TYPE_HIDE_ALL = 0;
    public static final int SEARCH_TYPE_FULL_TOOLBAR = 10;
    public static final int SEARCH_TYPE_MENU_DETAIL = 20;
    public static final int SEARCH_TYPE_MENU_CART = 25;
    public static final int SEARCH_TYPE_MENU_CHECKOUT = 30;
    //    public static final String HOME = "home";
//    public static final String PROD_CAT = "prod_cat";
//    public static final String PROD_DETAIL = "prod_detail";
//    public static final String ACCOUNT = "account";
//    public static final String CHECKOUT = "checkout";
    private boolean isWebPageCanGoBack = false;
    //    private boolean isFULLToolBarSearch = true;
    private int searchType = SEARCH_TYPE_HIDE_ALL;
    private boolean isCanShare = false;
    private View btSearch;
    private View btShare;
    private View btCart;
    private TextView cartNumber;
    private TemplateMessage mTemplateMessage;

    public void setWebPageCanGoBack(boolean webPageCanGoBack) {
        isWebPageCanGoBack = webPageCanGoBack;
    }

    @Override
    protected void initSearchView() {
        super.initSearchView();

        btSearch = findViewById(R.id.btn_search);
        btShare = findViewById(R.id.btn_share);
        btCart = findViewById(R.id.btn_cart);
        cartNumber = findViewById(R.id.cartNumber);

        btShare.setOnClickListener(v -> {
            if (mTemplateMessage != null && URLUtils.isURLValid(mTemplateMessage.getSharePageUrl())) {
                URLUtils.share(BaseTemplateActivity.this, mTemplateMessage.getSharePageUrl());
            }
        });

        btSearch.setOnClickListener(v -> {
            settingBack(true);
            showSearch();
        });

        btCart.setOnClickListener(v -> {
            HMConfig config = LoadConfig.getInstance().load();
            HMConfig.Menu mMenu = config.getMainMenuList().get(ViewPagerAdapter.MENU_CHECKOUT);
            PageWebActivity.startScreen(this, mMenu.getUrl(), mMenu.getName());

        });

        enableBackButtonToolbar(null/*default*/);

        super.settingBack(false);

        validateSearch();
    }

    @Override
    protected void enableBackButtonToolbar(View.OnClickListener listener) {
//        View backBt = findViewById(R.id.btn_back);
//        if (backBt != null) {
//            backBt.setVisibility(View.VISIBLE);
//            backBt.setOnClickListener(v -> {
//                if (isCanBack() && isSearchVisible()) {
//                    hideSearch();
//                } else {
//                    super.enableBackButtonToolbar(null);
//                }
//            });
//        }

        super.enableBackButtonToolbar(listener);
    }

    protected boolean isHandledHideSearchBarInBackPressed() {
        if (isVisible() && (searchType == SEARCH_TYPE_MENU_DETAIL || searchType == SEARCH_TYPE_MENU_CART)) {
            hideSearch();
            return true;
        }

        return false;
    }

    @Override
    public void onBackPressed() {
        if (!isHandledHideSearchBarInBackPressed()) {
            super.onBackPressed();
        }
    }

    //* * logic code */

    public void updateTemplate(TemplateMessage templateMessage) {
        if (templateMessage == null || TextUtils.isEmpty(templateMessage.getPageTemplate())) return;

        btSearch.post(new Runnable() {
            @Override
            public void run() {
                mTemplateMessage = templateMessage;
                String tempString = templateMessage.getPageTemplate();

                isCanShare = mTemplateMessage != null && !TextUtils.isEmpty(templateMessage.getSharePageUrl());
//        updateToolbarByTemplate(tempString);
//        if (cartNumber != null) {
//            cartNumber.setText(String.valueOf(mTemplateMessage.getCartCount()));
//        }

                if (mTemplateMessage != null && mTemplateMessage.isHome()) {
                    setTitleToolbar("");
                } else {
                    setTitleToolbar(templateMessage.getPageTitle());
                }

                updateToolbarByTemplate(tempString);
                if (cartNumber != null) {
                    cartNumber.setText(String.valueOf(mTemplateMessage.getCartCount()));
                }
            }
        });
    }

    protected void updateToolbarByTemplate(String template) {
        if (TextUtils.isEmpty(template)) {
            //HOME
            searchType = SEARCH_TYPE_FULL_TOOLBAR;
        } else if (TemplateMessage.PROD_CAT.equals(template)) {
            //PROD_CAT
            searchType = SEARCH_TYPE_MENU_CART;
        } else if (TemplateMessage.PROD_DETAIL.equals(template)) {
            //PROD_DETAIL
            searchType = SEARCH_TYPE_MENU_DETAIL;
        } else if (TemplateMessage.ACCOUNT.equals(template)) {
            //ACCOUNT
            searchType = SEARCH_TYPE_HIDE_ALL;
        } else if (TemplateMessage.CHECKOUT.equals(template)) {
            //ACCOUNT
            searchType = SEARCH_TYPE_MENU_CHECKOUT;
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
                    settingBack(isWebPageCanGoBack);
                    toolBarSearch();

                    if (this instanceof MainActivity) {
                        ((MainActivity) (this)).showOrHideBottomNavigation(true);
                    }
                    break;

                case SEARCH_TYPE_MENU_DETAIL:
                    settingBack(true);
                    menuSearchProdDetail();

                    if (this instanceof MainActivity) {
                        ((MainActivity) (this)).showOrHideBottomNavigation(false);
                    }
                    break;

                case SEARCH_TYPE_MENU_CART:
                    settingBack(true);
                    menuSearchProCat();
                    if (this instanceof MainActivity) {
                        MainActivity mainActivity = (MainActivity) (this);
                        mainActivity.showOrHideBottomNavigation(true);
                    }
                    break;
                case SEARCH_TYPE_MENU_CHECKOUT:
                    settingBack(isWebPageCanGoBack);
                    hideAllSearch();

                    if (this instanceof MainActivity) {
                        MainActivity mainActivity = (MainActivity) (this);
                        if (!mainActivity.isCheckOutTab()) {
                            mainActivity.showOrHideBottomNavigation(false);
                        } else {
                            mainActivity.showOrHideBottomNavigation(true);
                        }
                    }
                    break;

                case SEARCH_TYPE_HIDE_ALL:
                default:
                    settingBack(isWebPageCanGoBack);
                    hideAllSearch();

                    if (this instanceof MainActivity) {
                        ((MainActivity) (this)).showOrHideBottomNavigation(true);
                    }
                    break;
            }
        } else {
            Toast.makeText(this, "Something Error!Please try again!", Toast.LENGTH_SHORT).show();
        }
    }


    private void toolBarSearch() {
//        btSearch.post(new Runnable() {
//            @Override
//            public void run() {
//                btSearch.setVisibility(View.GONE);
//                btShare.setVisibility(View.GONE);
//                //TODO -------------
////        settingBack(false);
////        settingBack(isWebPageCanGoBack);
//                showSearch();
//            }
//        });

        btSearch.setVisibility(View.GONE);
        btShare.setVisibility(View.GONE);
        btCart.setVisibility(View.GONE);
        TextView middleTitle = findViewById(R.id.txt_title);
        middleTitle.setPadding(0, 0, 0, 0);
        //TODO -------------
//        settingBack(false);
//        settingBack(isWebPageCanGoBack);
        marginTopFrame(true);
        showSearch();

    }

    private void menuSearchProCat() {
//        btCart.post(new Runnable() {
//            @Override
//            public void run() {
//                settingBack(true);
//                btCart.setVisibility(View.GONE);
//                //        settingBack(isWebPageCanGoBack);
//                btSearch.setVisibility(View.VISIBLE);
//                btShare.setVisibility(isCanShare ? View.VISIBLE : View.GONE);
//                hideSearch();
//            }
//        });

        settingBack(true);
        btCart.setVisibility(View.GONE);
        //        settingBack(isWebPageCanGoBack);
        btSearch.setVisibility(View.VISIBLE);
        btShare.setVisibility(isCanShare ? View.VISIBLE : View.GONE);
        if (isCanShare) {
            TextView middleTitle = findViewById(R.id.txt_title);
            int dimensionPixelOffset = getResources().getDimensionPixelOffset(android.R.dimen.app_icon_size);
            middleTitle.setPadding(dimensionPixelOffset, 0, dimensionPixelOffset, 0);
        } else {
            TextView middleTitle = findViewById(R.id.txt_title);
            middleTitle.setPadding(0, 0, 0, 0);
        }
        marginTopFrame(true);
        hideSearch();
    }


    private void menuSearchProdDetail() {
////        settingBack(isWebPageCanGoBack);
//        btCart.post(new Runnable() {
//            @Override
//            public void run() {
//                settingBack(true);
//                btSearch.setVisibility(View.GONE);
//                btShare.setVisibility(View.GONE);
//                btCart.setVisibility(View.VISIBLE);
//                hideSearch();
//            }
//        });

        settingBack(true);
        btSearch.setVisibility(View.GONE);
        btShare.setVisibility(View.GONE);
        TextView middleTitle = findViewById(R.id.txt_title);
        middleTitle.setPadding(0, 0, 0, 0);
        btCart.setVisibility(View.VISIBLE);
        marginTopFrame(false);
        hideSearch();

    }

    private void hideAllSearch() {
        btSearch.setVisibility(View.GONE);
        btShare.setVisibility(View.GONE);
        btCart.setVisibility(View.GONE);
        TextView middleTitle = findViewById(R.id.txt_title);
        middleTitle.setPadding(0, 0, 0, 0);
        marginTopFrame(true);
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
