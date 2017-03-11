package com.app.test.airbnb.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


public abstract class BaseService {

    private static final String HTTPS_DEV = "https://api.airbnb.com/v2/";


    public BaseService() {
    }

    public <T> T buildApi(Class<T> clazz) {

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                .create();

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(chain -> {
            Request original = chain.request();
            Request request = original.newBuilder()
                    .header("Accept", "application/json")

                    .method(original.method(), original.body())
                    .build();

            return chain.proceed(request);
        });
        OkHttpClient client = httpClient.
                readTimeout(60, TimeUnit.SECONDS).
                connectTimeout(60, TimeUnit.SECONDS).
                build();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(HTTPS_DEV)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(client)

                .build();

        return retrofit.create(clazz);
    }

    public String getServerEndpoint() {
        return HTTPS_DEV;
    }

}
