package com.example.barcodescanner;

import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class ButtonActivity implements View.OnClickListener {
    private FirebaseFirestore db;
    private Map<String, Object> book;

    public ButtonActivity(Boolean have, Map bk, FirebaseFirestore db) {
        this.db = db;
        bk.put("own", have);
        this.book = bk;
    }

    @Override
    public void onClick(View view) {
        db.collection("library")
                .add(book)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Snackbar.make(view, R.string.db_success, Snackbar.LENGTH_SHORT)
                                .show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Snackbar.make(view, R.string.db_failure, Snackbar.LENGTH_SHORT)
                                .show();
                    }
                });

    }
}
