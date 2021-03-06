package com.hm.gillcaptital.api;

import android.content.Context;
import android.text.TextUtils;
import android.util.Base64;

import androidx.annotation.NonNull;

import com.hm.gillcaptital.BuildConfig;
import com.hm.gillcaptital.config.HMConfig;
import com.hm.gillcaptital.config.LoadConfig;
import com.hm.gillcaptital.searchdb.server.SearchServerRepository;
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

import static io.fabric.sdk.android.services.network.HttpRequest.HEADER_AUTHORIZATION;

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

    public NetworkAPI getNetworkAPI(Context context) {
        if (networkAPI == null) {
            HMConfig config = LoadConfig.getInstance(context).load();
            networkAPI = createNetworkAPI(context, config.getVersion().getMainDomain());
        }
        return networkAPI;
    }

    public NetworkConfigAPI getNetworkConfigAPI() {
        return networkConfigAPI;
    }

    private OkHttpClient createOKHttpClient(String mainDomain) {
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


        if (!TextUtils.isEmpty(mainDomain)) {
            builder.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(@NonNull Interceptor.Chain chain) throws IOException {
                    //this is where we will add whatever we want to our request headers.
                    Request basicRequest = chain.request();
                    Request.Builder requestBuilder = basicRequest.newBuilder();
                    requestBuilder.addHeader(HEADER_AUTHORIZATION, getAuthorizationParam(mainDomain));

                    return chain.proceed(requestBuilder.build());
                }
            });
        }

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

    private String getAuthorizationParam(String mainDomain) {
        String username = "";
        String password = "";

        if (!TextUtils.isEmpty(mainDomain)) {
            if (mainDomain.contains("hmid")) {
                username = "WCiGosSjqWqcg";
                password = "uKM_WFNr-o-u1";
            } else if (mainDomain.contains("hmth")) {
                username = "evjulzVRQrnA4";
                password = "YgNscTYNjw_E_23";
            }
        }

        if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {
            // concatenate username and password with colon for authentication
            String credentials = username + ":" + password;
            // create Base64 encodet string
            return "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
        }

        return "";
    }

    private NetworkAPI createNetworkAPI(Context context, String mainDomain) {
        HMConfig config = LoadConfig.getInstance(context).load();

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        okHttpClient = createOKHttpClient(mainDomain);
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

        okHttpClient = createOKHttpClient("");
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