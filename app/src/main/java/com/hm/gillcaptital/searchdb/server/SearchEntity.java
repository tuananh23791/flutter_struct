package com.hm.gillcaptital.searchdb.server;

import com.hm.gillcaptital.searchdb.SearchInterface;

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
