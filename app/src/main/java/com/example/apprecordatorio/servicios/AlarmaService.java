package com.example.apprecordatorio.servicios;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresPermission;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.apprecordatorio.R;
import com.example.apprecordatorio.activities.AlarmaActivity;

public class AlarmaService extends Service {
    private MediaPlayer mediaPlayer;

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("ALARM SERVICE","SE EJECUTO ALARMA SEVICE");
        String titulo = intent.getStringExtra("titulo");
        String tono = intent.getStringExtra("tono");
        String descripcion = intent.getStringExtra("descripcion");
        String imagen = intent.getStringExtra("imagen");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//        AudioAttributes attributes = new AudioAttributes.Builder()
//                .setUsage(AudioAttributes.USAGE_ALARM)
//                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
//                .build();
            NotificationChannel channel = new NotificationChannel(
                    "alarma_channel",
                    "Alarmas",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription("Notificaciones de alarma");
            channel.setSound(null, null);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

                // Intent para abrir la Activity al tocar la notificación o en pantalla bloqueada
        Intent i = new Intent(this, AlarmaActivity.class);
        i.putExtra("titulo", titulo);
        i.putExtra("descripcion", descripcion);
        i.putExtra("tono", tono);
        i.putExtra("imagen", imagen);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent fullScreenIntent = PendingIntent.getActivity(
                this, 0, i, PendingIntent.FLAG_IMMUTABLE
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "alarma_channel")
                .setSmallIcon(R.drawable.alarm_24px)
                .setContentTitle(titulo)
                .setContentText(descripcion)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setDefaults(NotificationCompat.DEFAULT_VIBRATE)
                .setFullScreenIntent(fullScreenIntent, true)
                .setOngoing(false).setAutoCancel(true);

        NotificationManagerCompat.from(this).notify(1, builder.build());
        startForeground(1, builder.build());


        // Reproducir el sonido
        if (tono != null) {
            mediaPlayer = MediaPlayer.create(this, Uri.parse(tono));
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
        }

        return START_STICKY;
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        stopForeground(true);
        //stopSelf();
        Log.d("ALARM SERVICE","DESTRUYENDO ALARMA SERVICE...");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}