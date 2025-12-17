package com.example.apprecordatorio.sync;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.example.apprecordatorio.dao.NotasExternoDao;
import com.example.apprecordatorio.dao.RecordatorioDao;
import com.example.apprecordatorio.dao.RecordatorioExternoDao;
import com.example.apprecordatorio.dao.RecordatorioGralDao;
import com.example.apprecordatorio.dao.SeguimientoDao;
import com.example.apprecordatorio.dao.SeguimientoExternoDao;
import com.example.apprecordatorio.entidades.Alarma;
import com.example.apprecordatorio.entidades.Recordatorio;
import com.example.apprecordatorio.entidades.Seguimiento;
import com.example.apprecordatorio.util.AlarmaUtil;
import com.example.apprecordatorio.util.FileUtil;

import java.io.IOException;
import java.util.List;

public class SyncManager {

    private final Context context;
    private final RecordatorioGralDao notasLocal;
    private final RecordatorioDao alarmasLocal;

    private final NotasExternoDao notasEx;
    private final RecordatorioExternoDao alarmasEx;

    private final AlarmaUtil alarmaUtil;

    private final SeguimientoExternoDao seguimientoEx;
    private final SeguimientoDao seguimientoLocal;

    private final FileUtil fu;

    public SyncManager(Context ctx) {

        this.context = ctx;

        this.notasLocal = new RecordatorioGralDao(ctx);
        this.alarmasLocal = new RecordatorioDao(ctx);

        this.notasEx = new NotasExternoDao();
        this.alarmasEx = new RecordatorioExternoDao();

        this.alarmaUtil = new AlarmaUtil();

        this.seguimientoEx = new SeguimientoExternoDao();
        this.seguimientoLocal = new SeguimientoDao(context);
        this.fu = new FileUtil();

    }


    public void syncUpSeguimiento()
    {
        List<Seguimiento> pendientes = seguimientoLocal.readAllPendingUpload();

        Log.d("sync up seg","en sync up seg");
        for(Seguimiento s : pendientes)
        {
            Log.d("sync up seg","seguimiento"+s.getId()+" id remoto:"+s.getAlarma().getIdRemoto()+" id paciente "+s.getAlarma().getPacienteId());
            if(seguimientoEx.add(s))
            {
                Log.d("sync up seg","agrego");
                s.setPending_upload(false);
                seguimientoLocal.setPendingUpload(s);
            }
        }
    }


