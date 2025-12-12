package com.example.apprecordatorio.negocio;

import android.content.Context;
import android.util.Log;

import com.example.apprecordatorio.dao.RecordatorioDao;
import com.example.apprecordatorio.dao.RecordatorioExternoDao;
import com.example.apprecordatorio.entidades.Alarma;
import com.example.apprecordatorio.entidades.Paciente;
import com.example.apprecordatorio.util.AlarmaUtil;


import java.util.ArrayList;
import java.util.List;

public class RecordatorioNegocio {

    private RecordatorioDao dao;
    private AlarmaUtil au;
    private RecordatorioExternoDao daoEx;

    private PacienteNegocio pneg;



    public ArrayList<Alarma>readAllFromPaciente(int id )
    {
        return daoEx.readAllFromPaciente(id);
    }

    public RecordatorioNegocio(Context context)
    {
        dao = new RecordatorioDao(context);
        au = new AlarmaUtil();
        pneg = new PacienteNegocio(context);
        daoEx = new RecordatorioExternoDao();
    }
    public List<Alarma> readAll()
    {
        List<Alarma> lista = dao.readAll();

        for(Alarma a : lista)
        {
            Log.d("EN READ NEG","id: "+a.getId());
        }
        return lista;
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
                    r.setId((int)resultado);
                    au.programarAlarmas(context, r);
                    //if(daoEx.add(r)<=0)resultado=-1;
                }
        }

        Log.e("NEG add","RESULTADO: "+resultado);
        return resultado;
    }
    public int update(Alarma r,Context context)
    {
        Log.d("sync up seg","id remoto en update"+r.getIdRemoto());

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
                r.setPacienteId(p.getId());
                au.programarAlarmas(context,r);
               // if(!daoEx.update(r))resultado = -1;
            }
        }

        Log.e("NEG update","RESULTADO: "+resultado);
        return resultado;
    }
    public int delete(Alarma r,Context context)
    {
        String nombre = "";
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
                r.setPacienteId(p.getId());
                //if (!daoEx.delete(r)) resultado = -1;
            }
            nombre = p.getNombre();
        }



        Log.e("NEG delete","RESULTADO: "+resultado+"PACIENTE: "+nombre);
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
            a.setPacienteId(p.getId());
            if(dao.desactivarAlarma(a)) {
               // daoEx.update(a);
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
            a.setPacienteId(p.getId());
            if (dao.activarAlarma(a))
            {
               // daoEx.update(a);
                au.programarAlarmas(context,a);
            }
        }

    }

    public Alarma readOneFrom(int id, int idPaciente)
    {
        return daoEx.readOneFrom(id,idPaciente);
    }

    public boolean addExterno(Alarma a)
    {
        //int id = daoEx.getLastId(a.getPacienteId());
        //a.setId(id+
        Log.d("RNEG","ID PACIENTE "+a.getPacienteId());
        return (daoEx.add(a)>0);
    }
    public List<Alarma> readAllExterno(int idPaciente)
    {
        return daoEx.readAllFromPaciente(idPaciente);
    }
    public boolean updateExterno(Alarma a)
    {
        return daoEx.update(a);
    }
    public boolean deleteExterno(Alarma a)
    {
        return daoEx.delete(a);
    }

}
