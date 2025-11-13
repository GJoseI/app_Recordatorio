package com.example.apprecordatorio.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.apprecordatorio.R;
import com.example.apprecordatorio.activities.MainActivity;

public class SeguimientoFragment extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_seguimiento, container, false);
    }
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view,savedInstanceState);

        LinearLayout container = view.findViewById(R.id.containerRecordatoriosSeguimiento);

        LayoutInflater inflater = LayoutInflater.from(this.getContext());

        Button atras = view.findViewById(R.id.btnAtras3);
        atras.setOnClickListener(v -> {
            ((MainActivity) requireActivity()).esconderFragmento();
        });

        // 3️⃣ Inflar las cards y agregarlas al contenedor
        // Supongamos que tu layout de card es "item_card_recordatorio.xml"
        for (int i = 0; i < 3; i++) {
            View cardView = inflater.inflate(R.layout.item_seguimiento, container, false);

            // Si tu card tiene textos o botones, los seteás acá:
            TextView titulo = cardView.findViewById(R.id.txtTituloRecSeg);
            titulo.setText("Recordatorio " + (i + 1));

            container.addView(cardView);

        }
    }
}