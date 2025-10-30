package com.example.apprecordatorio.entidades;

import com.example.apprecordatorio.enums.TipoRecordatorio;

import java.time.LocalDate;

public class Recordatorio {
    private int ID;
    private int tutorDNI;
    private int imgID;
    private int alarmaID;
    private int pacienteDNI;
    private String descripcion;
    private LocalDate fecha;
    private String hora;
    private boolean estado;

    private String titulo;

    public Recordatorio() {}

    

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Recordatorio(int alarmaID, boolean estado, String descripcion, LocalDate fecha, String hora,
                        int ID, int imgID, int pacienteDNI, int tutorDNI, String titulo ) {
        this.alarmaID = alarmaID;
        this.estado = estado;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.hora = hora;
        this.ID = ID;
        this.imgID = imgID;
        this.pacienteDNI = pacienteDNI;
        this.tutorDNI = tutorDNI;
    }

    public int getTutorDNI() {
        return tutorDNI;
    }

    public void setTutorDNI(int tutorDNI) {
        this.tutorDNI = tutorDNI;
    }

    public int getPacienteDNI() {
        return pacienteDNI;
    }

    public void setPacienteDNI(int pacienteDNI) {
        this.pacienteDNI = pacienteDNI;
    }

    public int getImgID() {
        return imgID;
    }

    public void setImgID(int imgID) {
        this.imgID = imgID;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getAlarmaID() {
        return alarmaID;
    }

    public void setAlarmaID(int alarmaID) {
        this.alarmaID = alarmaID;
    }
}
