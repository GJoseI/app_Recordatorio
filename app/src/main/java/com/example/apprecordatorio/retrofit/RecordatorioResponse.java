package com.example.apprecordatorio.retrofit;

public class RecordatorioResponse {
    private boolean success;
    private int id;
    private long updated_at;
    private String error;
    private String message;
    private String params;

    public boolean isSuccess() {
        return success;
    }

    public int getId() {
        return id;
    }

    public long getUpdated_at() {
        return updated_at;
    }

    public String getError() {
        return error;
    }
    public String getParams() {
        return params;
    }
    public String getMessage() {
        return message;
    }
}
