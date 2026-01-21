package com.example.apprecordatorio.Retrofit;

import com.example.apprecordatorio.Entidades.Alarma;

import java.util.List;

public class AlarmasResponse {

    private boolean success;
    private List<Alarma> alarmas;
    private String error;

    public boolean isSuccess() {
        return success;
    }

    public List<Alarma> getAlarmas() {
        return alarmas;
    }

    public String getError() {
        return error;
    }
}