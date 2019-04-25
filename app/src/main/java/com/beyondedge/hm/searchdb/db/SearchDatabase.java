package com.beyondedge.hm.searchdb.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = SearchEntity.class, version = 1, exportSchema = false)
public abstract class SearchDatabase extends RoomDatabase {
    private static final String DATABASE_NAME = "search-db";
    private static SearchDatabase instance;

    public static SearchDatabase getInstance(final Context context) {
        if (instance == null) {
            synchronized (SearchDatabase.class) {
                if (instance == null) {
                    instance = buildDatabase(context.getApplicationContext());
                }
            }
        }
        return instance;
    }

    private static SearchDatabase buildDatabase(final Context context) {
        return Room.databaseBuilder(context, SearchDatabase.class, DATABASE_NAME).build();
    }

    public abstract SearchDao getSearchDao();

}
