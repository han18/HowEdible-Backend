package com.backend.HowEdible.dto;


public class UserLoginRequest {
    private String username;
    private String password;

    // this is the main constructor
    public UserLoginRequest() {
    }

    // these are the getters & setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
