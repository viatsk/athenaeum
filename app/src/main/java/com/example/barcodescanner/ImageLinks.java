package com.example.barcodescanner;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ImageLinks implements Serializable {
    @SerializedName("smallThumbnail")
    private String smallThumbnail;
    @SerializedName("thumbnail")
    private String thumbnail;

    public String getThumbnail() {
        return thumbnail;
    }

}
