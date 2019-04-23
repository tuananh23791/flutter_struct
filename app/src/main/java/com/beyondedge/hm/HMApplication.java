package com.beyondedge.hm;

import android.app.Application;

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

    public void onCreate() {
        super.onCreate();
        appExecutors = new AppExecutors();

        final Fabric fabric = new Fabric.Builder(this)
                .kits(new Crashlytics())
                .debuggable(BuildConfig.DEBUG && !BuildConfig.LOG)
                .build();
        Fabric.with(fabric);

        //Timber log
        if (BuildConfig.DEBUG && BuildConfig.LOG) {
            Timber.plant(new Timber.DebugTree());
        }

        //Fetch download config file
        final FetchConfiguration fetchConfiguration = new FetchConfiguration.Builder(this)
                .enableRetryOnNetworkGain(true)
                .setDownloadConcurrentLimit(3)
                .setHttpDownloader(new HttpUrlConnectionDownloader(Downloader.FileDownloaderType.PARALLEL))
                .build();
        Fetch.Impl.setDefaultInstanceConfiguration(fetchConfiguration);
    }

    public SearchDatabase getDatabase() {
        return SearchDatabase.getInstance(this);
    }

    public SearchRepository getRepository() {
        return SearchRepository.getInstance(appExecutors, getDatabase());
    }
}

