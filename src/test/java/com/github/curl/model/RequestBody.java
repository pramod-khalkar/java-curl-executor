package com.github.curl.model;

import com.google.gson.Gson;

/**
 * Date: 16/02/22
 * Time: 6:17 PM
 * This file is project specific to java-curl-executor
 * Author: Pramod Khalkar
 */
public class RequestBody {
    private String username;
    private String password;

    public RequestBody(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
