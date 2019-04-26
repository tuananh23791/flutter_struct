package com.beyondedge.hm.ui.screen;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.beyondedge.hm.HMApplication;
import com.beyondedge.hm.R;
import com.beyondedge.hm.base.BaseFragment;
import com.beyondedge.hm.config.HMConfig;
import com.beyondedge.hm.config.LoadConfig;
import com.beyondedge.hm.databinding.SettingsLayoutBinding;
import com.beyondedge.hm.ui.SplashScreen;
import com.beyondedge.hm.utils.PrefManager;

import java.util.ArrayList;

/**
 * Created by Hoa Nguyen on Apr 25 2019.
 */
public class SettingsFragment extends BaseFragment {
    private SettingsLayoutBinding binding;
    private SettingViewModel mViewModel;
    private int whichRegion = 1;
    private boolean isForceRestartApp = false;

    public static SettingsFragment newInstance() {

        Bundle args = new Bundle();

        SettingsFragment fragment = new SettingsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        mViewModel = ViewModelProviders.of(this).get(SettingViewModel.class);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.settings_layout, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.setAppVersion(((HMApplication) view.getContext().getApplicationContext()).getVersionName());

        binding.tvNotificationInfo.setText(mViewModel.getTextSwitch(binding.switchNotification.isChecked()));
        binding.tvCameraInfo.setText(mViewModel.getTextSwitch(binding.switchCamera.isChecked()));

        binding.switchNotification.setOnCheckedChangeListener((buttonView, isChecked)
                -> binding.tvNotificationInfo.setText(mViewModel.getTextSwitch(isChecked)));

        binding.switchCamera.setOnCheckedChangeListener((buttonView, isChecked)
                -> binding.tvCameraInfo.setText(mViewModel.getTextSwitch(isChecked)));


        updateUIRegion();
        binding.pressRegion.setOnClickListener(v -> {
            HMConfig config = LoadConfig.getInstance().load();
            ArrayList<HMConfig.Region> regions = config.getRegion();
            CharSequence[] arrs = new CharSequence[regions.size()];
            HMConfig.Region region;
            String currentLinkConfig = PrefManager.getInstance().getCurrentLinkConfig();
            for (int i = 0; i < regions.size(); i++) {
                region = regions.get(i);
                arrs[i] = region.getName();

                if (region.getPropertyFile().equals(currentLinkConfig)) {
                    whichRegion = i;
                }
            }
            new AlertDialog.Builder(view.getContext())
                    .setSingleChoiceItems(arrs, whichRegion, (dialog, which) -> {
                        HMConfig.Region selectedRegion = regions.get(which);
                        PrefManager.getInstance().putCurrentLinkConfig(selectedRegion.getPropertyFile());
                        if (which != whichRegion) {
                            isForceRestartApp = true;
                        }
                        dialog.dismiss();
                    })
                    .setPositiveButton(android.R.string.ok, null)
                    .setOnDismissListener(dialog -> {
                        if (isForceRestartApp) {
                            new Handler().postDelayed(this::restartApp, 200);
                        }
                    })
                    .show();
        });
    }

    private void updateUIRegion() {
        HMConfig config = LoadConfig.getInstance().load();
        final ArrayList<HMConfig.Region> regions = config.getRegion();
        HMConfig.Region region;
        String currentLinkConfig = PrefManager.getInstance().getCurrentLinkConfig();
        for (int i = 0; i < regions.size(); i++) {
            region = regions.get(i);

            if (region.getPropertyFile().equals(currentLinkConfig)) {
                whichRegion = i;
            }
        }
        HMConfig.Region selectedRegion = regions.get(whichRegion);
        binding.setRegion(selectedRegion.getName());

        binding.tvLang.setText(config.getLanguageBy("search_product"));
        binding.tvLangInfo.setText(selectedRegion.getPropertyFile());
    }

    private void restartApp() {
        SplashScreen.startActivity(getActivity());
    }
}
