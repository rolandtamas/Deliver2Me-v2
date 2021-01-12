package com.app.deliver2me.models;

public class EntryViewModel {
    private String title;
    private String content;
    private String author;
    private String imageUri;

    public EntryViewModel(String title, String content, String author) {
        this.title = title;
        this.content = content;
        this.author = author;
    }

    public EntryViewModel(String title, String content, String author,String imageUri) {
        this.title = title;
        this.content = content;
        this.author = author;
        this.imageUri = imageUri;
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
}
