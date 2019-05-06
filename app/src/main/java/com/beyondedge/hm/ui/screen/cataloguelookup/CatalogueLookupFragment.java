package com.beyondedge.hm.ui.screen.cataloguelookup;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.beyondedge.hm.R;
import com.beyondedge.hm.base.BaseFragment;
import com.beyondedge.hm.databinding.CatalogueLookupLayoutBinding;
import com.beyondedge.hm.ui.screen.PageWebActivity;
import com.beyondedge.hm.utils.URLUtils;
import com.beyondedge.hm.utils.ViewUtils;

import timber.log.Timber;

/**
 * Created by Hoa Nguyen on Apr 25 2019.
 */
public class CatalogueLookupFragment extends BaseFragment {
    private CatalogueLookupLayoutBinding binding;
    private CatalogueLookupViewModel mViewModel;

    public static CatalogueLookupFragment newInstance() {

        Bundle args = new Bundle();

        CatalogueLookupFragment fragment = new CatalogueLookupFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(CatalogueLookupViewModel.class);
        mViewModel.getSearchListLive().observe(this, value -> {
            if (URLUtils.isURLValid(value)) {
                PageWebActivity.startScreen(getActivity(), value, "");
            } else {
                showPopupError(value);
            }
        });

        mViewModel.getGetLoadingState().observe(this, aBoolean -> {
            Timber.d("mViewModel.getGetLoadingState() - " + aBoolean);
            if (aBoolean) {
                showLoading();
            } else {
                hideLoading();
            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.catalogue_lookup_layout, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        binding.setAppVersion(((HMApplication) view.getContext().getApplicationContext()).getVersionName());

        binding.btLookup.setOnClickListener(v -> {
            ViewUtils.hideKeyboardFrom(v.getContext(), binding.tvArticleInput);
//            Toast.makeText(getActivity(), "Lookup - " + binding.tvArticleInput.getText().toString(), Toast.LENGTH_SHORT).show();

            String article = binding.tvArticleInput.getText().toString();
            if (TextUtils.isEmpty(article)) {
                Toast.makeText(getActivity(), "Please input article number", Toast.LENGTH_SHORT).show();
                return;
            }
            mViewModel.catalogueLookup(binding.tvArticleInput.getText().toString());
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mViewModel.onCleared();
    }

}
