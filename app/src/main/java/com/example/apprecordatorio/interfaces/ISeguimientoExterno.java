package com.example.apprecordatorio.interfaces;

import com.example.apprecordatorio.entidades.Seguimiento;

import java.util.ArrayList;

public interface ISeguimientoExterno {

    boolean add(Seguimiento s);
    ArrayList<Seguimiento>readAll();

}
