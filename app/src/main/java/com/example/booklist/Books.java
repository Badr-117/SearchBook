package com.example.booklist;

import android.widget.ImageView;

import java.net.URL;

public class Books {

    private String title, category, author, date, description, readLink;
    private String imageUrl;

    public Books(String title, String category, String author, String date, String imageUrl, String description, String readLink){
        this.title = title;
        this.category = category;
        this.author = author;
        this.date = date;
        this.imageUrl = imageUrl;
        this.description = description;
        this.readLink = readLink;
    }

    public String getTitle() {
        return title;
    }

    public String getCategory() {
        return "Category: " + category;
    }

    public String getAuthor() {
        return "Author: " + author;
    }

    public String getDate() {
        return "Published date: " + date;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public String getReadLink() {
        return readLink;
    }
}
