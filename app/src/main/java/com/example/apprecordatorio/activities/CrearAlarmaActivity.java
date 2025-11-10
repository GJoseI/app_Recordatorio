package com.example.apprecordatorio.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.apprecordatorio.R;
import com.example.apprecordatorio.fragments.AlarmasFragment;

import java.util.ArrayList;
import java.util.List;

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
    private Switch swModo;
    private View layoutPrincipal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SharedPreferences prefs = getSharedPreferences("config", MODE_PRIVATE);
        boolean modoClaro = prefs.getBoolean("modo_claro", true);
        AppCompatDelegate.setDefaultNightMode(
                modoClaro ? AppCompatDelegate.MODE_NIGHT_NO : AppCompatDelegate.MODE_NIGHT_YES
        );

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_crear_alarma);

        innitVars();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 🟢 Inicializar el switch de modo claro/oscuro
        swModo = findViewById(R.id.swModo);
        swModo.setChecked(modoClaro);

        swModo.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Guardar preferencia
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("modo_claro", isChecked);
            editor.apply();

            // Cambiar el modo visual
            AppCompatDelegate.setDefaultNightMode(
                    isChecked ? AppCompatDelegate.MODE_NIGHT_NO : AppCompatDelegate.MODE_NIGHT_YES
            );
        });

        btGuardar.setOnClickListener(view -> guardarAlarma(agregarDias()));
        btCancelar.setOnClickListener(view -> volverAtras());

        Spinner spinner = findViewById(R.id.spAlarmaTono);
        String tonos[] = {"tono 1: ejemplo", "tono 2: ejemplo"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_item, tonos);
        spinner.setAdapter(adapter);

    }

    public void volverAtras(){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    public void guardarAlarma(List<ToggleButton> botonesDias){
        int hora = tpHora.getHour();
        int minuto = tpHora.getMinute();
        String descripcion = etDesc.getText() != null ? etDesc.getText().toString() : "";
        String tono = spAlarmaTono.getSelectedItem() != null ? spAlarmaTono.getSelectedItem().toString() : "";
        List<String> dias = obtenerDiasSeleccionados(botonesDias);

        String mensaje = "Alarma guardada a las " + hora + ":" + String.format("%02d", minuto)
                + "\nDías: " + dias
                + "\nTono: " + tono
                + "\nDescripción: " + descripcion;

        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show();
    }

    public void innitVars(){
        tpHora = findViewById(R.id.tpHora);
        btDom = findViewById(R.id.btDom);
        btLu = findViewById(R.id.btLu);
        btMar = findViewById(R.id.btMar);
        btMie = findViewById(R.id.btMie);
        btJue = findViewById(R.id.btJue);
        btVie = findViewById(R.id.btVie);
        btSa = findViewById(R.id.btSa);
        spAlarmaTono = findViewById(R.id.spAlarmaTono);
        etDesc = findViewById(R.id.etDesc);
        btCancelar = findViewById(R.id.btnCancelar);
        btGuardar = findViewById(R.id.btnGuardar);
    }

    public List<String> obtenerDiasSeleccionados(List<ToggleButton> botonesDias){
        List<String> diasSeleccionados = new ArrayList<>();
        for (ToggleButton btn : botonesDias){
            if(btn.isChecked()) {
                //hace algo, prob los pone en una lista y la devuelve
                //asi que seguro cambio la funcion y no va a ser void

                //hace una lista de string, y añade el gettext del btn.
                diasSeleccionados.add(btn.getTextOn().toString());
            }
        }
        return diasSeleccionados;
    }

    public List<ToggleButton> agregarDias(){
        List<ToggleButton> botonesDias = new ArrayList<>();
        botonesDias.add(btDom);
        botonesDias.add(btLu);
        botonesDias.add(btMar);
        botonesDias.add(btMie);
        botonesDias.add(btJue);
        botonesDias.add(btVie);
        botonesDias.add(btSa);

        return botonesDias;
    }
}