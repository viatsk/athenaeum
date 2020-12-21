package com.example.barcodescanner;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface GoogleBooksAPI {
    @GET("/books/v1/volumes/")
    Results findBookByISBN(@Query("q") String ISBN);
}
