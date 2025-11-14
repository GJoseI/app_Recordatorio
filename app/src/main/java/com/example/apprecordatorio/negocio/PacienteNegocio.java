package com.example.apprecordatorio.negocio;

import android.content.Context;

import com.example.apprecordatorio.dao.NotasExternoDao;
import com.example.apprecordatorio.dao.PacienteDao;
import com.example.apprecordatorio.dao.PacienteExternoDao;
import com.example.apprecordatorio.dao.RecordatorioExternoDao;
import com.example.apprecordatorio.entidades.Alarma;
import com.example.apprecordatorio.entidades.Paciente;
import com.example.apprecordatorio.entidades.Recordatorio;

import java.util.List;

public class PacienteNegocio {

    private PacienteDao dao;
    private PacienteExternoDao daoEx;
    private  RecordatorioExternoDao daoExAlarma;
    private NotasExternoDao notasExDao;

    public  PacienteNegocio(Context context)
    {
        dao = new PacienteDao(context);
        daoEx = new PacienteExternoDao();
        daoExAlarma = new RecordatorioExternoDao();
        notasExDao = new NotasExternoDao();
    }

    public long add (Paciente p)
    {
        long resultado = 0;
        int nuevoId = 0;
        nuevoId = daoEx.add(p);
        if(nuevoId>0){
            p.setId(nuevoId);
           resultado = dao.add(p);
        }
        return resultado;
    }

    public Paciente read()
    {
        return dao.read();
    }
    public void ponerIdPacienteEnAlarmas(Context context)
    {
        Paciente p = this.read();
        if(p!=null)
        {
            RecordatorioNegocio rneg = new RecordatorioNegocio(context);
            List<Alarma> lista = rneg.readAll();

            RecordatorioGralNegocio gneg = new RecordatorioGralNegocio(context);
            List<Recordatorio> notas = gneg.readAll();

            for(Alarma a : lista)
            {
                a.setPacienteId(p.getId());
                rneg.update(a,context);
                daoExAlarma.add(a);
            }
            for (Recordatorio r : notas)
            {
                r.setPacienteId(p.getId());
                gneg.update(r);
                notasExDao.add(r);
            }
        }
    }
}
