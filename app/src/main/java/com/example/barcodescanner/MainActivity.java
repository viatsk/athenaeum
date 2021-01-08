package com.example.barcodescanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;


import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.MultiProcessor;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity implements ViewContract.View, BarcodeTracker.NewBarcodeInterface {
    private BarcodeDetector barcodeDetector;

    private CameraSource cameraSource;
    private static final int REQUEST_CAMERA_PERMISSION = 201;

    private TextView barcodeText;
    private TextView bookText;
    private SurfaceView surfaceView;
    private ImageView bookImage;

    private ViewContract.Presenter presenter;
    private Button haveButton;
    private Button wantButton;

    public int resCounter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        presenter = new QueryGoogleBooks(this);
        surfaceView = findViewById(R.id.surface_view); // Bind surface view
        barcodeText = findViewById(R.id.barcode_text); // Bind barcode text
        bookText = findViewById(R.id.bookinfo_text); // Bind book text
        bookImage = findViewById(R.id.book_image); // Bind image view
        haveButton = findViewById(R.id.have_button); // Bind have button
        wantButton = findViewById(R.id.want_button); // Bind want button
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
            bookText.setText("This is not a valid ISBN");
        }
        else {
            presenter.search(bcVal);
        }
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
        } else {
            handleFailure("Error - results size was 0!");
        }
    }

    @Override
    public void handleFailure(String message) {
        // Generic reuse of bookText variable; just print the error to the user :/
        bookText.setText(message);
    }



    protected void initCamera() {
        barcodeDetector = new BarcodeDetector.Builder(this).setBarcodeFormats(Barcode.ALL_FORMATS).build();
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
                // Surface won't change; won't do this
            }

            @Override
            public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
                cameraSource.stop();
            }
        });

    }



}