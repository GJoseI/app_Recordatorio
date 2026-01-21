package com.example.apprecordatorio.DAOInterfaces;

import com.example.apprecordatorio.Entidades.Alarma;

import java.util.List;

public interface IRecordatorio {
    long add(Alarma a);
    long addFromSync(Alarma a);
    int update(Alarma a);
    int updateIdRemoto(Alarma a);
    int updateFromSync(Alarma a);
    int setPendingChanges(Alarma r);
    int delete(Alarma a);
    List<Alarma> readAll();
    void cambiarEstado (Alarma a);
    Boolean desactivarAlarma(Alarma a);
    Boolean activarAlarma(Alarma a);
    int traerProximoId();
    int traerIdMaximo();
    List<Alarma> getAllPendingSync();
    Alarma readOneByIdRemoto(int id);
    Integer getIdRemoto(int id);
}
