package com.github.curl.model;

import com.google.gson.Gson;

/**
 * @author : Pramod Khalkar
 * @since : 22/02/22, Tue
 * description: This file belongs to java-curl-executor
 **/
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
