package com.example.apprecordatorio.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.apprecordatorio.Receivers.AlarmaReceiver;
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

    private void programarAlarma(Context context, Alarma r, int diaSemana) {
        Log.d("PROG ALARM","ALARMA PROGRAMANDO PARA DIA:"+diaSemana+" "+r.getHora()+" "+r.getMinuto());
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
        intent.putExtra("descripcion", r.getDescripcion());
        intent.putExtra("tono", r.getTono());
        intent.putExtra("imagen",r.getImagenUrl());

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                r.getId() + diaSemana, // ID único
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY * 7, // semanal
                pendingIntent
        );
    }
}
