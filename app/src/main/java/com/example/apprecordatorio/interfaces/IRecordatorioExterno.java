package com.example.apprecordatorio.interfaces;

import com.example.apprecordatorio.entidades.Alarma;

import java.util.ArrayList;

public interface IRecordatorioExterno {
    boolean add(Alarma a);
    ArrayList<Alarma>readAll();
    boolean delete (Alarma a);

    boolean update (Alarma a);

    Alarma readOne(int id);
}
