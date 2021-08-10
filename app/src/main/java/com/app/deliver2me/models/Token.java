package com.app.deliver2me.models;

public class Token {
    private String email;
    private String token;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Token(String email, String token) {
        this.email = email;
        this.token = token;
    }
    public Token(){}
}
