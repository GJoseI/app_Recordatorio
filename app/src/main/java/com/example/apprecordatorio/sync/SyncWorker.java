package com.example.apprecordatorio.sync;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.apprecordatorio.sync.SyncManager;

public class SyncWorker extends Worker {

    public SyncWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork() {

        try {
            // Obtengo el ID del paciente pasado al Worker
            int idPaciente = getInputData().getInt("idPaciente", -1);
            if (idPaciente == -1) {
                return Result.failure();
            }

            // Ejecutar sincronización
            SyncManager sm = new SyncManager(getApplicationContext());
            sm.syncTodo(idPaciente);

            return Result.success();

        } catch (Exception e) {
            e.printStackTrace();
            return Result.retry();
        }
    }
}