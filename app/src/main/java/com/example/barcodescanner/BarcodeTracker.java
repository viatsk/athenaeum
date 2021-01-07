package com.example.barcodescanner;

import android.content.Context;

import androidx.annotation.UiThread;

import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.Tracker;
import com.google.android.gms.vision.barcode.Barcode;

public class BarcodeTracker extends Tracker<Barcode> {
    private NewBarcodeInterface newBarcodeInterface;

    public interface NewBarcodeInterface {
        @UiThread
        void onBarcodeDetected(Barcode barcode);
    }

    // Constructor takes in the context, assigns one half to this interface
    BarcodeTracker(Context context) {
        this.newBarcodeInterface = (NewBarcodeInterface) context;
    }

    //TODO: Which function should update the interface?
    @Override
    public void onNewItem(int id, Barcode barcode) {
        newBarcodeInterface.onBarcodeDetected(barcode);
    }

    @Override
    public void onUpdate(Detector.Detections<Barcode> detections, Barcode barcode) {
        // Access detected barcodes :)

    }
}
