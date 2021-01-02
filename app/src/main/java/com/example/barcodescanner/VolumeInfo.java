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
    @SerializedName("imageLinks")
    private ImageLinks imageLinks;

    public String getTitle() {
        return title;
    }

    public List<String> getAuthors() {
        return authors;
    }
    public ImageLinks getImageLinks() {return imageLinks; }

    @Override
    public String toString(){
        StringBuilder builder = new StringBuilder();
        builder.append("Title: " + title + "\n");
        for (String author : authors) {
            builder.append("Author: " + author.toString() + "\n");
        }
        builder.append("This is a test sting I am adding!");
        return new String(builder);
    }
}
