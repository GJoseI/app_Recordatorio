package com.example.apprecordatorio.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.apprecordatorio.R;
import com.example.apprecordatorio.activities.MainActivity;
import com.example.apprecordatorio.dao.PacienteExternoDao;
import com.example.apprecordatorio.dao.TutorExternoDao;
import com.example.apprecordatorio.entidades.Paciente;
import com.example.apprecordatorio.entidades.Tutor;

public class TutorMenuFragment extends Fragment {

    TextView tvSiguiendo;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tutor_menu, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view,savedInstanceState);


        Bundle args = getArguments();
        Button vincular = view.findViewById(R.id.btnVincular);
        Button seguimiento = view.findViewById(R.id.btnSeguimiento);
        Button atras = view.findViewById(R.id.btnAtras);
        Button agregar = view.findViewById(R.id.btnAgregarRec);
        tvSiguiendo = view.findViewById(R.id.tvSiguiendo);


        TutorExternoDao tutorExternoDao = new TutorExternoDao();
        Tutor tutor = tutorExternoDao.obtenerTutor(args.getString("user"), args.getString("pass"), null);
        if(tutor!=null) {
            PacienteExternoDao pacienteExternoDao = new PacienteExternoDao();
            if (tutor.getP().getId() > 0) {
                Paciente paciente = pacienteExternoDao.readOne(tutor.getP().getId());
                tvSiguiendo.setText("Siguiendo a: " + paciente.getNombre());
                vincular.setVisibility(View.GONE);
            }
        }else{
            Log.d("debug tutor", "ta vacio");
        }

        vincular.setOnClickListener(v ->{
            Bundle args2 = new Bundle();
            if(args != null){
                args2.putString("user",args.getString("user"));
                args2.putString("pass",args.getString("pass"));
                args2.putInt("id", args.getInt("id"));
                args2.putString("email", args.getString("email"));
            }
            Fragment fragmento = new VincularFragment();
            fragmento.setArguments(args2);
            ((MainActivity) requireActivity()).mostrarFragmento(fragmento);
        });

        seguimiento.setOnClickListener(v -> {
            Fragment fragmento = new SeguimientoFragment();
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
