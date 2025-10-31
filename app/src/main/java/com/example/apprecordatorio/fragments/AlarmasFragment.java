package com.example.apprecordatorio.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.apprecordatorio.R;
import com.example.apprecordatorio.RecordatorioAdapter;
import com.example.apprecordatorio.activities.CrearAlarmaActivity;
import com.example.apprecordatorio.entidades.Recordatorio;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class AlarmasFragment extends Fragment {

    private RecyclerView recyclerView;
    private FloatingActionButton fabAgregar;
    private RecordatorioAdapter adapter;
    private ImageButton btnEditar;
    private List<Recordatorio> listaRecordatorios;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_alarmas, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recyclerRecordatorios);
        fabAgregar = view.findViewById(R.id.fabAgregar);
        btnEditar = view.findViewById(R.id.btnEditar);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        listaRecordatorios = new ArrayList<>();

        // ejemplo de datos, dsp cambiamos la funcion
        //Aca cargamos la lista desde la bd segun el usuario, si no encuentra nada
        //no mostrara recordatorios y dira "no hay recs"
       /* listaRecordatorios.add(new Recordatorio(101,true,"Tomar medicación",LocalDate.now(),
                "08:30",1,1,12345678,87654321));*/
        /*
        o

        cargarRecordatorios();
        */

        // configurar el adapter
        adapter = new RecordatorioAdapter(listaRecordatorios);
        recyclerView.setAdapter(adapter);

        //Acción del botón flotante agregar
        fabAgregar.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Agregar nuevo recordatorio", Toast.LENGTH_SHORT).show();
            Intent crear = new Intent(AlarmasFragment.this.getActivity(), CrearAlarmaActivity.class);
            startActivity(crear);
        });

        /*
        btnEditar.setOnClickListener(v -> {
           //codigo editar rec
        });
         */
    }

    //  funcion aux para actualizar la lista
    private void actualizarLista(List<Recordatorio> nuevos) {
        listaRecordatorios.clear();
        listaRecordatorios.addAll(nuevos);
        adapter.notifyDataSetChanged();
    }
}