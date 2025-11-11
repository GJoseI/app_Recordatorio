package com.example.apprecordatorio.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.apprecordatorio.activities.AlarmaActivity;

public class AlarmaReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("alarma!", "Alarma recibida, abriendo pantalla");

        String titulo = intent.getStringExtra("titulo");
        String descripcion = intent.getStringExtra("descripcion");
        String imagen = intent.getStringExtra("imagen");
        String tono = intent.getStringExtra("tono");


        Intent i = new Intent(context, AlarmaActivity.class);
        i.putExtra("titulo", titulo);
        if(descripcion!=null) i.putExtra("descripcion", descripcion);
        if(tono!=null) i.putExtra("tono", tono);
        if(imagen!=null)i.putExtra("imagen",imagen);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        context.startActivity(i);
    }
}
