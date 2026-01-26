package com.example.apprecordatorio.retrofit;

import com.example.apprecordatorio.entidades.SeguimientoDto;

import java.util.ArrayList;

public class SeguimientosResponse {
    private boolean success;
    private ArrayList<SeguimientoDto> seguimientos;

    public boolean isSuccess() {
        return success;
    }

    public ArrayList<SeguimientoDto> getSeguimientos() {
        return seguimientos;
    }
}
