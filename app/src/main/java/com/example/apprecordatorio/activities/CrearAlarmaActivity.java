package com.example.apprecordatorio.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.ToggleButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.apprecordatorio.R;
import com.example.apprecordatorio.fragments.AlarmasFragment;

public class CrearAlarmaActivity extends AppCompatActivity {
    private TimePicker tpHora;
    private ToggleButton btDom;
    private ToggleButton btLu;
    private ToggleButton btMar;
    private ToggleButton btMie;
    private ToggleButton btJue;
    private ToggleButton btVie;
    private ToggleButton btSa;
    private Spinner spAlarmaTono;
    private EditText etDesc;
    private Button btCancelar;
    private Button btGuardar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_crear_alarma);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        innitVars();

        btGuardar.setOnClickListener(view -> guardarAlarma());
        btCancelar.setOnClickListener(view -> volverAtras());
    }

    public void volverAtras(){
        Intent i = new Intent(this, AlarmasFragment.class);
        startActivity(i);
    }

    public void guardarAlarma(){

    }

    public void innitVars(){
        tpHora.findViewById(R.id.tpHora);
        btDom.findViewById(R.id.btDom);
        btLu.findViewById(R.id.btLu);
        btMar.findViewById(R.id.btMar);
        btMie.findViewById(R.id.btMie);
        btJue.findViewById(R.id.btJue);
        btVie.findViewById(R.id.btVie);
        btSa.findViewById(R.id.btSa);
        spAlarmaTono.findViewById(R.id.spAlarmaTono);
        etDesc.findViewById(R.id.etDesc);
    }
}