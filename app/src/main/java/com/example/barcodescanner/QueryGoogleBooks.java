package com.example.barcodescanner;

import android.os.AsyncTask;

import androidx.annotation.NonNull;

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

public class QueryGoogleBooks implements ViewContract.Presenter {
    private ViewContract.View view;
    private static final int TIMEOUT = 5000;
    private GoogleBooksAPI googleBooksAPI = HTTPClient.getGoogleBooksAPI();
    //public static SBook BOOK;

    QueryGoogleBooks(@NonNull ViewContract.View view) {
        this.view = view;
    }

    @Override
    public void search(String isbn) {
        Call<Results> call = googleBooksAPI.findBookByISBN(isbn);
        call.enqueue(new Callback<Results>() {
            @Override
            public void onResponse(Call<Results> call, Response<Results> response) {
                if (response != null && response.isSuccessful()) {
                    view.handleSuccess(response.body());
                }
            }

            @Override
            public void onFailure(Call<Results> call, Throwable t) {
                view.handleFailure("There was an error in the search");
            }
        });
    }
}
