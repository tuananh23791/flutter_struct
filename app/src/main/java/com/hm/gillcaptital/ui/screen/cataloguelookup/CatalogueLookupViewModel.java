package com.hm.gillcaptital.ui.screen.cataloguelookup;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.hm.gillcaptital.api.MainRepository;
import com.hm.gillcaptital.base.BaseViewModel;

/**
 * Created by Hoa Nguyen on Apr 25 2019.
 */
public class CatalogueLookupViewModel extends BaseViewModel<String> {
    private MainRepository repository;


    public CatalogueLookupViewModel(@NonNull Application application) {
        super(application);
        repository = MainRepository.getInstance();
//        disableLoading();
    }

    public LiveData<String> getSearchResultLiveData() {
        return getMainLiveData();
    }

    public void catalogueLookup(String query) {
        showLoading();
        repository.catalogueLookup(getApplication(), getMainLiveData(), query);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        repository.clear();
    }

}
