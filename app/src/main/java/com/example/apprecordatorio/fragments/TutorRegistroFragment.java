package com.example.apprecordatorio.fragments;

import android.os.Bundle;
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
import com.example.apprecordatorio.dao.TutorExternoDao;
import com.example.apprecordatorio.entidades.Tutor;

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

        return view;
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnRegistrase.setOnClickListener(v -> {
            String pass = etPass.getText().toString();
            String pass2 = etPass2.getText().toString();
            String email = etEmail.getText().toString();
            TutorExternoDao tutorExternoDao = new TutorExternoDao();
            Tutor tutor = new Tutor();

            String error = validarRegistro(email, pass, pass2, tutorExternoDao);
            if(error != null){
                Toast.makeText(this.getContext(), error, Toast.LENGTH_SHORT);
            }else{
            tutor.setEmail(etEmail.getText().toString());
            tutor.setUsername(etUser.getText().toString());
            tutor.setPassword(etPass.getText().toString());
            tutorExternoDao.add(tutor);
            }
        });
    }
    public static boolean validarEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    public String validarRegistro(String email, String pass, String pass2, TutorExternoDao tutorExternoDao){
        if(!validarEmail(etEmail.getText())) return "Email invalido";

        if (!pass.equals(pass2)) return "Las contraseñas no coinciden";

        if (tutorExternoDao.obtenerTutor(null, null, email) != null) return "Ya existe un tutor con este email";

        return null;
    }
}