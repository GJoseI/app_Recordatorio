package com.example.apprecordatorio.entidades;

public class SeguimientoDto {
    private int id;
    private boolean atendida;
    private int id_alarma;
    private int id_paciente;
    private String fecha_hora;

    private String titulo;

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public int getId() {
        return id;
    }

    public boolean isAtendida() {
        return atendida;
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
