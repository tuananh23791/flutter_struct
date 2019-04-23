package com.beyondedge.hm;

import android.app.Application;

import com.beyondedge.hm.searchdb.db.SearchDatabase;
import com.beyondedge.hm.searchdb.db.SearchRepository;
import com.beyondedge.hm.utils.AppExecutors;
import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;

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
                .debuggable(BuildConfig.DEBUG && BuildConfig.LOG)
                .build();
        Fabric.with(fabric);
    }

    public SearchDatabase getDatabase() {
        return SearchDatabase.getInstance(this);
    }

    public SearchRepository getRepository() {
        return SearchRepository.getInstance(appExecutors, getDatabase());
    }
}

