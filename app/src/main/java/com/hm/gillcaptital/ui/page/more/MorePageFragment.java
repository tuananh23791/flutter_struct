package com.hm.gillcaptital.ui.page.more;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hm.gillcaptital.R;
import com.hm.gillcaptital.base.BaseActivity;
import com.hm.gillcaptital.base.BaseFragment;
import com.hm.gillcaptital.config.Constant;
import com.hm.gillcaptital.config.HMConfig;
import com.hm.gillcaptital.config.LoadConfig;
import com.hm.gillcaptital.ui.page.PageInterface;
import com.hm.gillcaptital.ui.screen.FollowUsActivity;
import com.hm.gillcaptital.ui.screen.PageWebActivity;
import com.hm.gillcaptital.ui.screen.SettingsActivity;
import com.hm.gillcaptital.ui.screen.cataloguelookup.CatalogueLookupActivity;
import com.hm.gillcaptital.ui.view.PaddingDividerItemDecoration;

/**
 * Created by Hoa Nguyen on Apr 23 2019.
 */
public class MorePageFragment extends BaseFragment implements PageInterface {
    private View fragmentContainer;
    private MorePageAdapter mMorePageAdapter;
    private RecyclerView recyclerView;

    public static MorePageFragment newInstance() {

        Bundle args = new Bundle();

        MorePageFragment fragment = new MorePageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.single_recyclerview_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recyclerView);
        fragmentContainer = view.findViewById(R.id.fragmentContainer);
        mMorePageAdapter = new MorePageAdapter();
        mMorePageAdapter.setOnItemClickListener(menu -> {
            String url = menu.getUrl(getActivity());
            if (url != null && url.contains(Constant.FOLLOW_US_PATH)) {
                startActivity(FollowUsActivity.class);
            } else if (url != null && url.contains(Constant.APP_SETTING_PATH)) {
                SettingsActivity.startScreen(getActivity(), menu.getName());
            } else if (menu.isCatalogueLookupMenu()) {
                Intent intent = new Intent(getActivity(), CatalogueLookupActivity.class);
                startActivity(intent);
            } else {
                //Open Web Page
                Intent intent = new Intent(getActivity(), PageWebActivity.class);
                intent.putExtra(PageWebActivity.EXTRA_URL, url);
                intent.putExtra(PageWebActivity.EXTRA_TITLE, menu.getName());
                startActivity(intent);
            }
        });

        HMConfig config = LoadConfig.getInstance(getActivity()).load();

        if (config != null) {
            mMorePageAdapter.submitList(config.getMoreSubListMenuList());
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext(), RecyclerView.VERTICAL, false));

        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new PaddingDividerItemDecoration(recyclerView.getContext()).paddingLeft());
        recyclerView.setAdapter(mMorePageAdapter);
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
        String title = "";
        HMConfig config = LoadConfig.getInstance(getActivity()).load();

        if (config != null) {
            HMConfig.Menu more = config.getMoreMenu();
            title = more.getName();
        }

        FragmentActivity activity = getActivity();

        if (activity instanceof BaseActivity) {
            ((BaseActivity) activity).setTitleToolbar(title);
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

    @Override
    public String defaultPage() {
        return null;
    }

    @Override
    public boolean goBack() {
        //nothing

        return false;
    }

    @Override
    public boolean canBack() {
        return false;
    }
}
