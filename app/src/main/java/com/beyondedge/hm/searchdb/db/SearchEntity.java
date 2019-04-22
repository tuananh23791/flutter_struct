package com.beyondedge.hm.searchdb.db;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "search_table")
public class SearchEntity {
    @PrimaryKey
    @NonNull
    private String text;
    private long timestamp;

    public SearchEntity(@NonNull String text, long timestamp) {
        this.text = text;
        this.timestamp = timestamp;
    }

    @NonNull
    public String getText() {
        return text;
    }

    public void setText(@NonNull String text) {
        this.text = text;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
