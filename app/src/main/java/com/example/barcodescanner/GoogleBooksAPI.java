package com.example.barcodescanner;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GoogleBooksAPI {
    @GET("/books/v1/volumes?q=isbn:{ISBN}")
    Call<Result> findBookByISBN(@Query("") String ISBN);
}
