package com.example.apprecordatorio.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.apprecordatorio.R;
import com.example.apprecordatorio.entidades.Alarma;
import com.example.apprecordatorio.interfaces.OnRecordatorioGuardadoListener;
import com.example.apprecordatorio.negocio.RecordatorioGralNegocio;
import com.example.apprecordatorio.negocio.RecordatorioNegocio;
import com.example.apprecordatorio.util.FileUtil;

import java.time.LocalDate;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AltaRecordatorio extends DialogFragment {

    private Uri imagenSeleccionadaUri;
    private ImageView imgPreview;

    private OnRecordatorioGuardadoListener listener;

    private boolean imagenGuardada = false;

    private final ActivityResultLauncher<PickVisualMediaRequest> pickImageLauncher =
            registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                Log.e("URI:",uri.toString());

                try {
                    FileUtil fu = new FileUtil();
                    ExecutorService executor = Executors.newSingleThreadExecutor();
                    Handler handler = new Handler(Looper.getMainLooper());

                    executor.execute(()->{
                        Uri localUri = fu.copiarImagenLocal(uri,requireContext());

                        handler.post(()->{
                            setImagenSeleccionada(localUri);

                        });
                        executor.close();
                    });



                } catch (Exception e) {
                    e.printStackTrace();
                }


            });

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder( requireContext() );
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_alta_recordatorio, null);

        EditText editTitulo = view.findViewById(R.id.etTitulo);
        EditText editContenido = view.findViewById(R.id.etDesc);
        Button btnCancelar = view.findViewById(R.id.btnCancelar);
        Button btnGuardar = view.findViewById(R.id.btnGuardar);
        Button btnImg = view.findViewById(R.id.btnSeleccionarImagenAlarma);
        imgPreview = view.findViewById(R.id.ivDialog);

        builder.setView(view);
        AlertDialog dialog = builder.create();

        btnImg.setOnClickListener(v -> agregarImg());

        btnCancelar.setOnClickListener(v -> dialog.dismiss());
        btnGuardar.setOnClickListener(v -> {
            String titulo = editTitulo.getText().toString().trim();
            String contenido = editContenido.getText().toString().trim();
            if (!titulo.isEmpty()) {
                RecordatorioNegocio neg = new RecordatorioNegocio(getContext());
                Alarma r = new Alarma();
                r.setTitulo(titulo);
                r.setDescripcion(contenido);
                r.setFecha(LocalDate.now());
                r.setHora("11");
                if (imagenSeleccionadaUri != null) {
                    r.setImagenUrl(imagenSeleccionadaUri.toString());
                    Log.e("URI EN R:","ruta: "+r.getImagenUrl());
                }


                if(neg.add(r)>0)
                {
                    imagenGuardada = true;
                    Toast.makeText(requireContext(),"Creado con exito!",Toast.LENGTH_SHORT).show();
                    if (listener != null) listener.onRecordatorioGuardado();
                }
                dialog.dismiss();
            } else {
                editTitulo.setError("Ingrese un título");
            }
        });

        return dialog;
    }


    public void setOnRecordatorioGuardadoListener(OnRecordatorioGuardadoListener listener) {
        this.listener = listener;
    }

    public void agregarImg()
    {
        pickImageLauncher.launch(
                new PickVisualMediaRequest.Builder()
                        .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                        .build()
        );

    }


    public void setImagenSeleccionada(Uri uri) {

        FileUtil fu = new FileUtil();
        if(imagenSeleccionadaUri!=null)
        {
            fu.borrarImagenAnterior(imagenSeleccionadaUri.toString());
        }

        imagenSeleccionadaUri = uri;
        Log.e("URI en SET:",imagenSeleccionadaUri.toString());
        imgPreview.setVisibility(View.VISIBLE);
        imgPreview.setImageURI(uri);
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        FileUtil fu = new FileUtil();
        if(imagenSeleccionadaUri!=null && !imagenGuardada)
        {
            fu.borrarImagenAnterior(imagenSeleccionadaUri.toString());
        }

        Log.d("EN DISMIS","SE EJECUTO");

    }
/*
    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null && getDialog().getWindow() != null) {
            // Ocupa casi toda la pantalla (por ejemplo, 90% del ancho y 85% del alto)
            int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.98);
            int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.9);
            getDialog().getWindow().setLayout(width, height);
        }
    }*/
/*
    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
            );
            getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }
    }
*/

}
