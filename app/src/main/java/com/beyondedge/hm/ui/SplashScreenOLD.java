package com.beyondedge.hm.ui;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.beyondedge.hm.MainActivity;
import com.beyondedge.hm.R;
import com.beyondedge.hm.api.ServiceHelper;
import com.beyondedge.hm.config.Constant;
import com.beyondedge.hm.config.HMConfig;
import com.beyondedge.hm.config.LoadConfig;
import com.beyondedge.hm.config.ParseFileAsyncTask;
import com.beyondedge.hm.utils.PrefManager;
import com.daimajia.androidanimations.library.Techniques;
import com.tonyodev.fetch2.Download;
import com.tonyodev.fetch2.Fetch;
import com.tonyodev.fetch2.Request;
import com.tonyodev.fetch2core.FetchObserver;
import com.tonyodev.fetch2core.Reason;
import com.viksaa.sssplash.lib.activity.AwesomeSplash;
import com.viksaa.sssplash.lib.cnst.Flags;
import com.viksaa.sssplash.lib.model.ConfigSplash;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

import static com.beyondedge.hm.config.Constant.IS_FORCE_LOCAL_CONFIG;

/**
 * Created by Hoa Nguyen on Apr 22 2019.
 * <p>
 * 1.show splash logo
 * 2.download config file
 * 3.check version app
 */
public class SplashScreenOLD extends AwesomeSplash implements FetchObserver<Download> {

    private static final int STORAGE_PERMISSION_CODE = 100;
    private Request request;
    private Fetch fetch;
    private int doingTask = 0;

    public static void startActivity(Activity from) {
        Intent intent = new Intent(from, SplashScreenOLD.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK
                | Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_NO_HISTORY);
        from.startActivity(intent);
    }


    @Override
    public void initSplash(ConfigSplash configSplash) {
        doingTask++;
        //Customize Circular Reveal
        configSplash.setBackgroundColor(R.color.colorBackground); //any color you want form colors.xml
        configSplash.setAnimCircularRevealDuration(500); //int ms
        configSplash.setRevealFlagX(Flags.REVEAL_RIGHT);  //or Flags.REVEAL_LEFT
        configSplash.setRevealFlagY(Flags.REVEAL_BOTTOM); //or Flags.REVEAL_TOP

        //Customize Logo
        configSplash.setLogoSplash(R.mipmap.ic_logo); //or any other drawable
        configSplash.setAnimLogoSplashDuration(1500); //int ms
        configSplash.setAnimLogoSplashTechnique(Techniques.FadeIn); //choose one form Techniques (ref: https://github.com/daimajia/AndroidViewAnimations)

        //Customize Title
        configSplash.setTitleSplash("");
//        configSplash.setTitleTextColor(R.color.Wheat);
//        configSplash.setTitleTextSize(30f); //float value
        configSplash.setAnimTitleDuration(500);
//        configSplash.setAnimTitleTechnique(Techniques.FlipInX);
//        configSplash.setTitleFont("fonts/myfont.ttf"); //provide string to your font located in assets/fonts/

    }

    @Override
    public void animationsFinished() {
        doingTask--;
        postExecuteSplashScreen();
    }

    private void postExecuteSplashScreen() {
        if (doingTask <= 0) {
            HMConfig config = LoadConfig.getInstance().load();
            if (config != null) {
                //TODO check later
                //show popup force update app
//                boolean isShowDialog = AppVersion.isLastVersion(this,
//                        config.getVersion().getVersionAndroidForceUpdate());
//                boolean isLastVersion = false;
//                if (isShowDialog) {
//                    AlertDialog.Builder builder = new AlertDialog.Builder(SplashScreen.this)
//                            .setMessage(config.getLanguageBy("AlertNewVersion"))
//                            .setPositiveButton(config.getLanguageBy("NewVersion_update"),
//                                    (dialog, which) -> {
//                                        dialog.dismiss();
//                                        SplashScreen.this.finish();
//                                        URLUtils.openInWebBrowser(SplashScreen.this,
//                                                "https://play.google.com/store/apps?hl=en");
//                                    });
//
//                    if (!isLastVersion) {
//                        builder.setNegativeButton(config.getLanguageBy("NewVersion_cancel"), (dialog, which) -> {
//                            dialog.dismiss();
//                            directToMainScreen();
//                        });
//                        builder.setCancelable(true);
//                    } else {
//                        builder.setCancelable(false);
//                        builder.setNegativeButton(config.getLanguageBy("NewVersion_cancel"), (dialog, which) -> {
//                            dialog.dismiss();
//                            SplashScreen.this.finish();
//                        });
//                    }
//
//                    builder.show();
//                } else {
//                    directToMainScreen();
//                }

                directToMainScreen();
            } else {
                directToMainScreen();
            }
        }
    }

