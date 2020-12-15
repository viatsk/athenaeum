package com.example.barcodescanner;

import java.util.List;

public class SBook {
    private String title;
    private List<String> authors;

    public String getTitle() {
        return title;
    }

    public List<String> getAuthors() {
        return authors;
    }

    //public String toString(){
    //    StringBuilder builder = new StringBuilder();
    //    builder.append("Title: " + title + "\n");
    //    builder.append("Author: " + authors.toString() + "\n");
    //    return new String(builder);
    //}
}
