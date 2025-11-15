package com.example.apprecordatorio.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.apprecordatorio.R;
import com.example.apprecordatorio.activities.MainActivity;
import com.example.apprecordatorio.dao.SeguimientoExternoDao;
import com.example.apprecordatorio.entidades.Seguimiento;
import com.example.apprecordatorio.negocio.RecordatorioNegocio;
import com.example.apprecordatorio.negocio.SeguimientoNegocio;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SeguimientoFragment extends Fragment {

    private ArrayList<Seguimiento> lista = null;
    private LinearLayout container = null;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_seguimiento, container, false);
    }
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view,savedInstanceState);

        container = view.findViewById(R.id.containerRecordatoriosSeguimiento);

        LayoutInflater inflater = LayoutInflater.from(this.getContext());

        Button atras = view.findViewById(R.id.btnAtras3);
        atras.setOnClickListener(v -> {
            ((MainActivity) requireActivity()).esconderFragmento();
        });
        /*
        // 3️⃣ Inflar las cards y agregarlas al contenedor
        // Supongamos que tu layout de card es "item_card_recordatorio.xml"
        for (int i = 0; i < 3; i++) {
            View cardView = inflater.inflate(R.layout.item_seguimiento, container, false);

            // Si tu card tiene textos o botones, los seteás acá:
            TextView titulo = cardView.findViewById(R.id.txtTituloRecSeg);
            titulo.setText("Recordatorio " + (i + 1));

            container.addView(cardView);

        }*/
        mostrarSeguimientos();
    }


    public void mostrarSeguimientos( )
    {
        Bundle args = getArguments();
        if(args!=null)
        {
            int id = args.getInt("idPaciente");

            ExecutorService executor = Executors.newSingleThreadExecutor();
            Handler mainHandler = new Handler(Looper.getMainLooper());
            LayoutInflater inflater = LayoutInflater.from(this.getContext());

            executor.execute(() -> {

                SeguimientoNegocio neg = new SeguimientoNegocio();
                RecordatorioNegocio rneg = new RecordatorioNegocio(requireContext());
                lista = neg.readAllFromPaciente(id);

                for(Seguimiento a : lista)
                {
                    a.setAlarma(rneg.readOneFrom(a.getAlarma().getId(),a.getAlarma().getPacienteId()));
                }

                mainHandler.post(() -> {

                    for(Seguimiento s : lista)
                    {
                        View cardView = inflater.inflate(R.layout.item_seguimiento, container, false);
                        TextView titulo = cardView.findViewById(R.id.txtTituloRecSeg);
                        TextView fecha = cardView.findViewById(R.id.tvFechaSeguimiento);
                        TextView estado = cardView.findViewById(R.id.txtEstadoRecSeg);
                        fecha.setText(s.getTimestamp());
                        if(s.isAtendida())
                        {
                            estado.setText("Atendida");
                        }else {
                            estado.setText("Perdida");
                        }
                        titulo.setText(s.getAlarma().getTitulo());

                        container.addView(cardView);
                    }

                });
            });


        }
    }


}