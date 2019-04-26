package com.beyondedge.hm;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.beyondedge.hm.searchdb.db.SearchDatabase;
import com.beyondedge.hm.searchdb.db.SearchRepository;
import com.beyondedge.hm.utils.AppExecutors;
import com.crashlytics.android.Crashlytics;
import com.tonyodev.fetch2.Fetch;
import com.tonyodev.fetch2.FetchConfiguration;
import com.tonyodev.fetch2.HttpUrlConnectionDownloader;
import com.tonyodev.fetch2core.Downloader;

import io.fabric.sdk.android.Fabric;
import timber.log.Timber;

/**
 * Created by Hoa Nguyen on Apr 22 2019.
 */
public class HMApplication extends Application {
    private AppExecutors appExecutors;
    private static HMApplication instance;
    public static Application getInstance(){
        return instance;
    }

    public void onCreate() {
        super.onCreate();
        instance = this;
        appExecutors = new AppExecutors();

        boolean isDEBUG = BuildConfig.DEBUG && BuildConfig.LOG;

        final Fabric fabric = new Fabric.Builder(this)
                .kits(new Crashlytics())
                .debuggable(isDEBUG)
                .build();
        Fabric.with(fabric);

        //Timber log
        if (isDEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

        //Fetch download config file
        final FetchConfiguration fetchConfiguration = new FetchConfiguration.Builder(this)
                .enableRetryOnNetworkGain(true)
                .setDownloadConcurrentLimit(3)
                .setHttpDownloader(new HttpUrlConnectionDownloader(getHttpUrlConnectionPreferences(),
                        Downloader.FileDownloaderType.PARALLEL))
                .build();
        Fetch.Impl.setDefaultInstanceConfiguration(fetchConfiguration);
    }

    public SearchDatabase getDatabase() {
        return SearchDatabase.getInstance(this);
    }

    public SearchRepository getRepository() {
        return SearchRepository.getInstance(appExecutors, getDatabase());
    }

    private HttpUrlConnectionDownloader.HttpUrlConnectionPreferences getHttpUrlConnectionPreferences() {
        HttpUrlConnectionDownloader.HttpUrlConnectionPreferences httpUrlConnectionPreferences = new HttpUrlConnectionDownloader.HttpUrlConnectionPreferences();
        httpUrlConnectionPreferences.setConnectTimeout(5000);
        httpUrlConnectionPreferences.setReadTimeout(5000);
        return httpUrlConnectionPreferences;
    }

    public String getVersionName() {
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            return pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return "";
    }


}

