package com.example.apprecordatorio.DAOInterfaces;

import com.example.apprecordatorio.Entidades.Seguimiento;

import java.util.ArrayList;

public interface ISeguimientoExterno {

    boolean add(Seguimiento s);
    ArrayList<Seguimiento>readAllFromPaciente(int id);

}
