package com.example.apprecordatorio.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apprecordatorio.R;
import com.example.apprecordatorio.activities.MainActivity;
import com.example.apprecordatorio.dao.TutorExternoDao;
import com.example.apprecordatorio.entidades.Paciente;
import com.example.apprecordatorio.negocio.PacienteNegocio;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TutorFragment extends Fragment {


    Button btnCod;
    TextView tvCod;
    EditText etUser;
    EditText etPass;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tutor, container, false);

        btnCod = view.findViewById(R.id.btnGenerarCodigo);
        tvCod = view.findViewById(R.id.tvCodSeguimiento);
        etPass = view.findViewById(R.id.et_passTinicio);
        etUser = view.findViewById(R.id.et_userTInicio);

        PacienteNegocio negS = new PacienteNegocio(requireContext());
        Paciente pS = negS.read();
        if(pS!=null)
        {
            btnCod.setVisibility(View.GONE);
            tvCod.setVisibility(View.VISIBLE);
            tvCod.setText(String.valueOf(pS.getId()));
        }


        btnCod.setOnClickListener(v -> {

            final EditText input = new EditText(requireContext());
            input.setHint("Nombre");

            new MaterialAlertDialogBuilder(requireContext(), R.style.Theme_Oscuro_Dialog)
                    .setTitle("Ingresa tu nombre")
                    .setMessage("Nombre de la persona a seguir:")
                    .setView(input)
                    .setPositiveButton("Aceptar", (dialog, which) -> {
                        String nombre = input.getText().toString().trim();

                        if (!nombre.isEmpty()) {

                            ExecutorService executor = Executors.newSingleThreadExecutor();
                            Handler mainHandler = new Handler(Looper.getMainLooper());

                            executor.execute(() -> {

                                PacienteNegocio neg = new PacienteNegocio(requireContext());
                                Paciente p = new Paciente();
                                p.setNombre(nombre);

                                long insertado = neg.add(p);
                                Paciente pLeido = null;

                                if (insertado>0) {
                                    pLeido = neg.read();
                                    neg.ponerIdPacienteEnAlarmas(requireContext());
                                }

                                Paciente finalPL = pLeido;
                                mainHandler.post(() -> {

                                    if (insertado>0) {
                                        btnCod.setVisibility(View.GONE);
                                        tvCod.setText(String.valueOf(finalPL.getId()));
                                        tvCod.setVisibility(View.VISIBLE);
                                        Toast.makeText(requireContext(),
                                                "Código generado!",
                                                Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(requireContext(),
                                                "Error al generar el código.",
                                                Toast.LENGTH_SHORT).show();
                                    }

                                });
                            });

                        } else {
                            Toast.makeText(requireContext(),
                                    "Ingrese su nombre!",
                                    Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Cancelar", null)
                    .show();
        });

        return view;
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button registrarse = view.findViewById(R.id.btnRegistrarse);
        Button iniciar = view.findViewById(R.id.btnIniciarSesion);

        registrarse.setOnClickListener(v -> {
            //Intent registro = new Intent(TutorFragment.this.getActivity(), TutorRegistroFragment.class);
            //startActivity(registro);

            Fragment fragment = new TutorRegistroFragment();
            ((MainActivity) requireActivity()).mostrarFragmento(fragment);
        });

        iniciar.setOnClickListener(v -> {
            /// Funcion para corroborar datos ingresados
            String user = etUser.getText().toString();
            String pass = etPass.getText().toString();
            TutorExternoDao tutorExternoDao = new TutorExternoDao();
            if(tutorExternoDao.obtenerTutor(user, pass, null) != null){
                Fragment fragment = new TutorMenuFragment();
                ((MainActivity) requireActivity()).mostrarFragmento(fragment);
            }else{
                Toast.makeText(this.getContext(),"Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
            }
        });
    }
}