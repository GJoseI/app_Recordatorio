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
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresPermission;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.apprecordatorio.R;
import com.example.apprecordatorio.activities.AlarmaActivity;
import com.example.apprecordatorio.dao.RecordatorioDao;
import com.example.apprecordatorio.dao.SeguimientoDao;
import com.example.apprecordatorio.dao.SeguimientoExternoDao;
import com.example.apprecordatorio.entidades.Alarma;
import com.example.apprecordatorio.entidades.Seguimiento;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AlarmaService extends Service {
    private MediaPlayer mediaPlayer;

    private static boolean isRunning = false;
    private Handler handler = new Handler();
    private Runnable timeoutRunnable;
    private boolean atendida = false;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && "STOP_ALARM".equals(intent.getAction())) {
            atendida = true;
            if (mediaPlayer != null) {
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;
            }
            handler.removeCallbacks(timeoutRunnable);
            stopSelf();
            return START_NOT_STICKY;
        }

        if (isRunning) {
            Log.w("ALARM SERVICE", "Service ya en ejecución, ignorando nuevo start");
            return START_NOT_STICKY;
        }

        isRunning = true;

        // ---- MANEJO DE STOP ----


        Log.d("ALARM SERVICE","SE EJECUTO ALARMA SERVICE");

        String titulo = intent.getStringExtra("titulo");
        String tono = intent.getStringExtra("tono");
        String descripcion = intent.getStringExtra("descripcion");
        String imagen = intent.getStringExtra("imagen");
        int idAlarma = intent.getIntExtra("id",-1);
        int pacienteId = intent.getIntExtra("pacienteId",-1);

        // ---- NOTIFICACIÓN ----
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "alarma_channel",
                    "Alarmas",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setSound(null, null);
            getSystemService(NotificationManager.class).createNotificationChannel(channel);
        }

        Intent i = new Intent(this, AlarmaActivity.class);
        i.putExtra("titulo", titulo);
        i.putExtra("descripcion", descripcion);
        i.putExtra("tono", tono);
        i.putExtra("imagen", imagen);
        i.putExtra("idAlarma", idAlarma);
        i.putExtra("idPaciente", pacienteId);
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
                .setOngoing(true);

        startForeground(1, builder.build());

        // ---- SONIDO ----
        if (tono != null) {
            mediaPlayer = MediaPlayer.create(this, Uri.parse(tono));
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
        }

        // ---- TIMEOUT ----
        apagarEn30(idAlarma, pacienteId);

        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        isRunning = false;
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        stopForeground(true);
        Log.d("ALARM SERVICE","DESTRUYENDO ALARMA SERVICE...");
        super.onDestroy();
    }

    public void apagarEn30(int idAlarma,int pacienteId) {
        long timeout = 30000;

        timeoutRunnable = () -> {
            if (!atendida) {
                Log.d("ALARM SERVICE", "Tiempo agotado. Alarma NO ATENDIDA");

                stopForeground(true);
                stopSelf();

                ExecutorService executor = Executors.newSingleThreadExecutor();

                executor.execute(() -> {
                    if(pacienteId != -1) {
                        //SeguimientoExternoDao dao = new SeguimientoExternoDao();
                        SeguimientoDao dao = new SeguimientoDao(this);
                        RecordatorioDao rdao = new RecordatorioDao(this);
                        Seguimiento s = new Seguimiento();
                        Alarma a = new Alarma();
                        a.setPacienteId(pacienteId);
                        int idremoto = rdao.getIdRemoto(idAlarma);
                        a.setIdRemoto(idremoto);
                        s.setAlarma(a);
                        long timestamp = System.currentTimeMillis();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String mysqlTimestamp = sdf.format(new Date(timestamp));
                        s.setTimestamp(mysqlTimestamp);
                        s.setAtendida(false);
                       long ok = dao.add(s);
                       Log.d("ALARM SERVICE","Seguimiento agregado en timeout: "+ok);
                    }
                });

                executor.shutdown();
            }
        };

        handler.postDelayed(timeoutRunnable, timeout);
    }
}