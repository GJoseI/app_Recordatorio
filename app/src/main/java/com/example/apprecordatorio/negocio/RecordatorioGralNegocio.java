package com.example.apprecordatorio.negocio;

import android.content.Context;
import android.util.Log;

import com.example.apprecordatorio.dao.NotasExternoDao;
import com.example.apprecordatorio.dao.RecordatorioGralDao;
import com.example.apprecordatorio.entidades.Alarma;
import com.example.apprecordatorio.entidades.Paciente;
import com.example.apprecordatorio.entidades.Recordatorio;
import com.example.apprecordatorio.util.FileUtil;

import java.util.List;

public class RecordatorioGralNegocio {

    private RecordatorioGralDao dao;
    private NotasExternoDao daoEx;

    private PacienteNegocio pneg;

    private FileUtil fu;

    public RecordatorioGralNegocio(Context context)
    {

        dao = new RecordatorioGralDao(context);
        daoEx = new NotasExternoDao();
        pneg = new PacienteNegocio(context);
        fu = new FileUtil();
    }


    public List<Recordatorio> readAll()
    {
        return dao.readAll();
    }
    public long add(Recordatorio r)
    {
        Paciente p =  pneg.read();
        if(p!=null)
        {
            r.setPacienteId(p.getId());
        }

        return dao.add(r);
    }
    public int update(Recordatorio r)
    {
       Paciente p =  pneg.read();
        if(p!=null)
        {
            r.setPacienteId(p.getId());
        }

        return dao.update(r);
    }
    public int delete(Recordatorio r)
    {
        Paciente p =  pneg.read();
        int resultado = 0;
        if(p!=null)
        {
            r.setPacienteId(p.getId());
        }
        String imagen;
        imagen= r.getImagenUrl();
        resultado = dao.delete(r);
        if(resultado>0)
        {
            if(imagen!= null)
            {
                fu.borrarImagenAnterior(r.getImagenUrl());
            }
        }
        return resultado;
    }

    public boolean addEx(Recordatorio r)
    {
        r.setUpdatedAt(System.currentTimeMillis());
        return (daoEx.add(r)>0) ;
    }
    public boolean updateEx(Recordatorio r)
    {
        r.setUpdatedAt(System.currentTimeMillis());
        return daoEx.update(r);
    }
    public boolean deleteEx(Recordatorio r)
    {
        r.setUpdatedAt(System.currentTimeMillis());
        return daoEx.delete(r);
    }
    public List<Recordatorio> readAllEx(int pacienteId)
    {
        Log.d("NOTAS EXTERNO","EN NEGOCIO");
        return daoEx.readAllFrom(pacienteId);
    }
    public Recordatorio readOne(int id){return dao.readOne(id);}

}

