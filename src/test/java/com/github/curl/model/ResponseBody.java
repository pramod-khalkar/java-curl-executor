package com.github.curl.model;

import com.google.gson.Gson;

/**
 * Date: 16/02/22
 * Time: 6:17 PM
 * This file is project specific to java-curl-executor
 * Author: Pramod Khalkar
 */
public class ResponseBody {
    private String message;

    public ResponseBody(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ResponseBody editMessage(String newMessage) {
        this.message = newMessage;
        return this;
    }

    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
