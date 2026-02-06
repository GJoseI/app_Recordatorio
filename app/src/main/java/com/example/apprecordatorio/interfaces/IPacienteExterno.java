package com.example.apprecordatorio.interfaces;

import com.example.apprecordatorio.entidades.Paciente;

import java.util.ArrayList;

public interface IPacienteExterno {

    int add(Paciente p);
    Paciente readOne(int id);
}
