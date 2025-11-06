package com.example.apprecordatorio.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.apprecordatorio.R;
import com.example.apprecordatorio.fragments.TutorFragment;

public class TutorMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tutor_menu);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button vincular = findViewById(R.id.btnVincular);
        Button seguimiento = findViewById(R.id.btnSeguimiento);

        vincular.setOnClickListener(v ->{
            Intent registro = new Intent(this, VincularActivity.class);
            startActivity(registro);
        });

        seguimiento.setOnClickListener(v -> {
            Intent registro = new Intent(this, SeguimientoActivity.class);
            startActivity(registro);
        });

    }
}