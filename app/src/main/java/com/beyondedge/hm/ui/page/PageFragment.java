package com.beyondedge.hm.ui.page;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.beyondedge.hm.R;
import com.beyondedge.hm.base.BaseActivity;
import com.beyondedge.hm.config.HMConfig;
import com.beyondedge.hm.config.LoadConfig;

/**
 * Created by Hoa Nguyen on Apr 22 2019.
 */
public class PageFragment extends WebFragment implements PageInterface {
    private View fragmentContainer;
    private int mIndex;
    private HMConfig.Menu mMenu;

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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        mIndex = bundle.getInt("index", 0);
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
        final TextView textInfo = view.findViewById(R.id.textInfo);
        fragmentContainer = view.findViewById(R.id.fragmentContainer);


        HMConfig config = LoadConfig.getInstance().load();
        mMenu = config.getMenuList().get(mIndex);
        String linkPage = config.getPageLink(mMenu);
        textInfo.setText(mMenu.getName() + "\n" + linkPage);

        loadPage(linkPage);
    }

    /**
     * Refresh
     */
    @Override
    public void refresh() {
//        if (getArguments().getInt("index", 0) > 0 && recyclerView != null) {
//            recyclerView.smoothScrollToPosition(0);
//        }
    }


    /**
     * Called when a fragment will be displayed
     */
    @Override
    public void willBeDisplayed() {
        FragmentActivity activity = getActivity();

        if (activity != null && activity instanceof BaseActivity) {
            ((BaseActivity) activity).setTitleToolbar(mMenu.getName());
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
        if (fragmentContainer != null) {
            Animation fadeOut = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_out);
            fragmentContainer.startAnimation(fadeOut);
        }
    }
}
