package com.example.apprecordatorio.entidades;



import java.time.LocalDate;

public class Recordatorio {
    private int id;
    private int tutorDNI;
    private String imagenUrl;
    private int pacienteDNI;
    private String descripcion;
    private boolean estado;

    private String titulo;

    public Recordatorio() {}



    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Recordatorio(boolean estado, String descripcion,
                        int id, String imagenUrl, int pacienteDNI, int tutorDNI, String titulo ) {
        this.estado = estado;
        this.descripcion = descripcion;
        this.id = id;
        this.imagenUrl = imagenUrl;
        this.pacienteDNI = pacienteDNI;
        this.tutorDNI = tutorDNI;
        this.titulo = titulo;
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

    public String getimagenUrl() {
        return imagenUrl;
    }

    public void setimagenUrl(String imgID) {
        this.imagenUrl = imagenUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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


}
