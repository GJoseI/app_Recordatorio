package com.example.apprecordatorio.entidades;

import java.time.LocalDate;

public class Alarma extends Recordatorio{
    private boolean estado;
    private String tono;
    private String hora;

    private LocalDate fecha;



    public LocalDate getFecha() {
        return fecha;
    }


    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Alarma() {
        super();
    }

    public Alarma(boolean estado, String hora, String tono) {
        super();
        this.estado = estado;
        this.hora = hora;
        this.tono = tono;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getTono() {
        return tono;
    }

    public void setTono(String tono) {
        this.tono = tono;
    }

    @Override
    public String toString() {
        return "alarma{" +
                "estado=" + estado +
                ", tono='" + tono + '\'' +
                ", hora='" + hora + '\'' +
                '}';
    }
}
