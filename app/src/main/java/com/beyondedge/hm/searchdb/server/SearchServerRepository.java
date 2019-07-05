package com.beyondedge.hm.searchdb.server;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.beyondedge.hm.api.ServiceHelper;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class SearchServerRepository {
    private static SearchServerRepository instance;
    private Call<ArrayList<SearchEntity>> queryCall;

    private MutableLiveData<List<SearchEntity>> mListLiveData;
    private Callback<ArrayList<SearchEntity>> callQueryHandle = new Callback<ArrayList<SearchEntity>>() {
        @Override
        public void onResponse(@NonNull Call<ArrayList<SearchEntity>> call, @NonNull Response<ArrayList<SearchEntity>> response) {
            if (call.isCanceled() || mListLiveData == null)
                return;

            if (response.isSuccessful()) {
                mListLiveData.postValue(response.body());
            } else {
                mListLiveData.postValue(new ArrayList<>());
            }

            queryCall = null;
        }

        @Override
        public void onFailure(@NonNull Call<ArrayList<SearchEntity>> call, @NonNull Throwable t) {
            if (call.isCanceled() || mListLiveData == null)
                return;
            mListLiveData.postValue(new ArrayList<>());
            queryCall = null;

        }
    };

    private ServiceHelper mServiceHelper;

    private SearchServerRepository(ServiceHelper serviceHelper) {
        mServiceHelper = serviceHelper;
    }

    public static SearchServerRepository getInstance() {
        if (instance == null) {
            synchronized (SearchServerRepository.class) {
                if (instance == null) {
                    instance = new SearchServerRepository(ServiceHelper.getInstance());
                }
            }
        }
        return instance;
    }

    public void clear() {
//        mListLiveData.postValue(new ArrayList<>());
        mListLiveData = null;
        cancelQueryCall();
    }

    private void cancelQueryCall() {
        if (queryCall != null) {
            queryCall.cancel();
            queryCall = null;
        }
    }

    public void searchQuery(MutableLiveData<List<SearchEntity>> liveData, String query) {
        Timber.d("Query: " + query);
        mListLiveData = liveData;
        if (mListLiveData == null) {
            return;
        }
        if (TextUtils.isEmpty(query)) {
            mListLiveData.postValue(new ArrayList<>());
            cancelQueryCall();
        } else {
            cancelQueryCall();
            queryCall = mServiceHelper.getNetworkAPI().searchProductQuery(query, "1555641157245");
            queryCall.enqueue(callQueryHandle);
        }
    }

    public Observable<ArrayList<SearchEntity>> searchQuery(String query) {
        Timber.d("Query: " + query);
        return mServiceHelper.getNetworkAPI().searchProductQueryRx(query, "1555641157245");
    }
}
