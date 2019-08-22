package com.hm.gillcaptital.searchdb;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.hm.gillcaptital.HMApplication;
import com.hm.gillcaptital.searchdb.db.SearchEntity;
import com.hm.gillcaptital.searchdb.db.SearchRepository;

import java.util.Date;
import java.util.List;

/**
 * Created by Hoa Nguyen on Apr 22 2019.
 */
public class SearchViewModel extends AndroidViewModel {
    private SearchRepository repository;
    private LiveData<List<SearchEntity>> getSearchListLive;

    public SearchViewModel(@NonNull Application application) {
        super(application);
        repository = ((HMApplication) application).getRepository();
        initSearch();
    }

    private void initSearch() {
        getSearchListLive = repository.getSearchListLive();
    }

    public void insertSearchEntity(String query) {
        repository.insertSearches(new SearchEntity(query, new Date().getTime()));
    }

    public void deleteSearchEntity(SearchEntity searchEntity) {
        repository.deleteSearches(searchEntity);
    }

    public LiveData<List<SearchEntity>> getSearchListLive() {
        return getSearchListLive;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }

}
