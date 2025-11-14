package com.example.apprecordatorio.negocio;

import android.content.Context;
import android.util.Log;

import com.example.apprecordatorio.dao.RecordatorioDao;
import com.example.apprecordatorio.dao.RecordatorioExternoDao;
import com.example.apprecordatorio.entidades.Alarma;
import com.example.apprecordatorio.entidades.Paciente;
import com.example.apprecordatorio.util.AlarmaUtil;


import java.util.List;

public class RecordatorioNegocio {

    private RecordatorioDao dao;
    private AlarmaUtil au;
    private RecordatorioExternoDao daoEx;

    private PacienteNegocio pneg;

    public RecordatorioNegocio(Context context)
    {
        dao = new RecordatorioDao(context);
        au = new AlarmaUtil();
        pneg = new PacienteNegocio(context);
        daoEx = new RecordatorioExternoDao();
    }
    public List<Alarma> readAll()
    {
        return dao.readAll();
    }
    public long add(Alarma r, Context context)
    {
        long resultado = 0;
        Paciente p = pneg.read();
        if(p==null)
        {
            resultado = dao.add(r);
            if(resultado>0) au.programarAlarmas(context, r);
        }else {
            r.setPacienteId(p.getId());
            resultado = dao.add(r);
            if(resultado>0)
            {
                au.programarAlarmas(context, r);
                if(!daoEx.add(r))resultado = -1;// = error bd
            }
        }

        Log.e("NEG","RESULTADO: "+resultado);
        return resultado;
    }
    public int update(Alarma r,Context context)
    {
        au.cancelarAlarmas(context,r);

        int resultado = 0;
        Paciente p= pneg.read();
        if (p==null)
        {
            resultado =  dao.update(r);
            if (resultado>0 && !r.isBajaLogica())
            {
                au.programarAlarmas(context,r);
            }
        }else {
            resultado =  dao.update(r);
            if (resultado>0)
            {
                au.programarAlarmas(context,r);
                if(!daoEx.update(r))resultado = -1;
            }
        }

        Log.e("NEG","RESULTADO: "+resultado);
        return resultado;
    }
    public int delete(Alarma r,Context context)
    {
        int resultado = 0;
        Paciente p= pneg.read();

        if (p==null)
        {
            resultado = dao.delete(r);
            if(resultado>0)au.cancelarAlarmas(context,r);

        }else
        {
            resultado = dao.delete(r);
            if(resultado>0) {
                au.cancelarAlarmas(context, r);
                if (!daoEx.delete(r)) resultado = -1;
            }
        }

        Log.e("NEG","RESULTADO: "+resultado);
        return resultado;
    }

    public void desactivarAlarma(Alarma a,Context context)
    {
        Paciente p= pneg.read();

        if (p==null)
        {
            if(dao.desactivarAlarma(a))au.cancelarAlarmas(context,a);
        }else
        {
          a.setEstado(false);
            if(dao.desactivarAlarma(a)) {
                daoEx.update(a);
                au.cancelarAlarmas(context, a);
            };

        }
    }
    public void activarAlarma(Alarma a,Context context)
    {
        Paciente p= pneg.read();

        if (p==null) {
            if(dao.activarAlarma(a))au.programarAlarmas(context,a);
        }else {
            a.setEstado(true);
            if (dao.activarAlarma(a))
            {
                daoEx.update(a);
                au.programarAlarmas(context,a);
            }
        }

    }
}
