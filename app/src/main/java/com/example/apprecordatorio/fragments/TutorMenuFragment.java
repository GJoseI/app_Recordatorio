package com.example.apprecordatorio.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.apprecordatorio.R;
import com.example.apprecordatorio.activities.MainActivity;

public class TutorMenuFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tutor_menu, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view,savedInstanceState);

        Button vincular = view.findViewById(R.id.btnVincular);
        Button seguimiento = view.findViewById(R.id.btnSeguimiento);
        Button atras = view.findViewById(R.id.btnAtras);
        Button agregar = view.findViewById(R.id.btnAgregarRec);
        Button verAlarmas = view.findViewById(R.id.btnVerAlarmas);

        verAlarmas.setOnClickListener(v -> {

        });

        vincular.setOnClickListener(v ->{
            Fragment fragmento = new VincularFragment();
            ((MainActivity) requireActivity()).mostrarFragmento(fragmento);
        });

        seguimiento.setOnClickListener(v -> {
            Bundle args = new Bundle();
            args.putInt("idPaciente",14);
            Fragment fragmento = new SeguimientoFragment();
            fragmento.setArguments(args);
            ((MainActivity) requireActivity()).mostrarFragmento(fragmento);
        });

        atras.setOnClickListener(v -> {
            ((MainActivity) requireActivity()).esconderFragmento();
        });
        /*
        agregar.setOnClickListener(v -> {
            Fragment fragmento = new AgregarRecTutorFragment();
            ((MainActivity) requireActivity()).mostrarFragmento(fragmento);
        });
         */
    }
}
