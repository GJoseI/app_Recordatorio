package com.example.apprecordatorio.entidades;

public class Seguimiento {
    int id;

    Alarma alarma;

    String timestamp;

    boolean atendida;

    boolean pending_upload;




    public Seguimiento()
    {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Alarma getAlarma() {
        return alarma;
    }

    public void setAlarma(Alarma alarma) {
        this.alarma = alarma;
    }

    public boolean isAtendida() {
        return atendida;
    }

    public void setAtendida(boolean atendida) {
        this.atendida = atendida;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isPending_upload() {
        return pending_upload;
    }

    public void setPending_upload(boolean pending_upload) {
        this.pending_upload = pending_upload;
    }


}
