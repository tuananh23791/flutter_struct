package com.hm.gillcaptital.searchdb.db;



import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public abstract class SearchDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertSearches(SearchEntity... searchEntities);

    @Delete
    public abstract void deleteSearches(SearchEntity... searchEntities);

    @Query("SELECT * FROM search_table ORDER BY timestamp DESC LIMIT 5")
    public abstract List<SearchEntity> getSearchList();

    @Query("SELECT * FROM search_table ORDER BY timestamp DESC LIMIT 5")
    public abstract LiveData<List<SearchEntity>> getSearchListLive();

}
