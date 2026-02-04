package com.example.apprecordatorio.retrofit;

public class RecordatorioResponse {
    private boolean success;
    private int id;
    private long updated_at;
    private String error;

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
}
