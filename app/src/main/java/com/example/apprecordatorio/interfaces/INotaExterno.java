package com.example.apprecordatorio.interfaces;

import com.example.apprecordatorio.entidades.Alarma;
import com.example.apprecordatorio.entidades.Recordatorio;

import java.util.ArrayList;
import java.util.List;

public interface INotaExterno {
    int add(Recordatorio r);
    List<Recordatorio> readAllFrom(int id);
    boolean delete (Recordatorio r);

    boolean update (Recordatorio r);

    Recordatorio readOne(int id,int idPaciente);
}
