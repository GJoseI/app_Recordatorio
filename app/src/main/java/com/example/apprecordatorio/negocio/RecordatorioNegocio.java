package com.example.apprecordatorio.negocio;

import android.content.Context;

import com.example.apprecordatorio.dao.RecordatorioDao;
import com.example.apprecordatorio.entidades.Alarma;


import java.util.List;

public class RecordatorioNegocio {

    private RecordatorioDao dao;

    public RecordatorioNegocio(Context context)
    {
        dao = new RecordatorioDao(context);
    }

    public List<Alarma> readAll()
    {
        return dao.readAll();
    }
    public long add(Alarma r)
    {
        return dao.add(r);
    }
    public int update(Alarma r)
    {
        return dao.update(r);
    }
    public int delete(Alarma r)
    {
        return dao.delete(r);
    }

}
