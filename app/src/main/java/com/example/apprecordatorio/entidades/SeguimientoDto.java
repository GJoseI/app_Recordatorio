package com.example.apprecordatorio.entidades;

public class SeguimientoDto {
    private int id;
    private int atendida;
    private int id_alarma;
    private int id_paciente;
    private String fecha_hora;

    public int getId() {
        return id;
    }

    public boolean isAtendida() {
        return atendida == 1;
    }

    public int getIdAlarma() {
        return id_alarma;
    }

    public int getIdPaciente() {
        return id_paciente;
    }

    public String getFechaHora() {
        return fecha_hora;
    }
}
