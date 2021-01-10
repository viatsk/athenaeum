package com.example.barcodescanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;


import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.MultiProcessor;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity implements ViewContract.View, BarcodeTracker.NewBarcodeInterface {

    private CameraSource cameraSource;
    private static final int REQUEST_CAMERA_PERMISSION = 201;

    private TextView barcodeText;
    private TextView bookText;
    private CameraResizing customView;
    private ImageView bookImage;

    private ViewContract.Presenter presenter;
    private Button haveButton;
    private Button wantButton;

    public int resCounter = 0;

    public FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        presenter = new QueryGoogleBooks(this);
        customView = findViewById(R.id.surface_view); // Bind surface view
        barcodeText = findViewById(R.id.barcode_text); // Bind barcode text
        bookText = findViewById(R.id.bookinfo_text); // Bind book text
        bookImage = findViewById(R.id.book_image); // Bind image view
        haveButton = findViewById(R.id.have_button); // Bind have button
        wantButton = findViewById(R.id.want_button); // Bind want button
        db = FirebaseFirestore.getInstance();
        initCamera();
    }

    private boolean validateISBN(String ISBN) {
        int sum = 0;
        if (ISBN.length() != 13)
            return false;
        for (int i = 0; i < ISBN.length(); i++) {
            sum = sum + ISBN.charAt(i) * ((i %2 == 0)? 1:3);
        }
        return (sum%10 == 0);
    }

    @Override
    public void onBarcodeDetected(Barcode barcode) {
        // Use barcode data
        String bcVal = barcode.rawValue;
        barcodeText.setText(bcVal);
        if (!validateISBN(bcVal)) {
            bookText.setText(R.string.barcode_error);
        }
        else {
            presenter.search(bcVal);
        }
    }

    public void registerButtons(Book book) {
        Map<String, Object> bk = new HashMap<>();
        bk.put("title", book.getVolumeInfo().getTitle());
        bk.put("authors", book.getVolumeInfo().getAuthors());
        haveButton.setOnClickListener(new ButtonActivity(Boolean.TRUE, bk, db));
        wantButton.setOnClickListener(new ButtonActivity(Boolean.FALSE, bk, db));

    }

    @Override
    public void handleSuccess(Results results) {
        if (results.getItems() != null) {
            bookText.setText(results.getItemByIndex(resCounter).getVolumeInfo().toString());
            Picasso.with(this)
                    //.setLoggingEnabled(true)
                    .load(results.getItemByIndex(resCounter).getVolumeInfo().getImageLinks().getThumbnail().replace("http:", "https:"))
                    .fit().centerInside()
                    .into(bookImage);
            registerButtons(results.getItemByIndex(resCounter));
        } else {
            handleFailure("Error - results size was 0!");
        }
    }

    @Override
    public void handleFailure(String message) {
        // Generic reuse of bookText variable; just print the error to the user :/
        if (message.contains("java.net.UnknownHostException")) {
            // bad practice here lol
            bookText.setText(R.string.barcode_failure);
        } else {
            bookText.setText(message);
        }
    }



    protected void initCamera() {
        /*
        BarcodeDetector barcodeDetector = new BarcodeDetector.Builder(this).setBarcodeFormats(Barcode.ALL_FORMATS).build();
        BarcodeTrackerFactory barcodeFactory = new BarcodeTrackerFactory(this); // pass in the context
        barcodeDetector.setProcessor(new MultiProcessor.Builder<>(barcodeFactory).build());
        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setAutoFocusEnabled(true)
                .build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(@NonNull SurfaceHolder holder) {
                try {
                    if(ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
                        cameraSource.start(surfaceView.getHolder());
                    } else {
                        ActivityCompat.requestPermissions(MainActivity.this, new
                                String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
                // TODO: Handle rotations
            }

            @Override
            public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
                cameraSource.stop();
            }

        });
         */
    }



}