package com.example.apprecordatorio.interfaces;

import com.example.apprecordatorio.entidades.Alarma;
import com.example.apprecordatorio.entidades.Recordatorio;

import java.util.ArrayList;

public interface INotaExterno {
    int add(Recordatorio r);
    ArrayList<Recordatorio> readAllFrom(int id);
    boolean delete (Recordatorio r);

    boolean update (Recordatorio r);

    Recordatorio readOne(int id,int idPaciente);
}
