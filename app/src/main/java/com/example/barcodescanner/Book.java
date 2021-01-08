package com.example.barcodescanner;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/*
 * Represents a single result from the google books API
 */

public class Book implements Serializable {
    @SerializedName("id")
    private String id;
    @SerializedName("volumeInfo")
    private VolumeInfo volumeInfo;
    public VolumeInfo getVolumeInfo(){
        return volumeInfo;
    }

}