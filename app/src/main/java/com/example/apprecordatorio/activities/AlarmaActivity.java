package com.example.apprecordatorio.activities;

import android.media.MediaPlayer;
import android.os.Bundle;
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

public class AlarmaActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
        );
        setContentView(R.layout.activity_alarma);

        String titulo = getIntent().getStringExtra("titulo");

        TextView txtMensaje = findViewById(R.id.txtMensaje);
        txtMensaje.setText("¡Alarma: " + titulo + "!");

        ImageView imagen = findViewById(R.id.ivAlarmaDisparada);
        imagen.setVisibility(View.VISIBLE);

        Button btnDetener = findViewById(R.id.btnDetener);
        btnDetener.setOnClickListener(v -> detenerAlarma());

        // Reproducir sonido
        mediaPlayer = MediaPlayer.create(this, R.raw.terrariasounds);
        //mediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
    }

    private void detenerAlarma() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        detenerAlarma();
    }
}