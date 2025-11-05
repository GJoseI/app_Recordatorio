package com.example.apprecordatorio.fragments;

import static androidx.core.content.ContextCompat.getSystemService;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apprecordatorio.R;
import com.example.apprecordatorio.Adapters.RecordatorioAdapter;
import com.example.apprecordatorio.Receivers.AlarmReceiver;
import com.example.apprecordatorio.activities.CrearAlarmaActivity;
import com.example.apprecordatorio.entidades.Recordatorio;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;


public class AlarmasFragment extends Fragment {

    private RecyclerView recyclerView;
    private FloatingActionButton fabAgregar;
    private RecordatorioAdapter adapter;
    private ImageButton btnEditar;
    private Switch sEstado;
    private List<Recordatorio> listaRecordatorios;
    private TextView tvHora, tvMinuto, tvDias;
    private AlarmManager alarmManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_alarmas, container, false);


        ///  cargando ejemplo de card
        LinearLayout containerRec = view.findViewById(R.id.containerRecordatoriosAlarmas);

       View card =  inflater.inflate(R.layout.item_recordatorio,containerRec,false);
        containerRec.addView(card);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
        recyclerView = view.findViewById(R.id.recyclerRecordatorios);
        fabAgregar = view.findViewById(R.id.fabAgregar);
        btnEditar = view.findViewById(R.id.btnEditar);
        sEstado = view.findViewById(R.id.sEstado);
        tvHora = view.findViewById(R.id.tvHora);
        tvMinuto = view.findViewById(R.id.tvMinutos);
        tvDias = view.findViewById(R.id.tvDias);

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

        ///  ACA CARGO UNA CARD PARA EL EJEMPLO DE LAS ALARMAS




    }

    //  funcion aux para actualizar la lista
    private void actualizarLista(List<Recordatorio> nuevos) {
        listaRecordatorios.clear();
        listaRecordatorios.addAll(nuevos);
        adapter.notifyDataSetChanged();
    }

    private void alarmaSwitch(View view){
        if (!(view instanceof Switch)) return;

        Switch sw = (Switch) view;
        if(sw.isChecked()){
            Toast.makeText(this.getContext(), "ALARMA ENCENDIDA", Toast.LENGTH_SHORT).show();
            crearAlarmas(obtenerDias(), Integer.parseInt(tvHora.getText().toString()), Integer.parseInt(tvMinuto.getText().toString()));

        }
        else {
            Toast.makeText(this.getContext(), "ALARMA APAGADA", Toast.LENGTH_SHORT).show();
            cancelarAlarma((int) view.getTag());
        }
    }

    private void crearAlarmas(List<Integer> diasObtenidos, int hora, int minutos){
        if(diasObtenidos.size() == 7){
            Calendar calendar = Calendar.getInstance();

            calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(tvHora.getText().toString()));
            calendar.set(Calendar.MINUTE, Integer.parseInt(tvMinuto.getText().toString()));
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);

            long time = calendar.getTimeInMillis();

            if (System.currentTimeMillis() > time) {
                time += 24*60*60*1000;
            }

            Intent intent = new Intent(AlarmasFragment.this.getActivity(), AlarmReceiver.class);
            intent.putExtra("modo", "diario");

            PendingIntent pi = PendingIntent.getBroadcast(this.getContext(), 0, intent, PendingIntent.FLAG_IMMUTABLE);

            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, time, AlarmManager.INTERVAL_DAY, pi);
        } else {
            for (int dia : diasObtenidos){
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.DAY_OF_WEEK, dia);
                calendar.set(Calendar.HOUR_OF_DAY, hora);
                calendar.set(Calendar.MINUTE, minutos);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);

                long time = calendar.getTimeInMillis();
                if(System.currentTimeMillis() > time){
                    calendar.add(Calendar.WEEK_OF_YEAR,1);
                }

                Intent intent = new Intent(AlarmasFragment.this.getActivity(), AlarmReceiver.class);
                intent.putExtra("modo", "semanal");
                intent.putExtra("dia", dia);

                PendingIntent pi = PendingIntent.getBroadcast(this.getContext(), dia, intent, PendingIntent.FLAG_IMMUTABLE);

                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, time, pi);
            }
        }
    }

    public void cancelarAlarma(int aTag){
        Intent intent = new Intent(AlarmasFragment.this.getActivity(), AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this.getContext(), aTag, intent, PendingIntent.FLAG_IMMUTABLE);

        alarmManager.cancel(pi);
    }

    public List<Integer> obtenerDias(){
        List<Integer> diasSeleccionados = new ArrayList<>();
        String txt = tvDias.getText().toString().trim();

        String[] dias = txt.replace(" ", "").split(",");

        for (String dia : dias){
            switch (dia){
                case "D": diasSeleccionados.add(Calendar.SUNDAY);
                    break;
                case "L": diasSeleccionados.add(Calendar.MONDAY);
                    break;
                case "M": diasSeleccionados.add(Calendar.TUESDAY);
                    break;
                case "X": diasSeleccionados.add(Calendar.WEDNESDAY);
                    break;
                case "J": diasSeleccionados.add(Calendar.THURSDAY);
                    break;
                case "V": diasSeleccionados.add(Calendar.FRIDAY);
                    break;
                case "S": diasSeleccionados.add(Calendar.SATURDAY);
                    break;
            }
        }
        return diasSeleccionados;
    }
}