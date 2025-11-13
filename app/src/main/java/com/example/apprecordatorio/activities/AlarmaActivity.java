package com.example.apprecordatorio.activities;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
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
import com.example.apprecordatorio.servicios.AlarmaService;

public class AlarmaActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        // Métodos modernos (Android 8.1+)
        setShowWhenLocked(true);
        setTurnScreenOn(true);

        // Flag extra para mantener la pantalla encendida
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.activity_alarma);

        String titulo = getIntent().getStringExtra("titulo");

        TextView txtMensaje = findViewById(R.id.txtMensaje);
        txtMensaje.setText("¡Alarma: " + titulo + "!");

        String imagenUri = getIntent().getStringExtra("imagen");


        String tono = getIntent().getStringExtra("tono");

        if(imagenUri!=null)
        {
            ImageView imagen = findViewById(R.id.ivAlarmaDisparada);
            imagen.setImageURI(Uri.parse(imagenUri));
            imagen.setVisibility(View.VISIBLE);
        }


        Button btnDetener = findViewById(R.id.btnDetener);
        btnDetener.setOnClickListener(v -> detenerAlarma());

        // Reproducir sonido
      /*  mediaPlayer = MediaPlayer.create(this, Uri.parse(tono));
        //mediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();*/
    }

    private void detenerAlarma() {
        /*if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }*/
        Intent stopIntent = new Intent(this, AlarmaService.class);
        stopService(stopIntent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        detenerAlarma();
    }
}