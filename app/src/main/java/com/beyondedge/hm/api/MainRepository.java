package com.beyondedge.hm.api;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class MainRepository {
    private static MainRepository instance;
    Call<String> queryCall;

    private MutableLiveData<String> mListLiveData = new MutableLiveData<>();
    private Callback<String> callQueryHandle = new Callback<String>() {
        @Override
        public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
            if (call.isCanceled() || mListLiveData == null)
                return;

            if (response.isSuccessful()) {
                mListLiveData.postValue(response.body());
            } else {
                mListLiveData.postValue("");
            }

            queryCall = null;
        }

        @Override
        public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
            if (call.isCanceled())
                return;
            mListLiveData.postValue("");
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


    public LiveData<String> getSearchListLive() {
        return mListLiveData;
    }

    public void clear() {
        cancelQueryCall();
        //TODO ?!
        mListLiveData = new MutableLiveData<>();
    }

    private void cancelQueryCall() {
        if (queryCall != null) {
            queryCall.cancel();
            queryCall = null;
        }
    }

    public void catalogueLookup(String query) {
        Timber.d("Lookup: " + query);
        if (TextUtils.isEmpty(query)) {
            mListLiveData.postValue("");
            cancelQueryCall();
        } else {
            cancelQueryCall();
            queryCall = mServiceHelper.getNetworkAPI().catagolueLookupArticle(query);
            queryCall.enqueue(callQueryHandle);
        }
    }
}
