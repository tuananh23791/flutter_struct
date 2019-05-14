package com.beyondedge.hm.api;

import com.beyondedge.hm.BuildConfig;
import com.beyondedge.hm.searchdb.server.SearchServerRepository;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Hoa Nguyen on May 06 2019.
 */
public class ServiceHelper {

    private static ServiceHelper instance;
    private NetworkAPI networkAPI;
    private OkHttpClient okHttpClient;

    public ServiceHelper() {
        networkAPI = createNetworkAPI();
    }

    public static ServiceHelper getInstance() {
        if (instance == null) {
            synchronized (SearchServerRepository.class) {
                if (instance == null) {
                    instance = new ServiceHelper();
                }
            }
        }
        return instance;
    }

    public NetworkAPI getNetworkAPI() {
        return networkAPI;
    }

    private OkHttpClient createOKHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        builder.connectTimeout(60, TimeUnit.SECONDS);
        builder.readTimeout(60, TimeUnit.SECONDS);
        builder.writeTimeout(60, TimeUnit.SECONDS);

//        builder.addInterceptor(new Interceptor() {
//            @Override
//            public Response intercept(@NonNull Interceptor.Chain chain) throws IOException {
//                //this is where we will add whatever we want to our request headers.
//                Request basicRequest = chain.request();
//                Request.Builder requestBuilder = basicRequest.newBuilder();
//                requestBuilder.addHeader(HEADER_AUTHORIZATION, getAuthorizationParam());
//
//                return chain.proceed(requestBuilder.build());
//            }
//        });

        if (BuildConfig.LOG) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(interceptor);
        }

        return builder.build();
    }

    private NetworkAPI createNetworkAPI() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();

        okHttpClient = createOKHttpClient();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://hmthuat.specom.io/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build();

        networkAPI = retrofit.create(NetworkAPI.class);

        return networkAPI;
    }


}
