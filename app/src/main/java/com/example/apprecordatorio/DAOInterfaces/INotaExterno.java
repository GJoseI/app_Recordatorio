package com.example.apprecordatorio.DAOInterfaces;

import com.example.apprecordatorio.Entidades.Recordatorio;

import java.util.List;

public interface INotaExterno {
    int add(Recordatorio r);
    List<Recordatorio> readAllFrom(int id);
    boolean delete (Recordatorio r);

    boolean update (Recordatorio r);

    Recordatorio readOne(int id,int idPaciente);
}
