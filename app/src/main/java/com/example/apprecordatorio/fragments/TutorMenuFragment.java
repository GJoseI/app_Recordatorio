package com.example.apprecordatorio.fragments;


import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.apprecordatorio.R;
import com.example.apprecordatorio.activities.MainActivity;
import com.example.apprecordatorio.dao.PacienteExternoDao;
import com.example.apprecordatorio.dao.TutorExternoDao;

import com.example.apprecordatorio.dialogs.AltaNotaExterno;
import com.example.apprecordatorio.dialogs.AltaRecordatorioExterno;
import com.example.apprecordatorio.entidades.Paciente;
import com.example.apprecordatorio.entidades.Tutor;
import com.example.apprecordatorio.util.NetworkUtils;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import androidx.appcompat.app.AlertDialog;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TutorMenuFragment extends Fragment {

    Button tvSiguiendo;

    int codSeguimiento = 0;
    Button vincular;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tutor_menu, container, false);

        vincular = view.findViewById(R.id.btnVincular);
        tvSiguiendo = view.findViewById(R.id.tvSiguiendo);
        Bundle conIdPaciente = getArguments();
        if(conIdPaciente!=null)
        {
            codSeguimiento = conIdPaciente.getInt("codSeguimiento",0);
           String nombrePaciente = conIdPaciente.getString("nombrePaciente","default");

           if(codSeguimiento!=0)
           {
               Log.d("cod seguimiento"," "+codSeguimiento+" nombre"+nombrePaciente);
               vincular.setVisibility(View.GONE);
               tvSiguiendo.setText("Siguiendo a "+nombrePaciente);
               tvSiguiendo.setVisibility(View.VISIBLE);
           }
        }

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view,savedInstanceState);


        Bundle args = getArguments();

        Button seguimiento = view.findViewById(R.id.btnSeguimiento);
        Button atras = view.findViewById(R.id.btnAtras);
        Button agregar = view.findViewById(R.id.btnAgregarRec);
        Button verAlarmas = view.findViewById(R.id.btnVerAlarmas);
        Button verNotas = view.findViewById(R.id.btnVerNotas);


        TutorExternoDao tutorExternoDao = new TutorExternoDao();


        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler mainHandler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {

            PacienteExternoDao pacienteExternoDao = new PacienteExternoDao();
            if (args.getInt("codSeguimiento") > 0) {

                Paciente paciente = pacienteExternoDao.readOne(args.getInt("codSeguimiento"));
                mainHandler.post(()-> {

                    tvSiguiendo.setText("Siguiendo a: " + paciente.getNombre());
                    vincular.setVisibility(View.GONE);
                });

            }
        });
        executor.shutdown();






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

        tvSiguiendo.setOnClickListener(v -> {

            new MaterialAlertDialogBuilder(requireContext(), R.style.Theme_Oscuro_Dialog)
                    .setTitle("Desvincular")
                    .setMessage("¿Seguro que desea desvincularse?")
                    .setPositiveButton("Aceptar", (dialog, which) -> {

                        if (checkearTodoBien()) {

                            ExecutorService executor2 = Executors.newSingleThreadExecutor();
                            Handler mainHandler2 = new Handler(Looper.getMainLooper());

                            executor2.execute(() -> {
                                boolean ok = tutorExternoDao.desvincular(args.getInt("id"));

                                mainHandler2.post(() -> {
                                    if (ok) {
                                        Toast.makeText(requireContext(), "Desvinculado con éxito", Toast.LENGTH_SHORT).show();

                                        // Recargar fragment
                                        Fragment fragment = new TutorMenuFragment();

                                        Bundle argsb = new Bundle();
                                        argsb.putString("user", args.getString("user"));
                                        argsb.putString("pass", args.getString("pass"));
                                        argsb.putInt("id", args.getInt("id"));
                                        argsb.putString("email", args.getString("email"));
                                        argsb.putInt("codSeguimiento", 0); // sin paciente
                                        fragment.setArguments(argsb);

                                        ((MainActivity) requireActivity()).mostrarFragmento(fragment);

                                    } else {
                                        Toast.makeText(requireContext(), "Error al desvincular", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            });

                            executor2.shutdown();
                        }

                    })
                    .setNegativeButton("Cancelar", (dialog, which) -> {
                        dialog.dismiss();
                    })
                    .show();
        });

        seguimiento.setOnClickListener(v -> {
            if(checkearTodoBien())
            {
                Bundle argss = new Bundle();
                argss.putInt("idPaciente",codSeguimiento);
                Fragment fragmento = new SeguimientoFragment();
                fragmento.setArguments(argss);
                ((MainActivity) requireActivity()).mostrarFragmento(fragmento);
            }
        });


        atras.setOnClickListener(v -> {
            ((MainActivity) requireActivity()).esconderFragmento();
        });

        agregar.setOnClickListener(v -> {
            if(checkearTodoBien())
            {
                View viewDialog = getLayoutInflater().inflate(R.layout.dialog_tipo_recordatorio, null);

                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(requireContext(), R.style.Theme_Oscuro_Dialog)
                        .setTitle("Selecciona un tipo de recordatorio")
                        .setView(viewDialog)
                        .setNegativeButton("Cancelar", null);

                AlertDialog dialog = builder.create();

                dialog.show();

                viewDialog.findViewById(R.id.btnAlarma).setOnClickListener(v1 ->
                {
                    crearAlarma();
                    dialog.dismiss();
                });
                viewDialog.findViewById(R.id.btnNota).setOnClickListener(v1 -> {
                    crearNota();
                    dialog.dismiss();
                });
            }
        });

        verAlarmas.setOnClickListener(v->{
            if(checkearTodoBien())
            {
                Bundle argss = new Bundle();
                argss.putInt("idPaciente",codSeguimiento);
                Fragment fragmento = new AlarmaTutorFragment();
                fragmento.setArguments(argss);
                if (isAdded() && getActivity() != null && !requireActivity().isFinishing()) {
                    ((MainActivity) requireActivity()).mostrarFragmento(fragmento);
                }
            }
        });

        verNotas.setOnClickListener(v->{
            if(checkearTodoBien())
            {
                Bundle argss = new Bundle();
                argss.putInt("idPaciente",codSeguimiento);
                Fragment fragmento = new NotaTutorFragment();
                fragmento.setArguments(argss);
                if (isAdded() && getActivity() != null && !requireActivity().isFinishing()) {
                    ((MainActivity) requireActivity()).mostrarFragmento(fragmento);
                }
            }
        });

    }

    public boolean checkearTodoBien()
    {
        if(!NetworkUtils.hayConexion(requireContext()))
        {
            Toast.makeText(requireContext(),"Revise su conexion a internet",Toast.LENGTH_SHORT).show();
            return false;
        }else {
            if(codSeguimiento<=0)
            {
                Toast.makeText(requireContext(),"No esta vinculado",Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

    public void crearAlarma()
    {
        AltaRecordatorioExterno dialog = new AltaRecordatorioExterno();

        Bundle args = new Bundle();
        args.putInt("codSeguimiento",codSeguimiento);
        //dialog.setOnRecordatorioGuardadoListener(this);
        // currentDialog = dialog; // guarda una referencia
        dialog.show(getChildFragmentManager(), "Crear Alarma");
        dialog.setArguments(args);
    }
    public void crearNota()
    {
        AltaNotaExterno dialog = new AltaNotaExterno();
        Bundle args = new Bundle();
        args.putInt("codSeguimiento",codSeguimiento);

        dialog.show(getChildFragmentManager(), "Crear Nota");
        dialog.setArguments(args);
    }
}
