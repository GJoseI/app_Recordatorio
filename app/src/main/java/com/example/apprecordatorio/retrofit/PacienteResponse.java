package com.example.apprecordatorio.retrofit;

import com.example.apprecordatorio.entidades.Paciente;

public class PacienteResponse {

    private boolean success;
    private Paciente paciente;
    private String error;

    public boolean isSuccess() {
        return success;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public String getError() {
        return error;
    }
}
