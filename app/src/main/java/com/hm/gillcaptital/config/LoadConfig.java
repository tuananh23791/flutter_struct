package com.hm.gillcaptital.config;

import android.content.Context;

import com.hm.gillcaptital.utils.PrefManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by Hoa Nguyen on Apr 22 2019.
 */
public class LoadConfig {
    private static LoadConfig sSoleInstance;
    private Gson mGson = new GsonBuilder()
            .setLenient()
            .create();
    private HMConfig mHMConfig;
    private boolean forceLoadConfig = false;
    private Context mContext;

    //private constructor.
    private LoadConfig() {

        //Prevent form the reflection api.
        if (sSoleInstance != null) {
            throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
        }
    }

    private LoadConfig(Context context) {
        mContext = context.getApplicationContext();
    }

    public synchronized static LoadConfig getInstance(Context context) {
        if (sSoleInstance == null) { //if there is no instance available... create new one
            sSoleInstance = new LoadConfig(context.getApplicationContext());
        }

        return sSoleInstance;
    }

    public HMConfig load() {
        if (mHMConfig == null) {
            mHMConfig = PrefManager.getInstance(mContext).getCurrentHMConfigJson();
        }

        if (mHMConfig == null) {
            throw new RuntimeException("mHMConfig null now");
        }
        return mHMConfig;
    }

    public void setHMConfig(HMConfig config) {
        if (config == null) {
            throw new RuntimeException("setHMConfig must be != null");
        }

        PrefManager.getInstance(mContext).putCurrentHMConfigJson(config);
        mHMConfig = config;
    }
}
