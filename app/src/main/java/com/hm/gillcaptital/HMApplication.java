package com.hm.gillcaptital;

import android.app.Activity;
import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import androidx.multidex.MultiDex;

import com.hm.gillcaptital.searchdb.db.SearchDatabase;
import com.hm.gillcaptital.searchdb.db.SearchRepository;
import com.hm.gillcaptital.utils.AppExecutors;
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

        registerActivityLifecycleCallbacks(activityCallbacks);
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

    Application.ActivityLifecycleCallbacks activityCallbacks = new ActivityLifecycleCallbacks() {
        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            Log.e(activity.getClass().getSimpleName(),"onActivityCreated");
        }

        @Override
        public void onActivityStarted(Activity activity) {
            Log.e(activity.getClass().getSimpleName(),"onActivityStarted");
        }

        @Override
        public void onActivityResumed(Activity activity) {
            Log.e(activity.getClass().getSimpleName(),"onActivityResumed");
        }

        @Override
        public void onActivityPaused(Activity activity) {
            Log.e(activity.getClass().getSimpleName(),"onActivityPaused");
        }

        @Override
        public void onActivityStopped(Activity activity) {
            Log.e(activity.getClass().getSimpleName(),"onActivityStopped");
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            Log.e(activity.getClass().getSimpleName(),"onActivitySaveInstanceState");
        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            Log.e(activity.getClass().getSimpleName(),"onActivityDestroyed");
}
    };
}

