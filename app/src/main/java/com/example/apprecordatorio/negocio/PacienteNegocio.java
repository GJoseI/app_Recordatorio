package com.example.apprecordatorio.negocio;

import android.content.Context;
import android.net.Uri;

import com.example.apprecordatorio.dao.NotasExternoDao;
import com.example.apprecordatorio.dao.PacienteDao;
import com.example.apprecordatorio.dao.PacienteExternoDao;
import com.example.apprecordatorio.dao.RecordatorioDao;
import com.example.apprecordatorio.dao.RecordatorioExternoDao;
import com.example.apprecordatorio.dao.RecordatorioGralDao;
import com.example.apprecordatorio.entidades.Alarma;
import com.example.apprecordatorio.entidades.Paciente;
import com.example.apprecordatorio.entidades.Recordatorio;
import com.example.apprecordatorio.util.FileUtil;

import java.io.IOException;
import java.util.List;

public class PacienteNegocio {

    private RecordatorioDao rd;
    private RecordatorioGralDao nd;
    private PacienteDao dao;
    private PacienteExternoDao daoEx;
    private  RecordatorioExternoDao daoExAlarma;
    private NotasExternoDao notasExDao;
    private FileUtil fu;


    public  PacienteNegocio(Context context)
    {
        rd = new RecordatorioDao(context);
        nd = new RecordatorioGralDao(context);
        dao = new PacienteDao(context);
        daoEx = new PacienteExternoDao();
        daoExAlarma = new RecordatorioExternoDao();
        notasExDao = new NotasExternoDao();
        fu = new FileUtil();
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
                if(a.getImagenUrl()!= null && !a.getImagenUrl().isEmpty() && !a.getImagenUrl().equals("null"))
                {
                    try {

                        a.setImagenUrl(fu.uriToBase64(context, Uri.parse(a.getImagenUrl())));

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                int id = daoExAlarma.add(a);
                a.setIdRemoto(id);
                rd.updateIdRemoto(a);
            }
            for (Recordatorio r : notas)
            {
                r.setPacienteId(p.getId());
                gneg.update(r);
                if(r.getImagenUrl()!= null && !r.getImagenUrl().isEmpty() && !r.getImagenUrl().equals("null"))
                {
                    try {

                        r.setImagenUrl(fu.uriToBase64(context, Uri.parse(r.getImagenUrl())));

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }

                int id = notasExDao.add(r);
                r.setIdRemoto(id);
                nd.updateIdRemoto(r);
            }
        }
    }
}
