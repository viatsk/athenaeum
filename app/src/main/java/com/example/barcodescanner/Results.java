package com.example.barcodescanner;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/*
 * Top-level response class, stores all the values
 * that match the requested query
 */
public class Results implements Serializable {
    @SerializedName("totalItems")
    private int totalItems;

    @SerializedName("items")
    private List<Book> items;

    public int getTotalItems() {
        return totalItems;
    }
    public List<Book> getItems(){
        return items;
    }
    public Book getFirstItem() {
        return items.get(0);
    }

}
