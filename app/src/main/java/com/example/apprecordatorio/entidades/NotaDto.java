package com.example.apprecordatorio.entidades;

public class NotaDto {
    private int id;
    private String titulo;
    private String descripcion;
    private String imagen;
    private int id_paciente;
    private boolean baja_logica;
    private String updated_at;

    public int getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getImagen() {
        return imagen;
    }

    public int getId_paciente() {
        return id_paciente;
    }

    public boolean isBaja_logica() {
        return baja_logica;
    }

    public String getUpdated_at() {
        return updated_at;
    }
}
