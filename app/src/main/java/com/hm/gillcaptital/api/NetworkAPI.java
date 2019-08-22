package com.hm.gillcaptital.api;

import com.hm.gillcaptital.searchdb.server.SearchEntity;

import java.util.ArrayList;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Hoa Nguyen on Apr 24 2019.
 */
public interface NetworkAPI {

    @GET("search/ajax/suggest")
    Call<ArrayList<SearchEntity>> searchProductQuery(
            @Query("q") String query,
            @Query("_") String token);

    @GET("search/ajax/suggest")
    Observable<ArrayList<SearchEntity>> searchProductQueryRx(
            @Query("q") String query,
            @Query("_") String token);

    @GET("rest/V1/getProductsBySearch/articlenumber/{article}")
    Call<String> catagolueLookupArticle(
            @Path("article") String article);
}
