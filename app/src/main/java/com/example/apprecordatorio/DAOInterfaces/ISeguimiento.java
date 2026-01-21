package com.example.apprecordatorio.DAOInterfaces;

import com.example.apprecordatorio.Entidades.Seguimiento;

import java.util.List;

public interface ISeguimiento {
    List<Seguimiento> readAllPendingUpload();
    int setPendingUpload(Seguimiento s);
    long add(Seguimiento s);
}
