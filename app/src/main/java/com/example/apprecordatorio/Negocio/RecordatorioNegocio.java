package com.example.apprecordatorio.Negocio;

import android.content.Context;
import android.util.Log;

import com.example.apprecordatorio.DAO.RecordatorioDao;
import com.example.apprecordatorio.DAO.RecordatorioExternoDao;
import com.example.apprecordatorio.Entidades.Alarma;
import com.example.apprecordatorio.Entidades.Paciente;
import com.example.apprecordatorio.Util.AlarmaUtil;
import com.example.apprecordatorio.Util.FileUtil;


import java.util.List;

public class RecordatorioNegocio {

    private RecordatorioDao dao;
    private AlarmaUtil au;
    private RecordatorioExternoDao daoEx;

    private PacienteNegocio pneg;

    private FileUtil fu;


    public List<Alarma>readAllFromPaciente(int id )
    {
        return daoEx.readAllFromPaciente(id);
    }

    public RecordatorioNegocio(Context context)
    {
        dao = new RecordatorioDao(context);
        au = new AlarmaUtil();
        pneg = new PacienteNegocio(context);
        daoEx = new RecordatorioExternoDao();
        fu = new FileUtil();
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
                resultado = dao.add(r);
                if(resultado>0)
                {
                    r.setId((int)resultado);
                    au.programarAlarmas(context, r);
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
        int resultado = 0;




            String img = r.getImagenUrl();
            resultado = dao.delete(r);
            if(resultado>0)
            {
                if(img!=null)
                {
                    fu.borrarImagenAnterior(r.getImagenUrl());
                }
                au.cancelarAlarmas(context,r);
            }


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
         a.setUpdatedAt(System.currentTimeMillis());
        return (daoEx.add(a)>0);
    }
    public List<Alarma> readAllExterno(int idPaciente)
    {
        return daoEx.readAllFromPaciente(idPaciente);
    }
    public boolean updateExterno(Alarma a)
    {
        a.setUpdatedAt(System.currentTimeMillis());
        return daoEx.update(a);
    }
    public boolean deleteExterno(Alarma a)
    {
        a.setUpdatedAt(System.currentTimeMillis());
        return daoEx.delete(a);
    }

}
