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

import com.example.apprecordatorio.R;
import com.example.apprecordatorio.Entidades.Recordatorio;
import com.example.apprecordatorio.DAOInterfaces.OnRecordatorioGuardadoListener;
import com.example.apprecordatorio.Negocio.RecordatorioGralNegocio;
import com.example.apprecordatorio.Util.FileUtil;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ModificacionRecordatorioGeneral extends DialogFragment {


    private Uri imagenSeleccionadaUri;
    private ImageView imgPreview;

    private boolean imagenGuardada = false;
    private OnRecordatorioGuardadoListener listener;


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
                imgPreview.setImageURI(Uri.parse(viejaImagen));
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
                }
                r.setTitulo(titulo);
                r.setDescripcion(contenido);


                if (imagenSeleccionadaUri != null) {

                    if (viejaImagen != null && !viejaImagen.equals(imagenSeleccionadaUri.toString())) {
                        FileUtil fu = new FileUtil();
                        fu.borrarImagenAnterior(viejaImagen);
                    }
                    r.setImagenUrl(imagenSeleccionadaUri.toString());
                    Log.e("URI EN R:","ruta: "+r.getImagenUrl());
                }else {
                    r.setImagenUrl(viejaImagen);
                }


                ExecutorService executor = Executors.newSingleThreadExecutor();
                Handler mainHandler = new Handler(Looper.getMainLooper());
                executor.execute(() -> {

                    long insertado = neg.update(r);

                    mainHandler.post(() -> {

                        if (insertado>0) {
                            imagenGuardada = true;
                            Toast.makeText(requireContext(),"Actualizado con exito!",Toast.LENGTH_SHORT).show();
                            if (listener != null) listener.onRecordatorioGuardado();
                        } else {
                            Toast.makeText(requireContext(),"Error al modificar!",Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();
                    });
                });

                /*
                if(neg.update(r)>0)
                {
                    Toast.makeText(requireContext(),"Actualizado con exito!",Toast.LENGTH_SHORT).show();
                    if (listener != null) listener.onRecordatorioGuardado();
                }else {Log.e("UPDATE FALLO","NO SE MODIFICARON REGISTROS.");}

                dialog.dismiss();
                */
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

    }

}
