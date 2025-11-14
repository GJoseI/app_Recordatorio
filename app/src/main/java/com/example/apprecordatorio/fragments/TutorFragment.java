package com.example.apprecordatorio.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.apprecordatorio.R;
import com.example.apprecordatorio.activities.MainActivity;

public class TutorFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tutor, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button registrarse = view.findViewById(R.id.btnRegistrarse);
        Button iniciar = view.findViewById(R.id.btnIniciarSesion);

        registrarse.setOnClickListener(v -> {
            Fragment fragment = new TutorRegistroFragment();
            ((MainActivity) requireActivity()).mostrarFragmento(fragment);
        });

        iniciar.setOnClickListener(v -> {
            Fragment fragment = new TutorMenuFragment();
            ((MainActivity) requireActivity()).mostrarFragmento(fragment);
        });
    }
}