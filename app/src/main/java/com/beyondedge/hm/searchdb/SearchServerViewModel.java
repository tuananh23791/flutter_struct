package com.beyondedge.hm.searchdb;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.beyondedge.hm.searchdb.server.SearchEntity;
import com.beyondedge.hm.searchdb.server.SearchServerRepository;

import java.util.List;

/**
 * Created by Hoa Nguyen on Apr 22 2019.
 */
public class SearchServerViewModel extends AndroidViewModel {
    private SearchServerRepository repository;
    private LiveData<List<SearchEntity>> getSearchListLive;

    public SearchServerViewModel(@NonNull Application application) {
        super(application);
        repository = SearchServerRepository.getInstance();
        initSearch();
    }

    private void initSearch() {
        getSearchListLive = repository.getSearchListLive();
    }

    public LiveData<List<SearchEntity>> getSearchListLive() {
        return getSearchListLive;
    }

    public void searchQuery(String query) {
        repository.searchQuery(query);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        repository.clear();
    }

}
