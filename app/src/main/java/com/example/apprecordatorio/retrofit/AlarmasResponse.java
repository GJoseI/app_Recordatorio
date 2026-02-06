package com.example.apprecordatorio.retrofit;

import com.example.apprecordatorio.entidades.Alarma;
import com.example.apprecordatorio.entidades.AlarmaDto;

import java.util.List;

public class AlarmasResponse {

    private boolean success;
    private List<AlarmaDto> alarmas;
    private String error;

    public boolean isSuccess() {
        return success;
    }

    public List<AlarmaDto> getAlarmas() {
        return alarmas;
    }

    public String getError() {
        return error;
    }
}