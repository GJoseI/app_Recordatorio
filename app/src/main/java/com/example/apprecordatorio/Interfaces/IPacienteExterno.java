package com.example.apprecordatorio.Interfaces;

import com.example.apprecordatorio.Entidades.Paciente;

import java.util.ArrayList;

public interface IPacienteExterno {

    ArrayList<Paciente>readAll();
    int add(Paciente p);
    boolean update(Paciente p);
    boolean delete(Paciente p);
    Paciente readOne(int id);
}
