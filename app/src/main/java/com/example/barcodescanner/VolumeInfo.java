package com.example.barcodescanner;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class VolumeInfo implements Serializable{
    @SerializedName("title")
    private String title;
    @SerializedName("subtitle")
    private String subtitle;
    @SerializedName("authors")
    private List<String> authors;

    public String getTitle() {
        return title;
    }

    public List<String> getAuthors() {
        return authors;
    }

    @Override
    public String toString(){
        StringBuilder builder = new StringBuilder();
        builder.append("Title: " + title + "\n");
        for (String author : authors) {
            builder.append("Author: " + author.toString() + "\n");
        }
        return new String(builder);
    }
}
