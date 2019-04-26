package com.beyondedge.hm.ui.screen;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.beyondedge.hm.HMApplication;
import com.beyondedge.hm.R;
import com.beyondedge.hm.base.BaseFragment;
import com.beyondedge.hm.config.Constant;
import com.beyondedge.hm.config.HMConfig;
import com.beyondedge.hm.config.LoadConfig;
import com.beyondedge.hm.config.ParseFileAsyncTask;
import com.beyondedge.hm.databinding.SettingsLayoutBinding;
import com.beyondedge.hm.ui.SplashScreen;
import com.beyondedge.hm.utils.PrefManager;
import com.tonyodev.fetch2.Download;
import com.tonyodev.fetch2.Fetch;
import com.tonyodev.fetch2.Request;
import com.tonyodev.fetch2core.FetchObserver;
import com.tonyodev.fetch2core.Reason;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import timber.log.Timber;

/**
 * Created by Hoa Nguyen on Apr 25 2019.
 */
public class SettingsFragmentOLD extends BaseFragment implements FetchObserver<Download> {
    private ProgressDialog dialog;
    private SettingsLayoutBinding binding;
    private SettingViewModel mViewModel;
    private int whichRegion = 1;
    private Request request;
    private Fetch fetch;

    public static SettingsFragmentOLD newInstance() {

        Bundle args = new Bundle();

        SettingsFragmentOLD fragment = new SettingsFragmentOLD();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        mViewModel = ViewModelProviders.of(this).get(SettingViewModel.class);
        fetch = Fetch.Impl.getDefaultInstance();
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
                        //tODO
//                        whichRegion = which;
                        HMConfig.Region selectedRegion = regions.get(which);
                        PrefManager.getInstance().putCurrentLinkConfig(selectedRegion.getPropertyFile());
//                        binding.setRegion(selectedRegion.getName());

//                        enqueueDownload(selectedRegion.getPropertyFile());
                        dialog.dismiss();
                    })
                    .setPositiveButton(android.R.string.ok, null)
                    .show();
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        if (request != null) {
            fetch.attachFetchObserversForDownload(request.getId(), this);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (request != null) {
            fetch.removeFetchObserversForDownload(request.getId(), this);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (fetch != null)
            fetch.close();
    }

    private void enqueueDownload(String newLink) {
        dialog = ProgressDialog.show(getActivity(), "",
                "Loading. Please wait...", true, true);

        dialog.setOnDismissListener(dialog -> {
            if (request != null) {
                fetch.cancel(request.getId());
                request = null;
                Toast.makeText(getActivity(), "Undo", Toast.LENGTH_SHORT).show();
            }
        });
        PrefManager.getInstance().putCurrentLinkConfig(newLink);
        final String filePath = Constant.getLinkSavedFile();
        request = new Request(newLink, filePath);
//        request.addHeader("Authorization", Constant.getAuthorizationParam());
        fetch.attachFetchObserversForDownload(request.getId(), this)
                .enqueue(request,
                        result -> request = result,
//                        result -> {
//                        }
                        result -> handleDownloadConfigError(result.toString())
                );
    }

    @Override
    public void onChanged(Download download, @NotNull Reason reason) {
        if (getView() != null) {
            if (request.getId() == download.getId()) {
                if (reason == Reason.DOWNLOAD_COMPLETED || reason == Reason.DOWNLOAD_ERROR) {
                    if (reason == Reason.DOWNLOAD_ERROR) {
                        Timber.d("Download  Error: %1$s", download.toString());
                    }
                    request = null;
                    readConfig();
                } else if (reason == Reason.DOWNLOAD_WAITING_ON_NETWORK) {
                    Toast.makeText(getActivity(), "Waiting for network...", Toast.LENGTH_SHORT).show();
                } else if (reason == Reason.DOWNLOAD_CANCELLED) {
                    request = null;
                    Toast.makeText(getActivity(), "Canceled", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void readConfig() {
        Timber.d("Download  Completed");
        if (this.getContext() != null) {
            new ParseFileAsyncTask(this.getContext()).setTaskListener(() -> {
                if (getView() != null) {
                    //TODO
//                    restartApp();
                    request = null;
                    Toast.makeText(getActivity(), "Updated", Toast.LENGTH_SHORT).show();
                    updateUIRegion();

                    if (getView() != null && dialog != null) {
                        dialog.dismiss();
                    }
                }
            }).execute();
        }
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

    private void handleDownloadConfigError(String serverError) {
        //TODO
        Timber.d("Download Config Error: %1$s", serverError);
        Toast.makeText(getActivity(), "Cannot load Region", Toast.LENGTH_SHORT).show();
    }

    private void restartApp() {
        Intent mStartActivity = new Intent(getActivity(), SplashScreen.class);
        int mPendingIntentId = 123456;
        PendingIntent mPendingIntent = PendingIntent.getActivity(getActivity(), mPendingIntentId, mStartActivity,
                PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager mgr = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
        System.exit(0);
    }
}
