package com.example.barcodescanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.loader.content.AsyncTaskLoader;


import android.Manifest;
import android.content.AsyncQueryHandler;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private BarcodeDetector barcodeDetector;

    private CameraSource cameraSource;
    private static final int REQUEST_CAMERA_PERMISSION = 201;

    private TextView barcodeText;
    private TextView bookText;
    private SurfaceView surfaceView;


    class fetchGoogleBooks extends AsyncTask<String, Void, SBook> {
        @Override
        protected SBook doInBackground(String... params) {
            final String ISBN = params[0];
            try {
                QueryGoogleBooks q = new QueryGoogleBooks();
                q.getBookByISBN(ISBN);
                return q.BOOK;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(SBook book) {
            if (book == null) {
                bookText.setText("Query Failed");
            } else {
                bookText.setText(book.toString());
            }
        }
    }

    protected void initCamera() {
        BarcodeDetector barcodeDetector = new BarcodeDetector.Builder(this).setBarcodeFormats(Barcode.ALL_FORMATS).build();
        cameraSource = new CameraSource.Builder(this, barcodeDetector).setAutoFocusEnabled(true).build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(@NonNull SurfaceHolder holder) {
                try {
                    if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
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
            }

            @Override
            public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
                cameraSource.stop();
            }
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
                cameraSource.stop();
            }

            @Override
            public void receiveDetections(@NonNull Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> bcs = detections.getDetectedItems();
                final fetchGoogleBooks fetcher = new fetchGoogleBooks();
                if (bcs.size() != 0) {
                    Barcode bc = bcs.valueAt(0);
                    String bcVal = bc.rawValue;
                    barcodeText.post(new Runnable() {
                        @Override
                        public void run() {
                            barcodeText.setText(bcVal);
                        }
                    });
                    bookText.post(new Runnable() {
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
                        public void run() {
                            if (!validateISBN(bcVal)) {
                                bookText.setText("Not a book!");
                                //bookText.setText(queryGoogleAPI(bcVal));
                            }
                            else {
                                fetcher.execute(bcVal);
                            }
                        }
                    });
                };
            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        surfaceView = findViewById(R.id.surface_view); // Bind surface view
        barcodeText = findViewById(R.id.barcode_text); // Bind barcode text
        bookText = findViewById(R.id.bookinfo_text); // Bind book text
        initCamera();
    }


}