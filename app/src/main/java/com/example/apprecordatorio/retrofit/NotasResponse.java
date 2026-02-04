package com.example.apprecordatorio.retrofit;

import com.example.apprecordatorio.entidades.Alarma;
import com.example.apprecordatorio.entidades.NotaDto;
import com.example.apprecordatorio.entidades.Recordatorio;

import java.util.List;

public class NotasResponse {

    private boolean success;
    private List<NotaDto> notas;
    private String error;

    public boolean isSuccess() {
        return success;
    }

    public List<NotaDto> getNotas() {
        return notas;
    }

    public String getError() {
        return error;
    }
}