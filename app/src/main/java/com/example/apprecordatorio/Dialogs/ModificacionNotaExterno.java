package com.example.apprecordatorio.Dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
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

import com.bumptech.glide.Glide;
import com.example.apprecordatorio.R;
import com.example.apprecordatorio.Entidades.Recordatorio;
import com.example.apprecordatorio.DAOInterfaces.OnRecordatorioGuardadoListener;
import com.example.apprecordatorio.Negocio.RecordatorioGralNegocio;
import com.example.apprecordatorio.Util.FileUtil;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ModificacionNotaExterno extends DialogFragment {


    private ImageView imgPreview;

    private OnRecordatorioGuardadoListener listener;

    private static final String BASE_URL = "http://10.0.2.2/pruebaphp";
    //private final String BASE_URL = "http://marvelous-vision-production-c97b.up.railway.app/";
    private String imagenSeleccionadaBase64;

    private final ActivityResultLauncher<PickVisualMediaRequest> pickImageLauncher =
            registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                Log.e("URI:",uri.toString());
                try {
                    FileUtil fu = new FileUtil();
                    ExecutorService executor = Executors.newSingleThreadExecutor();
                    Handler handler = new Handler(Looper.getMainLooper());

                    executor.execute(()->{
                        try {
                            imagenSeleccionadaBase64 = fu.uriToBase64(requireContext(),uri);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                        handler.post(()->{
                            setImagenSeleccionada(uri);

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
        View view = inflater.inflate(R.layout.dialog_alta_recordatorio_general, null);

        EditText editTitulo = view.findViewById(R.id.etTituloDialogRg);
        EditText editContenido = view.findViewById(R.id.etContenidoDialogRg);
        Button btnCancelar = view.findViewById(R.id.btnCancelarDialogRg);
        Button btnGuardar = view.findViewById(R.id.btnGuardarDialogRg);
        Button btnImg = view.findViewById(R.id.btnImagenDialogRg);
        imgPreview = view.findViewById(R.id.ivDialogRg);
        String viejaImagen;

        builder.setView(view);
        AlertDialog dialog = builder.create();


        ///  cargar los datos del recordatorio en la card
        Bundle args = getArguments();
        if (args != null) {
            editTitulo.setText(args.getString("titulo"));
            editContenido.setText(args.getString("descripcion"));

            viejaImagen = args.getString("imagen");
            if (viejaImagen != null && !viejaImagen.isEmpty()) {
                Log.e("EN IF",viejaImagen);
                Glide.with(this)
                        .load(BASE_URL + "/" + viejaImagen)
                        .into(imgPreview);

                imgPreview.setVisibility(View.VISIBLE);
            }

        } else {
            viejaImagen = null;
        }


        btnImg.setOnClickListener(v -> agregarImg());

        btnCancelar.setOnClickListener(v -> {

            dialog.dismiss();
        });

        btnGuardar.setOnClickListener(v -> {

            String titulo = editTitulo.getText().toString().trim();
            String contenido = editContenido.getText().toString().trim();

            if (!titulo.isEmpty())
            {
                RecordatorioGralNegocio neg = new RecordatorioGralNegocio(getContext());
                Recordatorio r = new Recordatorio();

                if(args!=null)
                {
                    r.setId(args.getInt("id"));
                    r.setIdRemoto(args.getInt("idRemoto"));
                    r.setPacienteId(args.getInt("idPaciente"));
                }
                r.setTitulo(titulo);
                r.setDescripcion(contenido);


                if (imagenSeleccionadaBase64 != null) {
                    r.setImagenUrl(imagenSeleccionadaBase64);
                    Log.e("URI EN R:","ruta: "+r.getImagenUrl());
                }else {
                    r.setImagenUrl(viejaImagen);
                }


                ExecutorService executor = Executors.newSingleThreadExecutor();
                Handler mainHandler = new Handler(Looper.getMainLooper());
                executor.execute(() -> {

                    boolean insertado = neg.updateEx(r);

                    mainHandler.post(() -> {

                        if (insertado) {
                            Toast.makeText(requireContext(),"Actualizado con exito!",Toast.LENGTH_SHORT).show();
                            if (listener != null) listener.onRecordatorioGuardado();
                        } else {
                            Toast.makeText(requireContext(),"Error al modificar!",Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();
                    });
                });
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
        imgPreview.setVisibility(View.VISIBLE);
        imgPreview.setImageURI(uri);
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);

    }
}
