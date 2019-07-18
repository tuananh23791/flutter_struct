package com.beyondedge.hm.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.beyondedge.hm.MainActivity;
import com.beyondedge.hm.R;
import com.beyondedge.hm.api.ServiceHelper;
import com.beyondedge.hm.config.HMConfig;
import com.beyondedge.hm.config.LoadConfig;
import com.beyondedge.hm.config.ParseLocalConfigAsyncTask;
import com.beyondedge.hm.utils.AppVersion;
import com.beyondedge.hm.utils.PrefManager;
import com.beyondedge.hm.utils.URLUtils;
import com.daimajia.androidanimations.library.Techniques;
import com.viksaa.sssplash.lib.activity.AwesomeSplash;
import com.viksaa.sssplash.lib.cnst.Flags;
import com.viksaa.sssplash.lib.model.ConfigSplash;

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
public class SplashScreen extends AwesomeSplash {
    private int doingTask = 0;

    public static void startActivity(Activity from) {
        Intent intent = new Intent(from, SplashScreen.class);
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
                boolean isShowDialog = AppVersion.isLastVersion(this,
                        config.getVersion().getVersionAndroidForceUpdate());

                boolean canSkip = AppVersion.versionContains(this,
                        config.getVersion().getVersionAndroidForceUpdate());

                if (isShowDialog) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SplashScreen.this)
                            .setMessage(config.getLanguageBy("AlertNewVersion"))
                            .setPositiveButton(config.getLanguageBy("NewVersion_update"),
                                    (dialog, which) -> {
                                        dialog.dismiss();
                                        SplashScreen.this.finish();
                                        URLUtils.openInWebBrowser(SplashScreen.this,
                                                "https://play.google.com/store/apps?hl=en");
                                    });

                    if (!canSkip) {
                        builder.setNegativeButton(config.getLanguageBy("NewVersion_cancel"), (dialog, which) -> {
                            dialog.dismiss();
                            directToMainScreen();
                        });
                        builder.setCancelable(true);
                    } else {
                        builder.setCancelable(false);
                        builder.setNegativeButton(config.getLanguageBy("NewVersion_cancel"), (dialog, which) -> {
                            dialog.dismiss();
                            SplashScreen.this.finish();
                        });
                    }

                    builder.show();
                } else {
                    directToMainScreen();
                }
            } else {
                directToMainScreen();
            }
        }
    }

    private void directToMainScreen() {
        startActivity(new Intent(SplashScreen.this, MainActivity.class));
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
            readLocalConfig();
        } else {
            enqueueLoadConfigByAPI();
        }
    }


    private void enqueueLoadConfigByAPI() {
        doingTask++;
        final String url = PrefManager.getInstance().getCurrentLinkConfig();
        ServiceHelper.getInstance().getNetworkAPI().loadConfig(url)
                .enqueue(new Callback<HMConfig>() {
                    @Override
                    public void onResponse(Call<HMConfig> call, Response<HMConfig> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            LoadConfig.getInstance().setHMConfig(response.body());
                            doingTask--;
                            postExecuteSplashScreen();
                        } else {
                            handleLoadServerConfigError("");
                        }
                    }

                    @Override
                    public void onFailure(Call<HMConfig> call, Throwable t) {
                        handleLoadServerConfigError(t.getMessage());
                    }
                });
    }

    private void handleLoadServerConfigError(String serverError) {
        //TODO
        Timber.d("Download Config Error: %1$s", serverError);
        Toast.makeText(this, "Default Config", Toast.LENGTH_SHORT).show();
        readLocalConfig();
    }

    private void readLocalConfig() {
        new ParseLocalConfigAsyncTask(this)
                .setTaskListener(() -> {
                    doingTask--;
                    postExecuteSplashScreen();
                }).execute();
    }
}
