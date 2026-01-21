package com.example.apprecordatorio.Fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;


import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apprecordatorio.R;
import com.example.apprecordatorio.Dialogs.AltaRecordatorio;
import com.example.apprecordatorio.Dialogs.ModificacionRecordatorio;
import com.example.apprecordatorio.Entidades.Alarma;
import com.example.apprecordatorio.DAOInterfaces.OnRecordatorioGuardadoListener;
import com.example.apprecordatorio.Negocio.RecordatorioNegocio;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class AlarmasFragment extends Fragment implements OnRecordatorioGuardadoListener {


    private FloatingActionButton fabAgregar;

    private List<Alarma> lista;
    private LinearLayout containerRecordatorios;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_alarmas, container, false);

         containerRecordatorios = view.findViewById(R.id.containerRecordatoriosAlarmas);

        cargarRecordatorios();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fabAgregar = view.findViewById(R.id.fabAgregar);

        fabAgregar.setOnClickListener(v -> {
            agregarRecordatorio();

        });

    }


    private void agregarRecordatorio() {

        AltaRecordatorio dialog = new AltaRecordatorio();
        dialog.setOnRecordatorioGuardadoListener(this);
       // currentDialog = dialog; // guarda una referencia
        dialog.show(getChildFragmentManager(), "hola");
    }

    @Override
    public void onRecordatorioGuardado() {
        cargarRecordatorios();
    }

    private void cargarRecordatorios()
    {
        LayoutInflater inflater = getLayoutInflater();
        containerRecordatorios.removeAllViews();
        RecordatorioNegocio neg = new RecordatorioNegocio(requireContext());
        lista = neg.readAll();

        if(lista!=null)
        {
            for (Alarma r : lista)
            {
                View cardView = inflater.inflate(R.layout.item_recordatorio, containerRecordatorios, false);

                TextView txtTitulo = cardView.findViewById(R.id.tvTitulo);
                ImageButton btnBorrar = cardView.findViewById(R.id.btnBorrar);
                ImageButton btnEditar = cardView.findViewById(R.id.btnEditar);
                Switch sw = cardView.findViewById(R.id.sEstado);
                TextView tvDias = cardView.findViewById(R.id.tvDias);
                TextView tvHora = cardView.findViewById(R.id.tvHora);
                TextView tvMinuto = cardView.findViewById(R.id.tvMinutos);
                TextView dosPuntos = cardView.findViewById(R.id.tvDosPuntos);


                tvHora.setText(String.valueOf(r.getHora()));
                tvMinuto.setText(String.valueOf(r.getMinuto()));

                String dias = "";


                    if(r.isDomingo()) dias+="D";
                    if(r.isLunes()) dias+=" L";
                    if(r.isMartes())dias+=" Ma";
                    if(r.isMiercoles())dias+=" Mi";
                    if(r.isJueves())dias+=" J";
                    if(r.isViernes())dias+=" V";
                    if(r.isSabado())dias+=" S";

                    tvDias.setText(dias);


                    sw.setOnClickListener(v->{
                        if(sw.isChecked()){

                            ExecutorService executor = Executors.newSingleThreadExecutor();
                            Handler mainHandler = new Handler(Looper.getMainLooper());

                            executor.execute(() -> {

                                neg.activarAlarma(r,requireContext());

                                mainHandler.post(() -> {

                                    cardView.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.fondoElementoSeleccionado));
                                    txtTitulo.setTextColor(ContextCompat.getColor(requireContext(),R.color.letraBlanca));
                                    btnEditar.setColorFilter(ContextCompat.getColor(requireContext(), R.color.letraBlanca));
                                    btnBorrar.setColorFilter(ContextCompat.getColor(requireContext(), R.color.letraBlanca));
                                    tvDias.setTextColor(ContextCompat.getColor(requireContext(),R.color.letraBlanca));
                                    tvHora.setTextColor(ContextCompat.getColor(requireContext(),R.color.letraBlanca));
                                    tvMinuto.setTextColor(ContextCompat.getColor(requireContext(),R.color.letraBlanca));
                                    dosPuntos.setTextColor(ContextCompat.getColor(requireContext(),R.color.letraBlanca));
                                });
                            });



                        }else{

                            ExecutorService executor = Executors.newSingleThreadExecutor();
                            Handler mainHandler = new Handler(Looper.getMainLooper());

                            executor.execute(() -> {

                                neg.desactivarAlarma(r,requireContext());

                                mainHandler.post(() -> {

                                    cardView.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.fondoElementoOscuro));
                                    txtTitulo.setTextColor(ContextCompat.getColor(requireContext(),R.color.letraGris));
                                    btnEditar.setColorFilter(ContextCompat.getColor(requireContext(), R.color.letraGris));
                                    btnBorrar.setColorFilter(ContextCompat.getColor(requireContext(), R.color.letraGris));
                                    tvDias.setTextColor(ContextCompat.getColor(requireContext(),R.color.letraGris));
                                    tvHora.setTextColor(ContextCompat.getColor(requireContext(),R.color.letraGris));
                                    tvMinuto.setTextColor(ContextCompat.getColor(requireContext(),R.color.letraGris));
                                    dosPuntos.setTextColor(ContextCompat.getColor(requireContext(),R.color.letraGris));
                                });
                            });



                        }
                    });

                btnBorrar.setOnClickListener(v ->{
                    confirmarBorrado(r,neg);
                });


                btnEditar.setOnClickListener(v->{
                    editarRecordatorio(r);
                });

                txtTitulo.setText(r.getTitulo());

                if(r.isEstado())
                {
                    sw.setChecked(true);
                    cardView.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.fondoElementoSeleccionado));
                    txtTitulo.setTextColor(ContextCompat.getColor(requireContext(),R.color.letraBlanca));
                    btnEditar.setColorFilter(ContextCompat.getColor(requireContext(), R.color.letraBlanca));
                    btnBorrar.setColorFilter(ContextCompat.getColor(requireContext(), R.color.letraBlanca));
                    tvDias.setTextColor(ContextCompat.getColor(requireContext(),R.color.letraBlanca));
                    tvHora.setTextColor(ContextCompat.getColor(requireContext(),R.color.letraBlanca));
                    tvMinuto.setTextColor(ContextCompat.getColor(requireContext(),R.color.letraBlanca));
                    dosPuntos.setTextColor(ContextCompat.getColor(requireContext(),R.color.letraBlanca));
                }else
                {
                    cardView.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.fondoElementoOscuro));
                    txtTitulo.setTextColor(ContextCompat.getColor(requireContext(),R.color.letraGris));
                    btnEditar.setColorFilter(ContextCompat.getColor(requireContext(), R.color.letraGris));
                    btnBorrar.setColorFilter(ContextCompat.getColor(requireContext(), R.color.letraGris));
                    tvDias.setTextColor(ContextCompat.getColor(requireContext(),R.color.letraGris));
                    tvHora.setTextColor(ContextCompat.getColor(requireContext(),R.color.letraGris));
                    tvMinuto.setTextColor(ContextCompat.getColor(requireContext(),R.color.letraGris));
                    dosPuntos.setTextColor(ContextCompat.getColor(requireContext(),R.color.letraGris));
                }
                containerRecordatorios.addView(cardView);
            }
        }
    }

    public void borrarRecordatorio(Alarma r, RecordatorioNegocio neg)
    {

        Log.d("ALA FRAG","id de la alarma: "+r.getId());

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler mainHandler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            int insertado = neg.delete(r, requireContext());


            mainHandler.post(() -> {

                if (insertado>0) {

                    Toast.makeText(requireContext(), "borrado con exito.", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(requireContext(),"Error al borrar!",Toast.LENGTH_SHORT).show();
                }
                cargarRecordatorios();
            });
        });
             /*
        if(neg.delete(r,requireContext())>0)
        {
            Toast.makeText(requireContext(), "borrado con exito.", Toast.LENGTH_SHORT).show();
        }*/
    }

    private void confirmarBorrado(Alarma recordatorio, RecordatorioNegocio neg) {

        new MaterialAlertDialogBuilder(requireContext(), R.style.Theme_Oscuro_Dialog)
                .setTitle("Eliminar recordatorio")
                .setMessage("¿Seguro que deseas eliminar "+ recordatorio.getTitulo()+"?")
                .setPositiveButton("Eliminar", (dialog, which) -> borrarRecordatorio(recordatorio,neg))
                .setNegativeButton("Cancelar", null)
                .show();
    }

    public void editarRecordatorio(Alarma r)
    {
        ModificacionRecordatorio dialog = new ModificacionRecordatorio();
        dialog.setOnRecordatorioGuardadoListener(this);

        Bundle args = new Bundle();
        args.putInt("id", r.getId());
        args.putString("titulo", r.getTitulo());
        args.putString("descripcion", r.getDescripcion());
        args.putString("imagen", r.getImagenUrl());
//        args.putString("fecha",r.getFecha().toString());
        args.putInt("hora",r.getHora());
        args.putInt("minuto",r.getMinuto());
        args.putString("tono",r.getTono());
        args.putBoolean("domingo",r.isDomingo());
        args.putBoolean("lunes",r.isLunes());
        args.putBoolean("martes",r.isMartes());
        args.putBoolean("miercoles",r.isMiercoles());
        args.putBoolean("jueves",r.isJueves());
        args.putBoolean("viernes",r.isViernes());
        args.putBoolean("sabado",r.isSabado());
        dialog.setArguments(args);


        dialog.show(getChildFragmentManager(), "Editar Alarma");
    }
}


