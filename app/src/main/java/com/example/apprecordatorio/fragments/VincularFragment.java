package com.example.apprecordatorio.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.apprecordatorio.R;
import com.example.apprecordatorio.activities.MainActivity;
import com.example.apprecordatorio.dao.PacienteExternoDao;
import com.example.apprecordatorio.dao.TutorExternoDao;
import com.example.apprecordatorio.entidades.Tutor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class VincularFragment extends Fragment {
    Button btnVincular, atras;
    EditText codSeguimiento;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_vincular, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view,savedInstanceState);

        atras = view.findViewById(R.id.btnAtras2);
        btnVincular = view.findViewById(R.id.btnVincular);
        codSeguimiento = view.findViewById(R.id.et_codSeguimiento);

        atras.setOnClickListener(v -> {
            ((MainActivity) requireActivity()).esconderFragmento();
        });

        btnVincular.setOnClickListener(v -> {
            Bundle args = getArguments();
            if(args != null){
                ExecutorService executor = Executors.newSingleThreadExecutor();
                Handler mainHandler = new Handler(Looper.getMainLooper());
                Tutor tutor = new Tutor();
                TutorExternoDao tutorExternoDao = new TutorExternoDao();
                PacienteExternoDao pacienteExternoDao = new PacienteExternoDao();
                executor.execute(() ->{

                    tutor.setUsername(args.getString("user"));
                    tutor.setPassword(args.getString("pass"));
                    tutor.setId(args.getInt("id"));
                    tutor.setEmail(args.getString("email"));
                    tutor.setP(pacienteExternoDao.readOne(Integer.parseInt(codSeguimiento.getText().toString())));
                    boolean update = tutorExternoDao.vincular(tutor);
                    mainHandler.post(()->{
                        if(update){
                            Toast.makeText(this.getContext(),"Vinculacion con exito",Toast.LENGTH_SHORT).show();

                            //((MainActivity) requireActivity()).esconderFragmento();

                            Bundle argsb = new Bundle();
                            argsb.putString("user", tutor.getUsername());
                            argsb.putString("pass",tutor.getPassword());
                            argsb.putInt("id",tutor.getId());
                            argsb.putString("email", tutor.getEmail());
                            argsb.putInt("codSeguimiento",tutor.getP().getId());
                            argsb.putString("nombrePaciente",tutor.getP().getNombre());
                            Fragment fragment = new TutorMenuFragment();
                            fragment.setArguments(argsb);
                            ((MainActivity) requireActivity()).mostrarFragmento(fragment);

                        }else{
                            Toast.makeText(this.getContext(),"Error al vincular",Toast.LENGTH_SHORT).show();
                        }
                    });
                });
            }
        });
    }
}