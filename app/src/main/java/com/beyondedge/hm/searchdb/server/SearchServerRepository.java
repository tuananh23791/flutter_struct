package com.beyondedge.hm.searchdb.server;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.beyondedge.hm.BuildConfig;
import com.beyondedge.hm.api.NetworkAPI;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

public class SearchServerRepository {
    private static SearchServerRepository instance;
    Call<ArrayList<SearchEntity>> queryCall;
    private NetworkAPI networkAPI;
    private OkHttpClient okHttpClient;
    private MutableLiveData<List<SearchEntity>> mListLiveData = new MutableLiveData<>();
    ;
    private Callback<ArrayList<SearchEntity>> callQueryHandle = new Callback<ArrayList<SearchEntity>>() {
        @Override
        public void onResponse(@NonNull Call<ArrayList<SearchEntity>> call, @NonNull Response<ArrayList<SearchEntity>> response) {
            if (call.isCanceled() || mListLiveData == null)
                return;

            if (response.isSuccessful()) {
                mListLiveData.postValue(response.body());
            } else {
                mListLiveData.postValue(new ArrayList<>());
            }

            queryCall = null;
        }

        @Override
        public void onFailure(@NonNull Call<ArrayList<SearchEntity>> call, @NonNull Throwable t) {
            if (call.isCanceled())
                return;
            mListLiveData.postValue(new ArrayList<>());
            queryCall = null;

        }
    };

    private SearchServerRepository() {
        networkAPI = createNetworkAPI();
    }

    public static SearchServerRepository getInstance() {
        if (instance == null) {
            synchronized (SearchServerRepository.class) {
                if (instance == null) {
                    instance = new SearchServerRepository();
                }
            }
        }
        return instance;
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
                .client(okHttpClient)
                .build();

        networkAPI = retrofit.create(NetworkAPI.class);

        return networkAPI;
    }

    public LiveData<List<SearchEntity>> getSearchListLive() {
        return mListLiveData;
    }

    public void clear() {
        cancelQueryCall();
    }

    private void cancelQueryCall() {
        if (queryCall != null) {
            queryCall.cancel();
            queryCall = null;
        }
    }

    public void searchQuery(String query) {
        Timber.d("Query: " + query);
        if (TextUtils.isEmpty(query)) {
            mListLiveData.postValue(new ArrayList<>());
            cancelQueryCall();
        } else {
            cancelQueryCall();
            queryCall = networkAPI.searchProductQuery(query, "1555641157245");
            queryCall.enqueue(callQueryHandle);
        }
    }
}
