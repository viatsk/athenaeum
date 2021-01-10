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
    @SerializedName("description")
    private String description;
    @SerializedName("industryIdentifiers")
    private List<IndustryIdentifier> industryIdentifiers;
    @SerializedName("printType")
    private String printType;

    public String getTitle() {
        return title;
    }

    public List<String> getAuthors() {
        return authors;
    }
    public ImageLinks getImageLinks() {return imageLinks; }
    public String getPrintType() { return printType; }
    public String getPublisher() {return publisher; }
    public String getPublishedDate() { return publishedDate; }

    @Override
    public String toString(){
        StringBuilder builder = new StringBuilder();
        builder.append("Title: " + title);
        if (subtitle != null) {
            builder.append(" - " + subtitle);
        }
        builder.append("\n");
        for (String author : getAuthors()) {
            builder.append("Author: " + author.toString() + "\n");
        }
        if (publisher != null) {
            builder.append("Published By " + getPublisher() + " on " + publishedDate + "\n");
        }
        return new String(builder);
    }
}
