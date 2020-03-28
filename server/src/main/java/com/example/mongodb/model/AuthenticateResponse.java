package com.example.mongodb.model;

import net.minidev.json.JSONObject;

public class AuthenticateResponse{

    private final String token;
    private final JSONObject errors;

    public AuthenticateResponse(String token) {
        this.token = token;
        this.errors = null;
    }

    public AuthenticateResponse(JSONObject errors,int a) {
        this.errors = errors;
        this.token="";
    }

    public JSONObject getErrors() {
        return this.errors;
    }

    public String getToken() {
        return this.token;
    }

}