    public void syncUpNotas(int idPaciente) {
        List<Recordatorio> pendientes = notasLocal.getAllPendingSync();

        Log.d("sync up notas", "lista de notas pendientes");
        for (Recordatorio r : pendientes) {

            Log.d("sync up notas", "nota "+r.getId()+" idRemoto"+r.getIdRemoto());
            boolean ok;
            r.setPacienteId(idPaciente);

            if (r.getIdRemoto() == 0) {

                if(r.getImagenUrl()!= null && !r.getImagenUrl().isEmpty() && !r.getImagenUrl().equals("null"))
                {
                    try {

                        r.setImagenUrl(fu.uriToBase64(context, Uri.parse(r.getImagenUrl())));

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }

                int newId = notasEx.add(r);
                Log.d("sync up notas", "new id remoto "+newId);
                ok = newId > 0;
                if (ok) {
                    r.setIdRemoto(newId);
                    notasLocal.updateIdRemoto(r);
                }
            } else {

                Log.d("sync up notas", "updateando "+r.getId()+" con id remoto"+r.getIdRemoto());

                if(r.getImagenUrl()!= null && !r.getImagenUrl().isEmpty() && !r.getImagenUrl().equals("null"))
                {
                    try {

                        Log.d("sync up notas", "convirtiendo imagen a base64"+r.getImagenUrl());
                        String base64 = fu.uriToBase64(context, Uri.parse(r.getImagenUrl()));
                        Log.d("sync up notas", "base64 length "+base64.length());
                        r.setImagenUrl(base64);

                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }

                ok = notasEx.update(r);
            }

            if (ok) {
                r.setPendingChanges(false);
                notasLocal.setPendingChanges(r);
            }
        }
    }

    public void syncUpAlarmas(int idPaciente) {
        List<Alarma> pendientes = alarmasLocal.getAllPendingSync();

        Log.d("SYNC UP","lista de alarmas pendientes");
        for (Alarma a : pendientes) {

            Log.d("SYNC UP","Alarma"+a.getId()+" id remoto"+a.getIdRemoto());
            boolean ok;

            a.setPacienteId(idPaciente);
            if (a.getIdRemoto() == 0) {


                if(a.getImagenUrl()!= null && !a.getImagenUrl().isEmpty() && !a.getImagenUrl().equals("null"))
                {
                    try {

                        a.setImagenUrl(fu.uriToBase64(context, Uri.parse(a.getImagenUrl())));

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }

                int newId = alarmasEx.add(a);
                ok = newId > 0;
                if (ok) {
                    a.setIdRemoto(newId);
                    alarmasLocal.updateIdRemoto(a);
                }
            } else {

                if(a.getImagenUrl()!= null && !a.getImagenUrl().isEmpty() && !a.getImagenUrl().equals("null"))
                {
                    try {

                        a.setImagenUrl(fu.uriToBase64(context, Uri.parse(a.getImagenUrl())));

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }

                ok = alarmasEx.update(a);

            }

            if (ok) {
                a.setPendingChanges(false);
                alarmasLocal.setPendingChanges(a);
            }
        }
    }


    public void syncDownNotas(int idPaciente) {
        List<Recordatorio> remotos = notasEx.readAllSync(idPaciente);

        for (Recordatorio remoto : remotos) {

            Recordatorio local = notasLocal.readOneByIdRemoto(remoto.getIdRemoto());

            Log.d("sync notas","nota idRemoto:"+remoto.getIdRemoto());
            if (local == null) {
                Log.d("sync notas","local dio null");

                if(remoto.getImagenUrl()!= null && !remoto.getImagenUrl().isEmpty() && !remoto.getImagenUrl().equals("null"))
                {
                    Uri uriLocal = fu.descargarImagenDesdeUrl(remoto.getImagenUrl(),context);
                    remoto.setImagenUrl(uriLocal.toString());
                }

                notasLocal.addFromSync(remoto);

            } else {
                if (remoto.getUpdatedAt() > local.getUpdatedAt()) {

                    if(!remoto.isBajaLogica())
                    {
                        if(remoto.getImagenUrl()!= null && !remoto.getImagenUrl().isEmpty() && !remoto.getImagenUrl().equals("null"))
                        {
                            fu.borrarImagenAnterior(local.getImagenUrl());
                            Uri uriLocal = fu.descargarImagenDesdeUrl(remoto.getImagenUrl(),context);
                            if(uriLocal!=null)
                            {
                                remoto.setImagenUrl(uriLocal.toString());
                            }
                        }
                    }else
                    {
                        if(remoto.getImagenUrl()!= null && !remoto.getImagenUrl().isEmpty() && !remoto.getImagenUrl().equals("null"))
                        {
                            fu.borrarImagenAnterior(local.getImagenUrl());
                        }
                    }
                    notasLocal.updateFromSync(remoto);
                }


            }
        }
    }

    public void syncDownAlarmas(int idPaciente) {
        List<Alarma> remotos = alarmasEx.readAllSync(idPaciente);

        for (Alarma remoto : remotos) {

            Log.d("sync alarmas","un registro");
            Alarma local = alarmasLocal.readOneByIdRemoto(remoto.getIdRemoto());

            if (local == null) {
                Log.d("sync","local dio null");
                if(remoto.getImagenUrl()!= null && !remoto.getImagenUrl().isEmpty() && !remoto.getImagenUrl().equals("null"))
                {
                    Uri uriLocal = fu.descargarImagenDesdeUrl(remoto.getImagenUrl(),context);
                    if(uriLocal!=null)
                    {
                        remoto.setImagenUrl(uriLocal.toString());
                    }
                }
                long id = alarmasLocal.addFromSync(remoto);
                remoto.setId((int)id);
                alarmaUtil.programarAlarmas(context,remoto);

            } else {
                Log.d("sync alarmas","alarma "+remoto.getIdRemoto());
                Log.d("sync alarmas","remoto: "+remoto.getUpdatedAt()+" local: "+local.getUpdatedAt());
                if (remoto.getUpdatedAt() > local.getUpdatedAt()) {
                    Log.d("sync alarmas","actualizando alarma");
                    remoto.setId(local.getId());
                    alarmaUtil.cancelarAlarmas(context, remoto);

                    if (!remoto.isBajaLogica()) {

                        if(remoto.getImagenUrl()!= null && !remoto.getImagenUrl().isEmpty() && !remoto.getImagenUrl().equals("null"))
                        {
                            fu.borrarImagenAnterior(local.getImagenUrl());
                            Uri uriLocal = fu.descargarImagenDesdeUrl(remoto.getImagenUrl(),context);
                            remoto.setImagenUrl(uriLocal.toString());
                        }

                        alarmaUtil.programarAlarmas(context, remoto);
                    }else
                    {
                        if(remoto.getImagenUrl()!= null)
                        {
                            fu.borrarImagenAnterior(local.getImagenUrl());
                        }
                    }
                    alarmasLocal.updateFromSync(remoto);

                }
            }
        }
    }

    /** Sincronización completa */
    public void syncTodo(int idPaciente) {

        syncDownNotas(idPaciente);
        syncDownAlarmas(idPaciente);


        syncUpNotas(idPaciente);
        syncUpAlarmas(idPaciente);
        syncUpSeguimiento();




    }
}
