package com.example.apprecordatorio.Negocio;

import android.content.Context;

import com.example.apprecordatorio.DAO.NotasExternoDao;
import com.example.apprecordatorio.DAO.PacienteDao;
import com.example.apprecordatorio.DAO.PacienteExternoDao;
import com.example.apprecordatorio.DAO.RecordatorioDao;
import com.example.apprecordatorio.DAO.RecordatorioExternoDao;
import com.example.apprecordatorio.DAO.RecordatorioGralDao;
import com.example.apprecordatorio.Entidades.Alarma;
import com.example.apprecordatorio.Entidades.Paciente;
import com.example.apprecordatorio.Entidades.Recordatorio;

import java.util.List;

public class PacienteNegocio {

    private RecordatorioDao rd;
    private RecordatorioGralDao nd;
    private PacienteDao dao;
    private PacienteExternoDao daoEx;
    private  RecordatorioExternoDao daoExAlarma;
    private NotasExternoDao notasExDao;

    public  PacienteNegocio(Context context)
    {
        rd = new RecordatorioDao(context);
        nd = new RecordatorioGralDao(context);
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
                int id = daoExAlarma.add(a);
                a.setIdRemoto(id);
                rd.updateIdRemoto(a);
            }
            for (Recordatorio r : notas)
            {
                r.setPacienteId(p.getId());
                gneg.update(r);
                int id = notasExDao.add(r);
                r.setIdRemoto(id);
                nd.updateIdRemoto(r);
            }
        }
    }
}
