package com.example.apprecordatorio.negocio;

import android.content.Context;

import com.example.apprecordatorio.dao.RecordatorioDao;
import com.example.apprecordatorio.entidades.Alarma;
import com.example.apprecordatorio.util.AlarmaUtil;


import java.util.List;

public class RecordatorioNegocio {

    private RecordatorioDao dao;
    private AlarmaUtil au;

    public RecordatorioNegocio(Context context)
    {
        dao = new RecordatorioDao(context);
        au = new AlarmaUtil();
    }

    public List<Alarma> readAll()
    {
        return dao.readAll();
    }
    public long add(Alarma r, Context context)
    {
        long resultado = dao.add(r);
        if(resultado>0) au.programarAlarmas(context, r);
        return resultado;
    }
    public int update(Alarma r)
    {
        return dao.update(r);
    }
    public int delete(Alarma r)
    {
        return dao.delete(r);
    }

    public void desactivarAlarma(Alarma a,Context context)
    {
        if(dao.desactivarAlarma(a))au.cancelarAlarmas(context,a);

    }
    public void activarAlarma(Alarma a,Context context)
    {
        if(dao.activarAlarma(a))au.programarAlarmas(context,a);
    }
}
