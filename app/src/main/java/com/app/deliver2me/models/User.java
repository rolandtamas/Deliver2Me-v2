package com.app.deliver2me.models;

public class User {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String imageUri;
    private Boolean isCourier;

    public User(String firstName, String lastName, String email, String password, Boolean isCourier) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.isCourier = isCourier;
    }

    public User(String firstName, String lastName, String email, String password, String imageUri, Boolean isCourier) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.imageUri = imageUri;
        this.isCourier = isCourier;
    }

    public User(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    public User(String firstName, String lastName, String email,String password, String imageUri) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.imageUri = imageUri;
    }

    public User(){}

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public Boolean getCourier() {
        return isCourier;
    }

    public void setCourier(Boolean courier) {
        isCourier = courier;
    }
}

