package com.beyondedge.hm.api;

import com.beyondedge.hm.searchdb.server.SearchEntity;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Hoa Nguyen on Apr 24 2019.
 */
public interface NetworkAPI {
    @GET("search/ajax/suggest")
    Call<ArrayList<SearchEntity>> searchProductQuery(
            @Query("q") String query,
            @Query("_") String token);
}
