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
    @SerializedName("publisher")
    private String publisher;
    @SerializedName("publishedDate")
    private String publishedDate;
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
        builder.append("Title: " + title + " - " + subtitle + "\n");
        for (String author : authors) {
            builder.append("Author: " + author.toString() + "\n");
        }
        builder.append("Published By " + publisher + " on " + publishedDate + "\n");
        return new String(builder);
    }
}
