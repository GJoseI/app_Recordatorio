package com.example.apprecordatorio.entidades;

public class Tutor {
    private int id;
    private String username;
    private String email;
    private String password;
    private Paciente p;

    public Tutor() {

    }

    public Tutor(int id, String username, String email, String password, Paciente p) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.p = p;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Paciente getP() {
        return p;
    }

    public void setP(Paciente p) {
        this.p = p;
    }

    @Override
    public String toString() {
        return "Tutor{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", p=" + p +
                '}';
    }
}
