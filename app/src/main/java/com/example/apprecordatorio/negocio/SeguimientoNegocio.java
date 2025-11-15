package com.example.apprecordatorio.negocio;

import com.example.apprecordatorio.dao.SeguimientoExternoDao;
import com.example.apprecordatorio.entidades.Seguimiento;

import java.util.ArrayList;

public class SeguimientoNegocio {

    private SeguimientoExternoDao dao;


    public SeguimientoNegocio ()
    {
        dao = new SeguimientoExternoDao();
    }

    public ArrayList<Seguimiento> readAllFromPaciente(int id)
    {
       return dao.readAllFromPaciente(id);
    }
}
