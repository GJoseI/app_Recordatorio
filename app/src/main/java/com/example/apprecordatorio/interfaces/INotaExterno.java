package com.example.apprecordatorio.interfaces;

import com.example.apprecordatorio.entidades.Alarma;
import com.example.apprecordatorio.entidades.Recordatorio;

import java.util.ArrayList;

public interface INotaExterno {
    boolean add(Recordatorio r);
    ArrayList<Recordatorio> readAll();
    boolean delete (Recordatorio r);

    boolean update (Recordatorio r);

    Recordatorio readOne(int id);
}
