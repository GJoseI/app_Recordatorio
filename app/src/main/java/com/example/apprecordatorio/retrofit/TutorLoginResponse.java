package com.example.apprecordatorio.retrofit;

public class TutorLoginResponse {
    private boolean success;
    private int id;
    private String nombre_usuario;
    private String email;
    private int id_paciente;
    private String error;

    public boolean isSuccess() {
        return success;
    }

    public int getId() {
        return id;
    }

    public String getNombre_usuario() {
        return nombre_usuario;
    }

    public String getEmail() {
        return email;
    }

    public int getId_paciente() {
        return id_paciente;
    }

    public String getError() {
        return error;
    }
}
