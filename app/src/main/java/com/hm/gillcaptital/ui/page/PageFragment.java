package com.hm.gillcaptital.ui.page;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.hm.gillcaptital.R;
import com.hm.gillcaptital.base.BaseActivity;
import com.hm.gillcaptital.config.HMConfig;
import com.hm.gillcaptital.config.LoadConfig;

import static com.hm.gillcaptital.ui.page.ViewPagerAdapter.MENU_HOME;

/**
 * Created by Hoa Nguyen on Apr 22 2019.
 */
public class PageFragment extends WebFragment implements PageInterface {
    private View fragmentContainer;
    private int mIndex;
    private HMConfig.Menu mMenu;
//    private boolean isInnitPage = false;

    /**
     * Create a new instance of the fragment
     */
    public static PageFragment newInstance(int index) {
        PageFragment fragment = new PageFragment();
        Bundle b = new Bundle();
        b.putInt("index", index);
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public void refreshRootPage() {
        if (mMenu == null) {
            HMConfig config = LoadConfig.getInstance(getActivity()).load();
            mMenu = config.getMainMenuList().get(mIndex);
        }
        loadPage(mMenu.getUrl(getActivity()));
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        mIndex = bundle != null ? bundle.getInt("index", MENU_HOME) : MENU_HOME;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    /**
     * Init view
     */
    private void initView(View view) {
        fragmentContainer = view.findViewById(R.id.fragmentContainer);

//        HMConfig config = LoadConfig.getInstance().load();
//        mMenu = config.getMainMenuList().get(mIndex);
//
//        loadPage(mMenu.getUrl());

        refreshRootPage();
    }

    @Override
    public void onPageFinished(String url) {
        super.onPageFinished(url);

        if (isDisplaying && canGoBack()) {
            //TODO
        }
    }

    /**
     * Refresh
     */
    @Override
    public void refresh() {
//        if (getArguments().getInt("index", 0) > 0 && recyclerView != null) {
//            recyclerView.smoothScrollToPosition(0);
//        }

//        loadPage(mMenu.getUrl());
    }


    /**
     * Called when a fragment will be displayed
     */
    @Override
    public void willBeDisplayed() {
        setDisplaying(true);
//        if (!isInnitPage) {
//            isInnitPage = true;
//            loadPage(mMenu.getUrl());
//        }
        FragmentActivity activity = getActivity();


        if (activity instanceof BaseActivity) {

            //TODO now ignore name from menu, using in template
//            if (mIndex == MENU_HOME) {
//                ((BaseActivity) activity).setTitleToolbar("");
//            } else {
//                ((BaseActivity) activity).setTitleToolbar(mMenu.getName());
//            }

            //update template cache

//            handleTemplateUpdate();

            if (mIndex != MENU_HOME) {
                loadPage(mMenu.getUrl(getActivity()));
            } else {
                handleTemplateUpdate();
            }
        }

        // Do what you want here, for example animate the content
        if (fragmentContainer != null) {
            Animation fadeIn = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in);
            fragmentContainer.startAnimation(fadeIn);
        }
    }

    /**
     * Called when a fragment will be hidden
     */
    @Override
    public void willBeHidden() {
        setDisplaying(false);
        if (fragmentContainer != null) {
            Animation fadeOut = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_out);
            fragmentContainer.startAnimation(fadeOut);
        }
    }

    @Override
    public String defaultPage() {
        return mMenu != null ? mMenu.getUrl(getActivity()) : "";
    }

    @Override
    public boolean goBack() {
        return super.goBack();
    }

    @Override
    public boolean canBack() {
        return canGoBack();
    }
}
