package com.example.apprecordatorio.Entidades;

public class Paciente {
    private int id;
    private String nombre;



    public Paciente() {
    }

    public Paciente(String nombre, int id, String apellido) {
        this.nombre = nombre;
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getId() {
        return id;
    }

    public void setId(int DNI) {
        this.id = DNI;
    }


    @Override
    public String toString() {
        return "paciente{" +"id ="+id+
                ", nombre='" + nombre + '\'' +
                '}';
    }
}
