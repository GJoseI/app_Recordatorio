package com.example.apprecordatorio.sync;

import android.content.Context;
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

    public SyncManager(Context ctx) {

        this.context = ctx;

        this.notasLocal = new RecordatorioGralDao(ctx);
        this.alarmasLocal = new RecordatorioDao(ctx);

        this.notasEx = new NotasExternoDao();
        this.alarmasEx = new RecordatorioExternoDao();

        this.alarmaUtil = new AlarmaUtil();

        this.seguimientoEx = new SeguimientoExternoDao();
        this.seguimientoLocal = new SeguimientoDao(context);

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


                int newId = notasEx.add(r);
                Log.d("sync up notas", "new id remoto "+newId);
                ok = newId > 0;
                if (ok) {
                    r.setIdRemoto(newId);
                    notasLocal.updateIdRemoto(r);
                }
            } else {

                Log.d("sync up notas", "updateando "+r.getId()+" con id remoto"+r.getIdRemoto());

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

        for (Alarma a : pendientes) {

            Log.d("SYNC UP","Alarma"+a.getId()+" id remoto"+a.getIdRemoto());
            boolean ok;

            a.setPacienteId(idPaciente);
            if (a.getIdRemoto() == 0) {

                int newId = alarmasEx.add(a);
                ok = newId > 0;
                if (ok) {
                    a.setIdRemoto(newId);
                    alarmasLocal.updateIdRemoto(a);
                }
            } else {

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

                notasLocal.addFromSync(remoto);

            } else {
                if (remoto.getUpdatedAt() > local.getUpdatedAt()) {
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
                long id = alarmasLocal.addFromSync(remoto);
                remoto.setId((int)id);
                alarmaUtil.programarAlarmas(context,remoto);

            } else {
                Log.d("sync alarmas","alarma "+remoto.getIdRemoto());
                Log.d("sync alarmas","remoto: "+remoto.getUpdatedAt()+" local: "+local.getUpdatedAt());
                if (remoto.getUpdatedAt() > local.getUpdatedAt()) {
                    remoto.setId(local.getId());
                    alarmaUtil.cancelarAlarmas(context, remoto);
                    alarmasLocal.updateFromSync(remoto);

                    if (!remoto.isBajaLogica()) {
                        alarmaUtil.programarAlarmas(context, remoto);
                    }
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
