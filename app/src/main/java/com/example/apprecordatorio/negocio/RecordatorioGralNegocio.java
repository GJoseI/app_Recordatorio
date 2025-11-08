package com.example.apprecordatorio.negocio;

import android.content.Context;

import com.example.apprecordatorio.dao.RecordatorioGralDao;
import com.example.apprecordatorio.entidades.Recordatorio;

import java.util.List;

public class RecordatorioGralNegocio {

    private RecordatorioGralDao dao;

    public RecordatorioGralNegocio(Context context)
    {
        dao = new RecordatorioGralDao(context);
    }

    public List<Recordatorio> readAll()
    {
        return dao.readAll();
    }
    public long add(Recordatorio r)
    {
        return dao.add(r);
    }
    public int update(Recordatorio r)
    {
        return dao.update(r);
    }
    public int delete(Recordatorio r)
    {
        return dao.delete(r);
    }

    public Recordatorio readOne(int id){return dao.readOne(id);}
}

