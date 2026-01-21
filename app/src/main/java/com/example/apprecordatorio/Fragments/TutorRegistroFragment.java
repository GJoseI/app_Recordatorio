package com.example.apprecordatorio.Fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.apprecordatorio.R;
import com.example.apprecordatorio.Activities.MainActivity;
import com.example.apprecordatorio.DAO.TutorExternoDao;
import com.example.apprecordatorio.Entidades.Tutor;
import com.example.apprecordatorio.Util.NetworkUtils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TutorRegistroFragment extends Fragment {
    EditText etEmail, etUser, etPass, etPass2;
    Button btnRegistrase;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tutor_registro, container, false);
        etEmail = view.findViewById(R.id.et_mailTRegistro);
        etUser = view.findViewById(R.id.et_userTRegistro);
        etPass = view.findViewById(R.id.et_passTRegistro);
        etPass2 = view.findViewById(R.id.et_passTRegistro2);
        btnRegistrase = view.findViewById(R.id.btnRegistrarse);

        btnRegistrase.setOnClickListener(v -> {
            String pass = etPass.getText().toString().trim();
            String pass2 = etPass2.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String user = etUser.getText().toString().trim();

            if(!NetworkUtils.hayConexion(requireContext()))
            {
                Toast.makeText(requireContext(), "Revise su conexion a internet", Toast.LENGTH_SHORT).show();
            }
            else {

                if (email.isEmpty() || pass.isEmpty() || pass2.isEmpty() || user.isEmpty()) {
                    Toast.makeText(requireContext(), "Complete todos los campos", Toast.LENGTH_SHORT).show();
                }else
                {
                    TutorExternoDao tutorExternoDao = new TutorExternoDao();
                    Tutor tutor = new Tutor();
                    String error = validarRegistro(email, pass, pass2);
                    if (error != null) {
                        Toast.makeText(this.getContext(), error, Toast.LENGTH_SHORT).show();
                    } else {
                        ExecutorService executor = Executors.newSingleThreadExecutor();
                        Handler mainHandler = new Handler(Looper.getMainLooper());

                        executor.execute(() -> {
                            tutor.setEmail(etEmail.getText().toString());
                            tutor.setUsername(etUser.getText().toString());
                            tutor.setPassword(etPass.getText().toString());
                            Boolean agregado = tutorExternoDao.add(tutor);
                            mainHandler.post(() ->{
                                if(agregado){
                                    Toast.makeText(this.getContext(), "Registro correcto", Toast.LENGTH_SHORT).show();
                                    ((MainActivity) requireActivity()).esconderFragmento();
                                }else {
                                    Toast.makeText(this.getContext(), "Error al registrar", Toast.LENGTH_SHORT).show();
                                }
                            });
                        });
                    }
                }
            }


        });
        return view;
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    public String validarRegistro(String email, String pass, String pass2){

        if(!validarEmail(email)) return "Email invalido";

        if (!pass.equals(pass2)) return "Las contraseñas no coinciden";

       // if (tutorExternoDao.login(null, null, email) != null) return "Ya existe un tutor con este email";

        return null;
    }

    public static boolean validarEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
}