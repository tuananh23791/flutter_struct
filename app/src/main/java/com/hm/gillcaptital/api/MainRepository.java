package com.hm.gillcaptital.api;

import android.content.Context;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class MainRepository {
    private static MainRepository instance;
    Call<String> queryCall;

    private MutableLiveData<String> mLiveData;

    private Callback<String> callQueryHandle = new Callback<String>() {
        @Override
        public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
            if (call.isCanceled())
                return;

            if (response.isSuccessful()) {
                sendValue(response.body());
            } else {
                sendValue("");
            }

            queryCall = null;
        }

        @Override
        public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
            if (call.isCanceled())
                return;
            sendValue("");
            queryCall = null;

        }
    };

    private ServiceHelper mServiceHelper;

    private MainRepository(ServiceHelper serviceHelper) {
        mServiceHelper = serviceHelper;
    }

    public static MainRepository getInstance() {
        if (instance == null) {
            synchronized (MainRepository.class) {
                if (instance == null) {
                    instance = new MainRepository(ServiceHelper.getInstance());
                }
            }
        }
        return instance;
    }

    public void clear() {
        cancelQueryCall();
        mLiveData = null;
    }

    private void cancelQueryCall() {
        if (queryCall != null) {
            queryCall.cancel();
            queryCall = null;
        }
    }

    public void catalogueLookup(Context context,MutableLiveData<String> liveData, String query) {
        Timber.d("Lookup: " + query);
        mLiveData = liveData;
        if (TextUtils.isEmpty(query)) {
            sendValue("");
            cancelQueryCall();
        } else {
            cancelQueryCall();
            queryCall = mServiceHelper.getNetworkAPI(context).catagolueLookupArticle(query);
            queryCall.enqueue(callQueryHandle);
        }
    }

    private void sendValue(String value) {
        if (mLiveData != null)
            mLiveData.postValue(value);
    }

}
