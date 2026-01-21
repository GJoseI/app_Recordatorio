package com.example.apprecordatorio.Dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.apprecordatorio.R;
import com.example.apprecordatorio.Entidades.Alarma;
import com.example.apprecordatorio.DAOInterfaces.OnRecordatorioGuardadoListener;
import com.example.apprecordatorio.Negocio.RecordatorioNegocio;
import com.example.apprecordatorio.Util.FileUtil;
import com.example.apprecordatorio.Util.NetworkUtils;

import java.io.IOException;
import java.time.LocalDate;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AltaRecordatorioExterno extends DialogFragment {


    private String imagenSeleccionadaBase64;
    private ImageView imgPreview;

    private OnRecordatorioGuardadoListener listener;


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
                            e.printStackTrace();
                            throw new RuntimeException(e);
                        }
                        ;

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
        View view = inflater.inflate(R.layout.dialog_alta_recordatorio, null);

        EditText editTitulo = view.findViewById(R.id.etTitulo);
        EditText editContenido = view.findViewById(R.id.etDesc);
        Button btnCancelar = view.findViewById(R.id.btnCancelar);
        Button btnGuardar = view.findViewById(R.id.btnGuardar);
        Button btnImg = view.findViewById(R.id.btnSeleccionarImagenAlarma);
        imgPreview = view.findViewById(R.id.ivDialog);
        TimePicker timePicker = view.findViewById(R.id.tpHora);

        ToggleButton domingo = view.findViewById(R.id.btDom);
        ToggleButton lunes = view.findViewById(R.id.btLu);
        ToggleButton martes = view.findViewById(R.id.btMar);
        ToggleButton miercoles = view.findViewById(R.id.btMie);
        ToggleButton jueves = view.findViewById(R.id.btJue);
        ToggleButton viernes = view.findViewById(R.id.btVie);
        ToggleButton sabado = view.findViewById(R.id.btSa);

        Spinner spTono = view.findViewById(R.id.spAlarmaTono);
        String[] nombresTonos = {"Terraria", "Age of Empires"};
        int[] recursosTonos = {R.raw.terrariasounds, R.raw.aoe};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item, nombresTonos);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTono.setAdapter(adapter);

        builder.setView(view);
        AlertDialog dialog = builder.create();

        btnImg.setOnClickListener(v -> agregarImg());

        spTono.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            MediaPlayer mediaPlayer;
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (mediaPlayer != null) {
                    mediaPlayer.release();
                }
                mediaPlayer = MediaPlayer.create(requireContext(), recursosTonos[position]);
                mediaPlayer.start();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        btnCancelar.setOnClickListener(v -> dialog.dismiss());
        btnGuardar.setOnClickListener(v -> {
            String titulo = editTitulo.getText().toString().trim();
            String contenido = editContenido.getText().toString().trim();
            if (!titulo.isEmpty()) {
                if(!domingo.isChecked()&&!lunes.isChecked()&&!martes.isChecked()&&!miercoles.isChecked()
                    &&!jueves.isChecked()&&!viernes.isChecked()&&!sabado.isChecked())
                {
                    Toast.makeText(requireContext(),"Seleccione un dia",Toast.LENGTH_SHORT).show();
                }
                else {
                    RecordatorioNegocio neg = new RecordatorioNegocio(getContext());
                    Alarma r = new Alarma();

                    Bundle args = getArguments();
                    if(args!=null){
                        r.setPacienteId(args.getInt("codSeguimiento"));
                    }

                    r.setTitulo(titulo);
                    r.setDescripcion(contenido);
                    r.setFecha(LocalDate.now());
                    r.setHora(timePicker.getHour());
                    r.setMinuto(timePicker.getMinute());
                    r.setEstado(true);

                    if(domingo.isChecked())r.setDomingo(true);
                    if(lunes.isChecked())r.setLunes(true);
                    if(martes.isChecked())r.setMartes(true);
                    if(miercoles.isChecked())r.setMiercoles(true);
                    if(jueves.isChecked())r.setJueves(true);
                    if(viernes.isChecked())r.setViernes(true);
                    if(sabado.isChecked())r.setSabado(true);

                    int seleccion = spTono.getSelectedItemPosition();
                    int recursoSeleccionado = recursosTonos[seleccion];

                    Uri tonoUri = Uri.parse("android.resource://" + requireContext().getPackageName() + "/"
                            + recursoSeleccionado);

                    r.setTono(tonoUri.toString());

                    if (imagenSeleccionadaBase64 != null) {
                        r.setImagenUrl(imagenSeleccionadaBase64);
                        Log.e("URI EN R:","ruta: "+r.getImagenUrl());
                    }



                    ExecutorService executor = Executors.newSingleThreadExecutor();
                    Handler mainHandler = new Handler(Looper.getMainLooper());

                    executor.execute(() -> {

                        boolean insertado;

                        if(NetworkUtils.hayConexion(requireContext()))
                        {
                            insertado = neg.addExterno(r);
                        }else {
                            insertado = false;
                        }
                        mainHandler.post(() -> {

                            if (insertado) {
                                Toast.makeText(requireContext(),"Creado con exito!",Toast.LENGTH_SHORT).show();
                                if (listener != null) listener.onRecordatorioGuardado();
                            } else {
                                Toast.makeText(requireContext(),"Error al crear!",Toast.LENGTH_SHORT).show();
                            }
                            dialog.dismiss();
                        });
                    });
                }
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


        Log.d("EN DISMIS","SE EJECUTO");

    }
}
