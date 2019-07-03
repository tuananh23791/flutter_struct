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
public class ParseFileAsyncTask extends AsyncTask<Void, Void, HMConfig> {
    final String filePath = Constant.getLinkSavedFile();
    final Context mContext;
    private boolean isValidSavedFile = false;
    private Gson mGson = new GsonBuilder()
            .setLenient()
            .create();
    private WeakReference<TextView> textViewRef;
    private TaskListener mTaskListener;

    private boolean isForceLocalJson = false;

    public ParseFileAsyncTask(Context context) {
        mContext = context.getApplicationContext();
    }

    public ParseFileAsyncTask setTaskListener(TaskListener taskListener) {
        mTaskListener = taskListener;
        return this;
    }

    public ParseFileAsyncTask setTextView(TextView textView) {
        textViewRef = new WeakReference<>(textView);
        return this;
    }

    public ParseFileAsyncTask setForceLocalJson(boolean forceLocalJson) {
        isForceLocalJson = forceLocalJson;
        return this;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        File file = new File(filePath);
        isValidSavedFile = file.exists();
    }

    @Override
    protected HMConfig doInBackground(Void... input) {
        HMConfig result = null;

        if (isForceLocalJson || IS_FORCE_LOCAL_CONFIG) {
            result = parseLocalFile();
        } else {
            if (isValidSavedFile) {
                //parse server first
                result = parseServerFile();
            }

            if (result == null) {
                result = parseLocalFile();
            }

        }


        return result;
    }

    private HMConfig parseServerFile() {
        HMConfig result = null;
        String value;

        value = Utils.loadJSONFromDir(filePath);

        if (BuildConfig.DEBUG && BuildConfig.LOG && IS_FORCE_LOCAL_THAI_CONFIG
                && "TH-TH.txt".equals(Uri.parse(filePath).getLastPathSegment())) {
            return parseLocalFileTH_TH();
        } else {

            try {
                Timber.e("------------H&M------------- parse Server");
                result = mGson.fromJson(value, HMConfig.class);
            } catch (JsonSyntaxException e) {
                Timber.e("------------H&M------------- parse Config error");
//                Timber.e(value);
                Timber.e("------------------------- parse Config error");
                Timber.e(e);
            } catch (Exception e) {
                Timber.e("------------H&M------------- parse Config error");
//                Timber.e(value);
                Timber.e("------------------------- parse Config error");
                Timber.e(e);
            }
        }

        return result;
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
        if (textViewRef != null) {
            TextView textView = textViewRef.get();
            if (textView != null) {
                textView.setText(hmConfig != null ? mGson.toJson(hmConfig) : "Config == null");
            }
        }

        LoadConfig.getInstance().setHMConfig(hmConfig);

        if (mTaskListener != null) {
            mTaskListener.onDone();
        }
    }

    public interface TaskListener {
        void onDone();
    }
}