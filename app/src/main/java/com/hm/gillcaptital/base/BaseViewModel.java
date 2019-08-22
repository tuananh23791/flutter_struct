package com.hm.gillcaptital.base;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

/**
 * Created by Hoa Nguyen on May 06 2019.
 */
public abstract class BaseViewModel<T> extends AndroidViewModel {
    private MutableLiveData<Boolean> loadingState = new MutableLiveData<>();
    private boolean initLoading = true;
    private Observer<T> mLoadingObserver = s -> hideLoading();
    private MutableLiveData<T> liveDataLoading;
    private MutableLiveData<T> mMainLiveData;

    public BaseViewModel(@NonNull Application application) {
        super(application);
        initMainViewModel();
        initLoadingWithLiveData(mMainLiveData);
    }

    protected void disableLoading() {
        initLoading = false;
    }

    private void initMainViewModel() {
        mMainLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<T> getMainLiveData() {
        return mMainLiveData;
    }

    public LiveData<Boolean> getGetLoadingState() {
        return loadingState;
    }

    protected void showLoading() {
        if (initLoading)
            loadingState.postValue(true);
    }

    protected void hideLoading() {
        if (initLoading)
            loadingState.postValue(false);
    }

    protected void initLoadingWithLiveData(MutableLiveData<T> data) {
        liveDataLoading = data;
        data.observeForever(mLoadingObserver);
    }

    @Override
    protected void onCleared() {
        if (initLoading && liveDataLoading != null) {
            liveDataLoading.removeObserver(mLoadingObserver);
            liveDataLoading = null;
        }
        super.onCleared();
    }
}
