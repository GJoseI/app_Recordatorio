package com.example.apprecordatorio.Negocio;

import com.example.apprecordatorio.DAO.SeguimientoExternoDao;
import com.example.apprecordatorio.Entidades.Seguimiento;

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
