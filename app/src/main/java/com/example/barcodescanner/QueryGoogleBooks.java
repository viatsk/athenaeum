package com.example.barcodescanner;

import androidx.annotation.NonNull;

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
                view.handleFailure(t.toString());
            }
        });
    }
}
