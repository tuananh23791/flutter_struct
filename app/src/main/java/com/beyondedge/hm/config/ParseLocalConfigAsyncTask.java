package com.beyondedge.hm.config;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.TextView;

import com.beyondedge.hm.BuildConfig;
import com.beyondedge.hm.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.io.File;
import java.lang.ref.WeakReference;

import timber.log.Timber;

import static com.beyondedge.hm.config.Constant.IS_FORCE_LOCAL_CONFIG;
import static com.beyondedge.hm.config.Constant.IS_FORCE_LOCAL_THAI_CONFIG;

/**
 * Created by Hoa Nguyen on Apr 23 2019.
 */
public class ParseLocalConfigAsyncTask extends AsyncTask<Void, Void, HMConfig> {
    private final Context mContext;
    private Gson mGson = new GsonBuilder()
            .setLenient()
            .create();
    private TaskListener mTaskListener;

    public ParseLocalConfigAsyncTask(Context context) {
        mContext = context.getApplicationContext();
    }

    public ParseLocalConfigAsyncTask setTaskListener(TaskListener taskListener) {
        mTaskListener = taskListener;
        return this;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected HMConfig doInBackground(Void... input) {
        return parseLocalFile();
    }

    private HMConfig parseLocalFile() {
        HMConfig result = null;
        String value;

        value = Utils.loadJSONFromAsset(mContext);
        try {
            Timber.e("------------H&M------------- parse Local");
            result = mGson.fromJson(value, HMConfig.class);
        } catch (JsonSyntaxException e) {
            Timber.e(e);
        }

        return result;
    }

    private HMConfig parseLocalFileTH_TH() {
        HMConfig result = null;
        String value;

        value = Utils.loadJSONFromAsset(mContext, R.raw.th_th);
        try {
            Timber.e("------------H&M---TH_TH.txt---------- parse Local");
            result = mGson.fromJson(value, HMConfig.class);
        } catch (JsonSyntaxException e) {
            Timber.e(e);
        }

        return result;
    }

    @Override
    protected void onPostExecute(HMConfig hmConfig) {
        super.onPostExecute(hmConfig);

        LoadConfig.getInstance(mContext).setHMConfig(hmConfig);

        if (mTaskListener != null) {
            mTaskListener.onDone();
        }
    }

    public interface TaskListener {
        void onDone();
    }
}