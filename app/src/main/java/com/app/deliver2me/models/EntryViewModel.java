package com.app.deliver2me.models;

public class EntryViewModel {
    private String title;
    private String content;
    private String author;
    private String address;
    private String imageUri;
    private String phoneNo;
    private String priority;

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public EntryViewModel(String title, String content, String author, String address, String imageUri, String phoneNo, boolean isUrgent) {
        this.title = title;
        this.content = content;
        this.author = author;
        this.address = address;
        this.imageUri = imageUri;
        this.phoneNo = phoneNo;
        if(isUrgent)
        {
            this.priority = "urgent";
        }
        else
        {
            this.priority = "normal";
        }
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public EntryViewModel(String title, String content, String author, String address, String imageUri, String phoneNo) {
        this.title = title;
        this.content = content;
        this.author = author;
        this.address = address;
        this.imageUri = imageUri;
        this.phoneNo = phoneNo;
        this.priority = "normal";
    }

    public EntryViewModel(String title, String content, String author, String imageUri) {
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
