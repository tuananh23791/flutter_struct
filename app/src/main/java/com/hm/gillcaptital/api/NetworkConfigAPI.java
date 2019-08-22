package com.hm.gillcaptital.api;

import com.hm.gillcaptital.config.HMConfig;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * Created by Hoa Nguyen on Apr 24 2019.
 */
public interface NetworkConfigAPI {

//    @Headers({"CONNECT_TIMEOUT:10", "READ_TIMEOUT:5", "WRITE_TIMEOUT:5"})
    @GET
    Call<HMConfig> loadConfig(@Url String url);
}
