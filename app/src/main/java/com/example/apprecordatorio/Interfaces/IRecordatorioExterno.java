package com.example.apprecordatorio.Interfaces;

import com.example.apprecordatorio.Entidades.Alarma;

import java.util.List;

public interface IRecordatorioExterno {
    int add(Alarma a);
    List<Alarma> readAllFromPaciente(int id);
    boolean delete (Alarma a);

    boolean update (Alarma a);

    Alarma readOneFrom(int id, int idPaciente);
}
