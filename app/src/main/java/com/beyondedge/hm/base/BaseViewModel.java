package com.beyondedge.hm.base;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

/**
 * Created by Hoa Nguyen on May 06 2019.
 */
public abstract class BaseViewModel extends AndroidViewModel {
    private MutableLiveData<Boolean> getLoadingState = new MutableLiveData<>();
    private boolean initLoading = false;
//    private Observer<Boolean> loadingObserver = isLoading -> {
//        if (isLoading)
//            showLoading();
//        else
//            hideLoading();
//    };

    public BaseViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<Boolean> getGetLoadingState() {
        return getLoadingState;
    }

    protected void showLoading() {
        if (initLoading)
            getLoadingState.postValue(true);
    }

    protected void hideLoading() {
        if (initLoading)
            getLoadingState.postValue(false);
    }

    protected void initLoading() {
        initLoading = true;
//        getGetLoadingState().observeForever(loadingObserver);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
//        if (initLoading)
//            getGetLoadingState().removeObserver(loadingObserver);
    }
}
