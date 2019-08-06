package com.beyondedge.hm.api;

import android.text.TextUtils;

import com.beyondedge.hm.BuildConfig;
import com.beyondedge.hm.config.HMConfig;
import com.beyondedge.hm.config.LoadConfig;
import com.beyondedge.hm.searchdb.server.SearchServerRepository;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Hoa Nguyen on May 06 2019.
 */
public class ServiceHelper {
    public static final String CONNECT_TIMEOUT = "CONNECT_TIMEOUT";
    public static final String READ_TIMEOUT = "READ_TIMEOUT";
    public static final String WRITE_TIMEOUT = "WRITE_TIMEOUT";

    private static ServiceHelper instance;
    private NetworkAPI networkAPI;
    private NetworkConfigAPI networkConfigAPI;
    private OkHttpClient okHttpClient;

    public ServiceHelper() {
//        networkAPI = createNetworkAPI();
        networkConfigAPI = createNetworkConfigAPI();
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
        if (networkAPI == null) {
            networkAPI = createNetworkAPI();
        }
        return networkAPI;
    }

    public NetworkConfigAPI getNetworkConfigAPI() {
        return networkConfigAPI;
    }

    private OkHttpClient createOKHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        builder.connectTimeout(20, TimeUnit.SECONDS);
        builder.readTimeout(20, TimeUnit.SECONDS);
        builder.writeTimeout(20, TimeUnit.SECONDS);

        Interceptor timeoutInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();

                int connectTimeout = chain.connectTimeoutMillis();
                int readTimeout = chain.readTimeoutMillis();
                int writeTimeout = chain.writeTimeoutMillis();

                String connectNew = request.header(CONNECT_TIMEOUT);
                String readNew = request.header(READ_TIMEOUT);
                String writeNew = request.header(WRITE_TIMEOUT);

                if (!TextUtils.isEmpty(connectNew)) {
                    connectTimeout = Integer.valueOf(connectNew);
                }
                if (!TextUtils.isEmpty(readNew)) {
                    readTimeout = Integer.valueOf(readNew);
                }
                if (!TextUtils.isEmpty(writeNew)) {
                    writeTimeout = Integer.valueOf(writeNew);
                }

                return chain
                        .withConnectTimeout(connectTimeout, TimeUnit.SECONDS)
                        .withReadTimeout(readTimeout, TimeUnit.SECONDS)
                        .withWriteTimeout(writeTimeout, TimeUnit.SECONDS)
                        .proceed(request);
            }
        };

//        builder.addInterceptor(timeoutInterceptor);

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
        HMConfig config = LoadConfig.getInstance().load();

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        okHttpClient = createOKHttpClient();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(config.getVersion().getMainDomain())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build();

        networkAPI = retrofit.create(NetworkAPI.class);

        return networkAPI;
    }

    private NetworkConfigAPI createNetworkConfigAPI() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        okHttpClient = createOKHttpClient();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://hmthuat.specom.io/")// dont care
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build();

        networkConfigAPI = retrofit.create(NetworkConfigAPI.class);

        return networkConfigAPI;
    }
}