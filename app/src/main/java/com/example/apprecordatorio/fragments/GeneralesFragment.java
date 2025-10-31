package com.example.apprecordatorio.fragments;

import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.apprecordatorio.R;
import com.example.apprecordatorio.dao.RecordatorioGralDao;
import com.example.apprecordatorio.dialogs.AltaRecordatorioGeneral;
import com.example.apprecordatorio.entidades.Recordatorio;
import com.example.apprecordatorio.interfaces.OnRecordatorioGuardadoListener;
import com.example.apprecordatorio.negocio.RecordatorioGralNegocio;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;


public class GeneralesFragment extends Fragment implements OnRecordatorioGuardadoListener {

    private LinearLayout containerRecordatorios;
    private FloatingActionButton btnAgregar;

    private List<Recordatorio> lista;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_generales, container, false);

        containerRecordatorios = view.findViewById(R.id.containerRecordatoriosGenerales);
        btnAgregar = view.findViewById(R.id.btnAgregarRecGral);

        btnAgregar.setOnClickListener(v -> agregarRecordatorio(inflater));

        cargarRecordatorios(inflater);

        return view;
    }


    private void agregarRecordatorio(LayoutInflater inflater) {

       // View cardView = inflater.inflate(R.layout.item_recordatorio_general, containerRecordatorios, false);

        // Cambiar el título dinámicamente (puedes luego pedirlo con un diálogo)
       //
        //txtTitulo.setText( (containerRecordatorios.getChildCount() + 1));

        // Agregar la card al contenedor

        AltaRecordatorioGeneral dialog = new AltaRecordatorioGeneral();
        dialog.setOnRecordatorioGuardadoListener(this); //  registramos este fragment como "oyente"
        dialog.show(getChildFragmentManager(), "hola");
    }

    private void cargarRecordatorios(LayoutInflater inflater)
    {
        containerRecordatorios.removeAllViews();
        RecordatorioGralNegocio neg = new RecordatorioGralNegocio(requireContext());
        lista = neg.readAll();

        if(lista!=null)
        {
            for (Recordatorio r : lista)
            {
                View cardView = inflater.inflate(R.layout.item_recordatorio_general, containerRecordatorios, false);

                TextView txtTitulo = cardView.findViewById(R.id.txtTituloRecGral);

                txtTitulo.setText(r.getTitulo());

                containerRecordatorios.addView(cardView);
            }
        }
    }

    @Override
    public void onRecordatorioGuardado() {
        cargarRecordatorios(LayoutInflater.from(requireContext()));
    }
}