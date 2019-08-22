package com.hm.gillcaptital.searchdb.db;

import androidx.lifecycle.LiveData;

import com.hm.gillcaptital.utils.AppExecutors;

import java.util.List;


public class SearchRepository {
    private static SearchRepository instance;
    private final SearchDatabase database;
    private final AppExecutors appExecutors;

    private SearchRepository(AppExecutors appExecutors, final SearchDatabase database) {
        this.appExecutors = appExecutors;
        this.database = database;
    }

    public static SearchRepository getInstance(final AppExecutors appExecutors, final SearchDatabase database) {
        if (instance == null) {
            synchronized (SearchRepository.class) {
                if (instance == null) {
                    instance = new SearchRepository(appExecutors, database);
                }
            }
        }
        return instance;
    }

    public void insertSearches(final SearchEntity... searchEntities) {
        appExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                database.getSearchDao().insertSearches(searchEntities);
            }
        });
    }

    public void deleteSearches(final SearchEntity... searchEntities) {
        appExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                database.getSearchDao().deleteSearches(searchEntities);
            }
        });
    }

    public LiveData<List<SearchEntity>> getSearchListLive() {
        return database.getSearchDao().getSearchListLive();
    }

}
