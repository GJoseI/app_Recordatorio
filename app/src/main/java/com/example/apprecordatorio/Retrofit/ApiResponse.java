package com.example.apprecordatorio.Retrofit;

public class ApiResponse {

    private boolean success;
    private int id;
    private String error;

    public boolean isSuccess() {
        return success;
    }

    public int getId() {
        return id;
    }

    public String getError() {
        return error;
    }
}