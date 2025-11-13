package com.example.apprecordatorio.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import com.example.apprecordatorio.Receivers.AlarmaReceiver;
import com.example.apprecordatorio.activities.AlarmaActivity;
import com.example.apprecordatorio.entidades.Alarma;

import java.util.Calendar;

public class AlarmaUtil {

    public void programarAlarmas(Context context, Alarma r) {
        if (r.isLunes()) programarAlarma(context, r, Calendar.MONDAY);
        if (r.isMartes()) programarAlarma(context, r, Calendar.TUESDAY);
        if (r.isMiercoles()) programarAlarma(context, r, Calendar.WEDNESDAY);
        if (r.isJueves()) programarAlarma(context, r, Calendar.THURSDAY);
        if (r.isViernes()) programarAlarma(context, r, Calendar.FRIDAY);
        if (r.isSabado()) programarAlarma(context, r, Calendar.SATURDAY);
        if (r.isDomingo()) programarAlarma(context, r, Calendar.SUNDAY);
    }

    public void programarAlarma(Context context, Alarma r, int diaSemana) {

        // Chequear permiso antes de programar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            context.createAttributionContext("com.example.apprecordatorio");
            AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            if (!am.canScheduleExactAlarms()) {
                Log.w("PROG ALARM", "No tiene permiso para programar alarmas exactas. Abriendo ajustes...");
                Intent intent = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                intent.setData(Uri.parse("package:" + context.getPackageName()));
                context.startActivity(intent);
                return; // salimos sin programar hasta que el usuario lo habilite
            }
        }

        Log.d("PROG ALARM","PROGRAMANDO ALARMA PARA DIA:"+diaSemana+" "+r.getHora()+" "+r.getMinuto());
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, diaSemana);
        calendar.set(Calendar.HOUR_OF_DAY, r.getHora());
        calendar.set(Calendar.MINUTE, r.getMinuto());
        calendar.set(Calendar.SECOND, 0);

        // Si la fecha ya pasó para esta semana, se pasa a la próxima
        if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
            calendar.add(Calendar.WEEK_OF_YEAR, 1);
        }

        Intent intent = new Intent(context, AlarmaReceiver.class);
        intent.putExtra("titulo", r.getTitulo());
        if(r.getDescripcion()!=null) intent.putExtra("descripcion", r.getDescripcion());
        if(r.getTono()!=null) intent.putExtra("tono", r.getTono());
        if(r.getImagenUrl()!=null)intent.putExtra("imagen",r.getImagenUrl());
        intent.putExtra("id", r.getId());
        intent.putExtra("diaSemana", diaSemana);
        intent.putExtra("hora", r.getHora());
        intent.putExtra("minuto", r.getMinuto());



        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                r.getId() + diaSemana, // ID único
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(),
                pendingIntent
        );
    }

    public void cancelarAlarmas(Context context, Alarma r) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        for (int dia = Calendar.SUNDAY; dia <= Calendar.SATURDAY; dia++) {
            int requestCode = r.getId() + dia;

            Intent intent = new Intent(context, AlarmaReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    context,
                    requestCode,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
            );

            alarmManager.cancel(pendingIntent);
        }
    }
}
