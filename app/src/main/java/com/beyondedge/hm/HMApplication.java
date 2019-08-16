package com.beyondedge.hm;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import androidx.multidex.MultiDex;

import com.beyondedge.hm.searchdb.db.SearchDatabase;
import com.beyondedge.hm.searchdb.db.SearchRepository;
import com.beyondedge.hm.utils.AppExecutors;
import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.core.CrashlyticsCore;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.fabric.sdk.android.Fabric;
import timber.log.Timber;

/**
 * Created by Hoa Nguyen on Apr 22 2019.
 */
public class HMApplication extends Application {
    private static HMApplication instance;
    private static Gson mGson = new GsonBuilder()
            .setLenient()
            .create();
    private AppExecutors appExecutors;

    public static Gson getGson() {
        return mGson;
    }

    public static Application getInstance() {
        return instance;
    }

    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
        instance = this;
        appExecutors = new AppExecutors();

        boolean isDEBUG = BuildConfig.DEBUG && BuildConfig.LOG;

        // Initializes Fabric for builds that don't use the debug build type.
        Crashlytics kit = new Crashlytics.Builder()
                .core(new CrashlyticsCore.Builder()
                        .disabled(isDEBUG)
                        .build())
                .build();

        Fabric.with(this, kit);

        //Timber log
        if (isDEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }

    public SearchDatabase getDatabase() {
        return SearchDatabase.getInstance(this);
    }

    public SearchRepository getRepository() {
        return SearchRepository.getInstance(appExecutors, getDatabase());
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

