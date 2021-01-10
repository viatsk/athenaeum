package com.example.barcodescanner;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.common.images.Size;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.MultiProcessor;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;
import java.util.List;

public class CameraResizing extends ViewGroup {
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    private Context context;
    private CameraSource cameraSource;
    private SurfaceView surfaceView;

    public CameraResizing(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        this.surfaceView = new SurfaceView(context);
        BarcodeDetector barcodeDetector = new BarcodeDetector.Builder(context).setBarcodeFormats(Barcode.ALL_FORMATS).build();
        BarcodeTrackerFactory barcodeFactory = new BarcodeTrackerFactory(context); // pass in the context
        barcodeDetector.setProcessor(new MultiProcessor.Builder<>(barcodeFactory).build());
        cameraSource = new CameraSource.Builder(context, barcodeDetector)
                .setAutoFocusEnabled(true)
                .build();
        surfaceView.getHolder().addCallback(new resizingCallback());
        addView(surfaceView); // why
    }

    @RequiresPermission(Manifest.permission.CAMERA)
    public void start(CameraSource cameraSource) throws IOException, SecurityException {
        this.cameraSource = cameraSource;
    }

    private class resizingCallback implements SurfaceHolder.Callback {
        @Override
        public void surfaceCreated(@NonNull SurfaceHolder holder) {
            try {
                if(ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
                    cameraSource.start(surfaceView.getHolder());
               // } else {
               //     ActivityCompat.requestPermissions(MainActivity, new
               //             String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
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

    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int width = 400;
        int height = 750;
        if (cameraSource != null) {
            Size size = cameraSource.getPreviewSize();
            if (size != null) {
                width = size.getWidth();
                height = size.getHeight();
            }
        }


        final int layoutWidth = right - left;
        final int layoutHeight = bottom - top;

        // Computes height and width for potentially doing fit width.
        int childWidth = layoutWidth;
        int childHeight = (int) (((float) layoutWidth / (float) width) * height);

        // If height is too tall using fit width, does fit height instead.
        if (childHeight < layoutHeight) {
            childHeight = layoutHeight;
            childWidth = (int) (((float) layoutHeight / (float) height) * width);
        }

        for (int i = 0; i < getChildCount(); ++i) {
            getChildAt(i).layout(0, 0, childWidth, childHeight);
        }
    }
}
