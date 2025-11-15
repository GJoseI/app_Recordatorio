package com.example.apprecordatorio.interfaces;

import com.example.apprecordatorio.entidades.Alarma;

import java.util.ArrayList;

public interface IRecordatorioExterno {
    int add(Alarma a);
    ArrayList<Alarma>readAllFromPaciente(int id);
    boolean delete (Alarma a);

    boolean update (Alarma a);

    Alarma readOneFrom(int id, int idPaciente);
}
