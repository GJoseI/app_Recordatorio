package com.example.apprecordatorio.DAOInterfaces;

import com.example.apprecordatorio.Entidades.Recordatorio;

import java.util.List;

public interface IRecordatorioGral {
    long add(Recordatorio rec);
    long addFromSync(Recordatorio rec);
    int update(Recordatorio rec);
    int updateIdRemoto(Recordatorio r);
    long updateFromSync(Recordatorio r);
    int setPendingChanges(Recordatorio r);
    int delete(Recordatorio rec);
    List<Recordatorio> readAll();
    Recordatorio readOne(int id);
    int traerMaximoId();
    List<Recordatorio> getAllPendingSync();
    Recordatorio readOneByIdRemoto(int id);
}
