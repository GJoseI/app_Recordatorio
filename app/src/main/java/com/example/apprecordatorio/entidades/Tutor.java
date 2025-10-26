package com.example.apprecordatorio.entidades;

public class Tutor {
    private int DNI;
    private String nombre;
    private String apellido;
    private String email;
    private String telefono;
    private int dniPaciente;
    private String password;
    private boolean estado;

    public Tutor() {}

    public Tutor(String password, String nombre, String telefono, boolean estado,
                 String email, int dniPaciente, int DNI, String apellido) {
        this.password = password;
        this.nombre = nombre;
        this.telefono = telefono;
        this.estado = estado;
        this.email = email;
        this.dniPaciente = dniPaciente;
        this.DNI = DNI;
        this.apellido = apellido;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getDniPaciente() {
        return dniPaciente;
    }

    public void setDniPaciente(int dniPaciente) {
        this.dniPaciente = dniPaciente;
    }

    public int getDNI() {
        return DNI;
    }

    public void setDNI(int DNI) {
        this.DNI = DNI;
    }

    @Override
    public String toString() {
        return "tutor{" +
                "apellido='" + apellido + '\'' +
                ", DNI=" + DNI +
                ", nombre='" + nombre + '\'' +
                ", email='" + email + '\'' +
                ", telefono='" + telefono + '\'' +
                ", dniPaciente=" + dniPaciente +
                ", password='" + password + '\'' +
                ", estado=" + estado +
                '}';
    }
}
