package com.example.apprecordatorio.activities;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.apprecordatorio.R;
import com.example.apprecordatorio.dao.RecordatorioDao;
import com.example.apprecordatorio.dao.SeguimientoDao;
import com.example.apprecordatorio.dao.SeguimientoExternoDao;
import com.example.apprecordatorio.entidades.Alarma;
import com.example.apprecordatorio.entidades.Seguimiento;
import com.example.apprecordatorio.servicios.AlarmaService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AlarmaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setShowWhenLocked(true);
        setTurnScreenOn(true);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.activity_alarma);

        String titulo = getIntent().getStringExtra("titulo");
        int idAlarma = getIntent().getIntExtra("idAlarma", -1);
        int pacienteId = getIntent().getIntExtra("idPaciente", -1);

        TextView txtMensaje = findViewById(R.id.txtMensaje);
        txtMensaje.setText("¡" + titulo + "!");

        String imagenUri = getIntent().getStringExtra("imagen");
        if (imagenUri != null) {
            ImageView imagen = findViewById(R.id.ivAlarmaDisparada);
            imagen.setImageURI(Uri.parse(imagenUri));
            imagen.setVisibility(View.VISIBLE);
        }

        Button btnDetener = findViewById(R.id.btnDetener);
        btnDetener.setOnClickListener(v -> detenerAlarma(idAlarma, pacienteId));
    }

    private void detenerAlarma(int idAlarma, int pacienteId) {


        Intent stopIntent = new Intent(this, AlarmaService.class);
        stopIntent.setAction("STOP_ALARM");
        startService(stopIntent);


        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler mainHandler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {

            if (pacienteId != -1) {
                //SeguimientoExternoDao dao = new SeguimientoExternoDao();
                SeguimientoDao dao = new SeguimientoDao(this);
                RecordatorioDao rdao = new RecordatorioDao(this);
                Seguimiento s = new Seguimiento();
                Alarma a = new Alarma();
                a.setPacienteId(pacienteId);
                int idremoto = rdao.getIdRemoto(idAlarma);
                a.setIdRemoto(idremoto);
                Log.d("sync up seg","id alarma remota en alarma activity"+idremoto);
                Log.d("sync up seg","id paciente en activity"+pacienteId);
                s.setAlarma(a);
                s.setAtendida(true);
                long timestamp = System.currentTimeMillis();

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String mysqlTimestamp = sdf.format(new Date(timestamp));
                s.setTimestamp(mysqlTimestamp);

                dao.add(s);
            }

            mainHandler.post(() ->
                    Log.d("ALARM ACTIVITY", "Seguimiento registrado")
            );
        });

        executor.shutdown();

        finish();
    }
}