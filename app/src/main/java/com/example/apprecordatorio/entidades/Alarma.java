package com.example.apprecordatorio.entidades;

import java.time.LocalDate;

public class Alarma extends Recordatorio{
    private int ID;
    private int recordatiorioID;
    private boolean estado;
    private String tono;
    private String hora;

    private LocalDate fecha;


    @Override
    public LocalDate getFecha() {
        return fecha;
    }

    @Override
    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Alarma() {
        super();
    }

    public Alarma(boolean estado, String hora, int ID, int recordatiorioID, String tono) {
        super();
        this.estado = estado;
        this.hora = hora;
        this.ID = ID;
        this.recordatiorioID = recordatiorioID;
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

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getRecordatiorioID() {
        return recordatiorioID;
    }

    public void setRecordatiorioID(int recordatiorioID) {
        this.recordatiorioID = recordatiorioID;
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
                ", ID=" + ID +
                ", recordatiorioID=" + recordatiorioID +
                ", tono='" + tono + '\'' +
                ", hora='" + hora + '\'' +
                '}';
    }
}