    private void directToMainScreen() {
        startActivity(new Intent(SplashScreenOLD.this, MainActivity.class));
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setup load config
        setupLoadConfig();
    }

    private void setupLoadConfig() {
        if (IS_FORCE_LOCAL_CONFIG) {
            doingTask++;
            readConfig();
        } else {
            fetch = Fetch.Impl.getDefaultInstance();
            checkStoragePermission();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (request != null) {
            fetch.attachFetchObserversForDownload(request.getId(), this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (request != null) {
            fetch.removeFetchObserversForDownload(request.getId(), this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (fetch != null)
            fetch.close();
    }

    @Override
    public void onChanged(Download data, @NotNull Reason reason) {
        updateViews(data, reason);
    }

    private void checkStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        } else {
//            enqueueDownload();

            enqueueLoadConfigByAPI();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == STORAGE_PERMISSION_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//            enqueueDownload();

            enqueueLoadConfigByAPI();
        } else {
//            Snackbar.make(mainView, R.string.permission_not_enabled, Snackbar.LENGTH_LONG).show();

            //TODO
//            showPopup
            handleDownloadConfigError(getString(R.string.permission_not_enabled));
        }
    }

    private void enqueueDownload() {
        doingTask++;
        final String url = PrefManager.getInstance().getCurrentLinkConfig();
        final String filePath = Constant.getLinkSavedFile();
        request = new Request(url, filePath);
//        request.addHeader("Authorization", Constant.getAuthorizationParam());
        fetch.attachFetchObserversForDownload(request.getId(), this)
                .enqueue(request,
                        result -> request = result,
//                        result -> {
//                        }
                        result -> handleDownloadConfigError(result.toString())
                );
    }

    private void enqueueLoadConfigByAPI() {
        doingTask++;
        final String url = PrefManager.getInstance().getCurrentLinkConfig();
        ServiceHelper.getInstance().getNetworkConfigAPI().loadConfig(url)
                .enqueue(new Callback<HMConfig>() {
                    @Override
                    public void onResponse(Call<HMConfig> call, Response<HMConfig> response) {
                        //TODO

                        if (response.isSuccessful() && response.body() != null) {
                            LoadConfig.getInstance().setHMConfig(response.body());
                            doingTask--;
                            postExecuteSplashScreen();
                        } else {
                            handleDownloadConfigError("");
                        }
                    }

                    @Override
                    public void onFailure(Call<HMConfig> call, Throwable t) {
                        handleDownloadConfigError(t.getMessage());
                    }
                });
    }

    private void updateViews(@NotNull Download download, Reason reason) {
        if (request.getId() == download.getId()) {
            Timber.d("updateViews : %1$s - %2$s", reason.toString(), download.getProgress());
            if (reason == Reason.DOWNLOAD_COMPLETED || reason == Reason.DOWNLOAD_ERROR) {
                if (reason == Reason.DOWNLOAD_ERROR) {
                    Timber.d("Download  Error: %1$s", download.toString());
                }
                readConfig();
            } else if (reason == Reason.DOWNLOAD_WAITING_ON_NETWORK) {
                Toast.makeText(this, "Waiting for network...", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void handleDownloadConfigError(String serverError) {
        //TODO
        Timber.d("Download Config Error: %1$s", serverError);
        Toast.makeText(this, "Default Config", Toast.LENGTH_SHORT).show();
        readConfig();
    }

    private void readConfig() {
        new ParseFileAsyncTask(this)
                .setForceLocalJson(true)
                .setTaskListener(() -> {
                    doingTask--;
                    postExecuteSplashScreen();
                }).execute();
    }


}
