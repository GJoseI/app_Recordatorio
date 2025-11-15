package com.example.apprecordatorio.negocio;

import android.content.Context;
import android.util.Log;

import com.example.apprecordatorio.dao.NotasExternoDao;
import com.example.apprecordatorio.dao.RecordatorioGralDao;
import com.example.apprecordatorio.entidades.Alarma;
import com.example.apprecordatorio.entidades.Paciente;
import com.example.apprecordatorio.entidades.Recordatorio;

import java.util.List;

public class RecordatorioGralNegocio {

    private RecordatorioGralDao dao;
    private NotasExternoDao daoEx;

    private PacienteNegocio pneg;

    public RecordatorioGralNegocio(Context context)
    {

        dao = new RecordatorioGralDao(context);
        daoEx = new NotasExternoDao();
        pneg = new PacienteNegocio(context);
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
/*
    public List<Recordatorio> readAll()
    {
        List<Recordatorio> lista = dao.readAll();

        for(Recordatorio a : lista)
        {
            Log.d("EN READ NEG","id: "+a.getId());
        }
        return lista;
    }
    public long add(Recordatorio r)
    {
        long resultado = 0;
        Paciente p = pneg.read();
        if(p==null)
        {
            resultado = dao.add(r);
        }else {
            r.setPacienteId(p.getId());
            resultado = dao.add(r);
            if(resultado>0)
            {
                int id = dao.traerMaximoId();
                r.setId(id);
                if(!daoEx.add(r))resultado=-1;
            }
        }

        Log.e("NEG add","RESULTADO: "+resultado);
        return resultado;
    }
    public int update(Recordatorio r)
    {

        int resultado = 0;
        Paciente p= pneg.read();
        if (p==null)
        {
            resultado =  dao.update(r);
        }else {
            resultado =  dao.update(r);
            if (resultado>0)
            {
                r.setPacienteId(p.getId());
                if(!daoEx.update(r))resultado = -1;
            }
        }
        Log.e("NEG update","RESULTADO: "+resultado);
        return resultado;
    }
    public int delete(Recordatorio r)
    {
        String nombre = "";
        int resultado = 0;
        Paciente p= pneg.read();

        if (p==null)
        {
            resultado = dao.delete(r);

        }else
        {
            resultado = dao.delete(r);
            if(resultado>0) {
                r.setPacienteId(p.getId());
                if (!daoEx.delete(r)) resultado = -1;
            }
            nombre = p.getNombre();
        }
        Log.e("NEG delete","RESULTADO: "+resultado+"PACIENTE: "+nombre);
        return resultado;
    }
*/
}

