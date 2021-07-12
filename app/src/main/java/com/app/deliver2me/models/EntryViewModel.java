package com.app.deliver2me.models;

public class EntryViewModel {
    private String title;
    private String content;
    private String author;
    private String address;
    private String imageUri;

    public EntryViewModel(String title, String content, String author,String imageUri) {
        this.title = title;
        this.content = content;
        this.author = author;
        this.imageUri = imageUri;
    }

    public EntryViewModel(String title, String content, String author,String imageUri, String address) {
        this.title = title;
        this.content = content;
        this.author = author;
        this.imageUri = imageUri;
        this.address = address;
    }

    public EntryViewModel(){

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
