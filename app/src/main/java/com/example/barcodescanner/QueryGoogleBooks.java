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
    //public static SBook BOOK;

    public Book getBookByISBN(String ISBN) throws Exception {
        final Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("q", "isbn:"+ISBN);
        final Results googleResponse = googleBooksAPI.findBookByISBN(queryParams);
        if (googleResponse == null) {
            System.out.println("GOOGLE BOOKS NO ENTRY");
            throw new Exception("Google Books - no entry for " + ISBN);
        }

        final List<Result> results = googleResponse.getItems();
        if (results == null || results.size() < 1) {
            System.out.println("google books response! ");
        }
        return results.get(0).getBook();
    }
}
