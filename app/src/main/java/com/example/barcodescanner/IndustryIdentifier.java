package com.example.barcodescanner;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class IndustryIdentifier implements Serializable {
    @SerializedName("type")
    private String type;
    @SerializedName("identifier")
    private String identifier;

    public String getISBN() {
        return identifier;
    }

}
