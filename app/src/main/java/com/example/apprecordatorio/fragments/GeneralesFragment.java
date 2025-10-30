package com.example.apprecordatorio.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.apprecordatorio.R;
import com.example.apprecordatorio.dialogs.AltaRecordatorioGeneral;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class GeneralesFragment extends Fragment {

    private LinearLayout containerRecordatorios;
    private FloatingActionButton btnAgregar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_generales, container, false);

        containerRecordatorios = view.findViewById(R.id.containerRecordatoriosGenerales);
        btnAgregar = view.findViewById(R.id.btnAgregarRecGral);

        btnAgregar.setOnClickListener(v -> agregarRecordatorio(inflater));

        return view;
    }


    private void agregarRecordatorio(LayoutInflater inflater) {

        View cardView = inflater.inflate(R.layout.item_recordatorio_general, containerRecordatorios, false);

        // Cambiar el título dinámicamente (puedes luego pedirlo con un diálogo)
       // TextView txtTitulo = cardView.findViewById(R.id.txtTituloRecGral);
        //txtTitulo.setText( (containerRecordatorios.getChildCount() + 1));

        // Agregar la card al contenedor
        new AltaRecordatorioGeneral().show(
                getChildFragmentManager(), "hola");
        containerRecordatorios.addView(cardView);
    }
}