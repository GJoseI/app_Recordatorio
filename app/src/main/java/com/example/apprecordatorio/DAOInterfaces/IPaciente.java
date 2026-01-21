package com.example.apprecordatorio.DAOInterfaces;

import com.example.apprecordatorio.Entidades.Paciente;

public interface IPaciente {
    long add(Paciente p);
    Paciente read();
}
