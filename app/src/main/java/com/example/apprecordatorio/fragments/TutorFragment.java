package com.example.apprecordatorio.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.apprecordatorio.R;
import com.example.apprecordatorio.activities.CrearAlarmaActivity;
import com.example.apprecordatorio.activities.TutorMenuActivity;
import com.example.apprecordatorio.activities.TutorRegistroActivity;

public class TutorFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view =inflater.inflate(R.layout.fragment_tutor, container, false);

        Button registrarse = view.findViewById(R.id.btnRegistrarse);
        Button iniciar = view.findViewById(R.id.btnIniciarSesion);

        registrarse.setOnClickListener(v -> {

                    Intent registro = new Intent(TutorFragment.this.getActivity(), TutorRegistroActivity.class);
                    startActivity(registro);
                });

        iniciar.setOnClickListener(v -> {

            Intent registro = new Intent(TutorFragment.this.getActivity(), TutorMenuActivity.class);
            startActivity(registro);
        });
        return view;
    }
}