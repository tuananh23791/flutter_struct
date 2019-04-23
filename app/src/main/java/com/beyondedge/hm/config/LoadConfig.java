package com.beyondedge.hm.config;

import com.beyondedge.hm.utils.Constant;
import com.google.gson.Gson;

/**
 * Created by Hoa Nguyen on Apr 22 2019.
 */
public class LoadConfig {
    private static LoadConfig sSoleInstance;
    private Gson mGson = new Gson();
    private HMConfig mHMConfig;
    private boolean forceLoadConfig = false;

    //private constructor.
    private LoadConfig() {

        //Prevent form the reflection api.
        if (sSoleInstance != null) {
            throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
        }
    }

    public synchronized static LoadConfig getInstance() {
        if (sSoleInstance == null) { //if there is no instance available... create new one
            sSoleInstance = new LoadConfig();
        }

        return sSoleInstance;
    }

    public HMConfig load() {
        if (mHMConfig == null) {
            throw new RuntimeException("mHMConfig null now");
        }
        return mHMConfig;
    }

    public void setHMConfig(HMConfig config) {
        if (config == null) {
            throw new RuntimeException("setHMConfig must be != null");
        }
        mHMConfig = config;
    }
}
