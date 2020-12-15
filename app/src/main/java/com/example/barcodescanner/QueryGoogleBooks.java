package com.example.barcodescanner;

import android.os.AsyncTask;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QueryGoogleBooks {
    private static final int TIMEOUT = 5000;
    private GoogleBooksAPI googleBooksAPI = HTTPClient.getGoogleBooksAPI();
    public static SBook BOOK;

    protected void getBookByISBN(String ISBN) throws Exception {
        final Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("q", "isbn:"+ISBN);
        final Call<Result> googleResponse = googleBooksAPI.findBookByISBN(ISBN);
        if (googleResponse == null) {
            System.out.println("GOOGLE BOOKS NO ENTRY");
            throw new Exception("Google Books - no entry for " + ISBN);
        }

        googleResponse.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                final List<Book> results = response.body().getItems();
                if (results == null || results.size() < 1) {
                    System.out.println("google books response! ");
                }
                BOOK = results.get(0).getBook();
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }
}
