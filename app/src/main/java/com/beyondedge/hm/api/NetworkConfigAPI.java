package com.beyondedge.hm.api;

import com.beyondedge.hm.config.HMConfig;
import com.beyondedge.hm.searchdb.server.SearchEntity;

import java.util.ArrayList;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Created by Hoa Nguyen on Apr 24 2019.
 */
public interface NetworkConfigAPI {

//    @Headers({"CONNECT_TIMEOUT:10", "READ_TIMEOUT:5", "WRITE_TIMEOUT:5"})
    @GET
    Call<HMConfig> loadConfig(@Url String url);
}
