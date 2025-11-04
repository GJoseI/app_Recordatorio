package com.example.apprecordatorio.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.apprecordatorio.R;
import com.example.apprecordatorio.dao.RecordatorioGralDao;
import com.example.apprecordatorio.entidades.Recordatorio;
import com.example.apprecordatorio.interfaces.OnRecordatorioGuardadoListener;
import com.example.apprecordatorio.negocio.RecordatorioGralNegocio;

import java.util.List;
import java.util.zip.Inflater;

public class AltaRecordatorioGeneral extends DialogFragment {

    private Uri imagenSeleccionadaUri;
    private ImageView imgPreview;

    private OnRecordatorioGuardadoListener listener;

    private final ActivityResultLauncher<PickVisualMediaRequest> pickImageLauncher =
            registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                Log.e("URI:",uri.toString());

                    // Guardar permiso persistente
                    final int takeFlags = Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION;
                    try {
                        requireContext().getContentResolver().takePersistableUriPermission(uri, takeFlags);
                    } catch (SecurityException e) {
                        e.printStackTrace();
                    }
                        setImagenSeleccionada(uri);


            });

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder( requireContext() );
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_alta_recordatorio_general, null);

        EditText editTitulo = view.findViewById(R.id.etTituloDialogRg);
        EditText editContenido = view.findViewById(R.id.etContenidoDialogRg);
        Button btnCancelar = view.findViewById(R.id.btnCancelarDialogRg);
        Button btnGuardar = view.findViewById(R.id.btnGuardarDialogRg);
        Button btnImg = view.findViewById(R.id.btnImagenDialogRg);
        imgPreview = view.findViewById(R.id.ivDialogRg);

        builder.setView(view);
        AlertDialog dialog = builder.create();

        btnImg.setOnClickListener(v -> agregarImg());

        btnCancelar.setOnClickListener(v -> dialog.dismiss());
        btnGuardar.setOnClickListener(v -> {
            String titulo = editTitulo.getText().toString().trim();
            String contenido = editContenido.getText().toString().trim();
            if (!titulo.isEmpty()) {
                RecordatorioGralNegocio neg = new RecordatorioGralNegocio(getContext());
                Recordatorio r = new Recordatorio();
                r.setTitulo(titulo);
                r.setDescripcion(contenido);
                if (imagenSeleccionadaUri != null) {
                    r.setImagenUrl(imagenSeleccionadaUri.toString());
                    Log.e("URI EN R:","ruta: "+r.getImagenUrl());
                }


                if(neg.add(r)>0)
                {
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
        imagenSeleccionadaUri = uri;
        Log.e("URI en SET:",imagenSeleccionadaUri.toString());
        imgPreview.setVisibility(View.VISIBLE);
        imgPreview.setImageURI(uri);
    }


}
