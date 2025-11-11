package com.example.apprecordatorio.Receivers;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Vibrator vibrator = (Vibrator) context.getSystemService(context.VIBRATOR_SERVICE);
        vibrator.vibrate(VibrationEffect.createOneShot(4000, VibrationEffect.DEFAULT_AMPLITUDE));

        Toast.makeText(context, "Alarma", Toast.LENGTH_LONG).show();

        Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if(alarmUri == null){
            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        }

        Ringtone ringtone = RingtoneManager.getRingtone(context, alarmUri);
        if(ringtone != null && !ringtone.isPlaying()){
            ringtone.play();
        }

        NotificationChannel channel = new NotificationChannel("alarma_channel", "Alarmas", NotificationManager.IMPORTANCE_HIGH);
        channel.setDescription("Canal de alarmas");

        NotificationManager manager = context.getSystemService(NotificationManager.class);
        if(manager != null) manager.createNotificationChannel(channel);

        Notification notificacion = new NotificationCompat.Builder(context,"alarma_channel")
                .setContentTitle("Alarma activa").setContentText("Alarma sonando")
                .setPriority(NotificationCompat.PRIORITY_HIGH).setAutoCancel(true)
                .build();

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
        NotificationManagerCompat.from(context).notify(1001, notificacion);
        }

    }
}
