package com.example.barcodescanner;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HTTPClient {
    private static final String APIURL = "https://www.googleapis.com";

    private static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(APIURL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    private static GoogleBooksAPI googleBooksAPI = retrofit.create(GoogleBooksAPI.class);

    public static GoogleBooksAPI getGoogleBooksAPI() {
        return googleBooksAPI;
    }
}
