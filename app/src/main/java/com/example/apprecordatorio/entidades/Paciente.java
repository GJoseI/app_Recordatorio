package com.example.apprecordatorio.entidades;

public class Paciente {
    private int DNI;
    private String nombre;
    private String apellido;

    public Paciente() {
    }

    public Paciente(String nombre, int DNI, String apellido) {
        this.nombre = nombre;
        this.DNI = DNI;
        this.apellido = apellido;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getDNI() {
        return DNI;
    }

    public void setDNI(int DNI) {
        this.DNI = DNI;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    @Override
    public String toString() {
        return "paciente{" +
                "apellido='" + apellido + '\'' +
                ", DNI=" + DNI +
                ", nombre='" + nombre + '\'' +
                '}';
    }
}
