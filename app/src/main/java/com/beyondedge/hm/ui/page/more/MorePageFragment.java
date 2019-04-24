package com.beyondedge.hm.ui.page.more;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.beyondedge.hm.R;
import com.beyondedge.hm.base.BaseFragment;
import com.beyondedge.hm.config.HMConfig;
import com.beyondedge.hm.config.LoadConfig;
import com.beyondedge.hm.ui.page.PageInterface;
import com.beyondedge.hm.ui.screen.PageWebActivity;
import com.beyondedge.hm.ui.view.PaddingDividerItemDecoration;

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
        return inflater.inflate(R.layout.more_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recyclerView);
        fragmentContainer = view.findViewById(R.id.fragmentContainer);
        mMorePageAdapter = new MorePageAdapter();
        mMorePageAdapter.setOnItemClickListener(menu -> {
            Intent intent = new Intent(getActivity(), PageWebActivity.class);
            intent.putExtra(PageWebActivity.EXTRA_URL, menu.getUrl());
            intent.putExtra(PageWebActivity.EXTRA_TITLE, menu.getName());
            startActivity(intent);
        });

        HMConfig config = LoadConfig.getInstance().load();

        if (config != null) {
            mMorePageAdapter.submitList(config.getMenuList().get(4).getSubListMenu());
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
