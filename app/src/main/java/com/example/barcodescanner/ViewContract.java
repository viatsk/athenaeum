package com.example.barcodescanner;

import com.example.barcodescanner.Results;

public interface ViewContract {
    interface View {
        // void showLoadingBar
        void handleSuccess(Results response);
        void handleFailure(String message);
    }

    interface Presenter {
        void search(String isbn);
    }
}
