package com.example.apprecordatorio.entidades;

public class Emergencia {
    private int ID;
    private String descripcion;
    private String numero;
    private int recordatorioID;
    private boolean estado;

    public Emergencia() {
    }

    public Emergencia(String descripcion, boolean estado, int ID, String numero, int recordatorioID) {
        this.descripcion = descripcion;
        this.estado = estado;
        this.ID = ID;
        this.numero = numero;
        this.recordatorioID = recordatorioID;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public int getRecordatorioID() {
        return recordatorioID;
    }

    public void setRecordatorioID(int recordatorioID) {
        this.recordatorioID = recordatorioID;
    }

    @Override
    public String toString() {
        return "emergencia{" +
                "descripcion='" + descripcion + '\'' +
                ", ID=" + ID +
                ", numero='" + numero + '\'' +
                ", recordatorioID=" + recordatorioID +
                ", estado=" + estado +
                '}';
    }
}
