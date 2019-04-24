package com.beyondedge.hm.searchdb.server;

import com.beyondedge.hm.searchdb.SearchInterface;

/**
 * Created by Hoa Nguyen on Apr 24 2019.
 */
public class SearchEntity implements SearchInterface {
    private String title;
    private String num_results;

    public String getNumResults() {
        return num_results;
    }

    @Override
    public String getText() {
        return title;
    }
}
