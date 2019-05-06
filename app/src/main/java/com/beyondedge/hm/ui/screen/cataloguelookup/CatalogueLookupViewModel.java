package com.beyondedge.hm.ui.screen.cataloguelookup;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.beyondedge.hm.api.MainRepository;
import com.beyondedge.hm.base.BaseViewModel;

/**
 * Created by Hoa Nguyen on Apr 25 2019.
 */
public class CatalogueLookupViewModel extends BaseViewModel {

    private MainRepository repository;
    private LiveData<String> getSearchListLive;
    private Observer<String> mStringObserver = new Observer<String>() {
        @Override
        public void onChanged(String s) {
            hideLoading();
        }
    };

    public CatalogueLookupViewModel(@NonNull Application application) {
        super(application);
        repository = MainRepository.getInstance();
        initLoading();
        initSearch();
    }

    private void initSearch() {
        getSearchListLive = repository.getSearchListLive();
        getSearchListLive.observeForever(mStringObserver);
    }

    public LiveData<String> getSearchListLive() {
        return getSearchListLive;
    }

    public void catalogueLookup(String query) {
        showLoading();
        //TODO loading hear
        repository.catalogueLookup(query);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        getSearchListLive.removeObserver(mStringObserver);
        repository.clear();
    }

}
