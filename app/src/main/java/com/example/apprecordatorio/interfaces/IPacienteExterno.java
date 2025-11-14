package com.example.apprecordatorio.interfaces;

import com.example.apprecordatorio.entidades.Paciente;

import java.util.ArrayList;

public interface IPacienteExterno {

    ArrayList<Paciente>readAll();
    boolean add(Paciente p);
    boolean update(Paciente p);
    boolean delete(Paciente p);
    Paciente readOne(int id);
}
