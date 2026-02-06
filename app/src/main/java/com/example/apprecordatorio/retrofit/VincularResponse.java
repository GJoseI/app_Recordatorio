package com.example.apprecordatorio.retrofit;

public class VincularResponse {
    private boolean success;
    private String message;
    private String error;

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public String getError() {
        return error;
    }
}