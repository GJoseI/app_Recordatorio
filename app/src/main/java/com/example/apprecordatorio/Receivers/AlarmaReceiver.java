package com.example.apprecordatorio.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.content.ContextCompat;

import com.example.apprecordatorio.activities.AlarmaActivity;
import com.example.apprecordatorio.entidades.Alarma;
import com.example.apprecordatorio.servicios.AlarmaService;
import com.example.apprecordatorio.util.AlarmaUtil;

import java.util.Objects;

public class AlarmaReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("alarma!", "Alarma recibida, abriendo pantalla");

        String titulo = intent.getStringExtra("titulo");
        String descripcion = intent.getStringExtra("descripcion");
        String imagen = intent.getStringExtra("imagen");
        String tono = intent.getStringExtra("tono");
        int hora = intent.getIntExtra("hora", -1);
        int minuto = intent.getIntExtra("minuto", -1);
        int id = intent.getIntExtra("id", -1);
        int diaSemana = intent.getIntExtra("diaSemana", -1);

        Alarma r = new Alarma();
        r.setId(id);
        r.setTitulo(titulo);
        r.setDescripcion(descripcion);
        r.setTono(tono);
        r.setImagenUrl(imagen);
        r.setHora(hora);
        r.setMinuto(minuto);

        AlarmaUtil au = new AlarmaUtil();
        au.programarAlarma(context,r,diaSemana);

        Intent i = new Intent(context, AlarmaService.class);
        i.putExtra("titulo", titulo);
        if(descripcion!=null) i.putExtra("descripcion", descripcion);
        if(tono!=null) i.putExtra("tono", tono);
        if(imagen!=null)i.putExtra("imagen",imagen);
        i.putExtras(Objects.requireNonNull(intent.getExtras()));
        ContextCompat.startForegroundService(context, i);

        /*Intent i = new Intent(context, AlarmaActivity.class);
        i.putExtra("titulo", titulo);
        if(descripcion!=null) i.putExtra("descripcion", descripcion);
        if(tono!=null) i.putExtra("tono", tono);
        if(imagen!=null)i.putExtra("imagen",imagen);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        context.startActivity(i);*/



    }
}
