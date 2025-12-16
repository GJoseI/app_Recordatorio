package com.example.apprecordatorio.fragments;

import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.apprecordatorio.R;
import com.example.apprecordatorio.dialogs.AltaRecordatorioGeneral;
import com.example.apprecordatorio.dialogs.ModificacionNotaExterno;
import com.example.apprecordatorio.dialogs.ModificacionRecordatorioExterno;
import com.example.apprecordatorio.dialogs.ModificacionRecordatorioGeneral;
import com.example.apprecordatorio.entidades.Recordatorio;
import com.example.apprecordatorio.interfaces.OnRecordatorioGuardadoListener;
import com.example.apprecordatorio.negocio.RecordatorioGralNegocio;
import com.example.apprecordatorio.util.NetworkUtils;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class NotaTutorFragment extends Fragment implements OnRecordatorioGuardadoListener {
    private LinearLayout containerRecordatorios;


    private List<Recordatorio> lista;

    private AltaRecordatorioGeneral currentDialog = null;
    private Bundle args;

    private static final String BASE_URL = "http://10.0.2.2/pruebaphp";



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nota_tutor, container, false);

        containerRecordatorios = view.findViewById(R.id.containerRecordatoriosGenerales);

        args = getArguments();
        cargarRecordatorios();

        return view;
    }

    private void cargarRecordatorios()
    {
        LayoutInflater inflater = getLayoutInflater();
        containerRecordatorios.removeAllViews();
        RecordatorioGralNegocio neg = new RecordatorioGralNegocio(requireContext());

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(()->{

            if(args!=null)
            {
                lista = neg.readAllEx(args.getInt("idPaciente"));
            }



            handler.post(()->{

                if(lista!=null)
                {
                    for (Recordatorio r : lista)
                    {
                        View cardView = inflater.inflate(R.layout.item_recordatorio_general, containerRecordatorios, false);

                        TextView txtTitulo = cardView.findViewById(R.id.txtTituloRecGral);
                        ImageButton btnBorrar = cardView.findViewById(R.id.ibtnDeleteRegGral);
                        ImageButton btnEditar = cardView.findViewById(R.id.ibtnEditarRecGral);
                        TextView txtDescripcion = cardView.findViewById(R.id.txtDescripcionRecGral);
                        LinearLayout layoutExpandible = cardView.findViewById(R.id.layoutExpandibleRecGral);
                        ImageView imgRec = cardView.findViewById(R.id.imgRecGral);

                        btnBorrar.setOnClickListener(v ->{
                            confirmarBorrado(r,neg,inflater);
                        });

                        btnEditar.setOnClickListener(v->{
                            editarRecordatorio(r);
                        });

                        txtTitulo.setText(r.getTitulo());
                        txtDescripcion.setText(r.getDescripcion());
                        String imgUri = r.getImagenUrl();

                        if(imgUri!=null)
                        {
                            Log.e("URI:",imgUri);
                            imgRec.setImageURI(Uri.parse(imgUri));
                        }


                        cardView.setOnClickListener(v -> {

                            if (layoutExpandible.getVisibility() != View.VISIBLE) {


                                layoutExpandible.setVisibility(View.VISIBLE);


                                if (r.getImagenUrl() != null) {

                                    Glide.with(this)
                                            .load(BASE_URL + "/" + r.getImagenUrl())
                                            .into(imgRec);

                                    imgRec.setVisibility(View.VISIBLE);
                                }




                                cardView.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.fondoElementoSeleccionado));
                                txtTitulo.setTextColor(ContextCompat.getColor(requireContext(), R.color.letraBlanca));
                                txtDescripcion.setTextColor(ContextCompat.getColor(requireContext(), R.color.letraBlanca));

                                btnEditar.setColorFilter(ContextCompat.getColor(requireContext(), R.color.letraBlanca));
                                btnBorrar.setColorFilter(ContextCompat.getColor(requireContext(), R.color.letraBlanca));
                            } else {

                                layoutExpandible.setVisibility(View.GONE);

                                cardView.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.fondoElementoOscuro));
                                txtTitulo.setTextColor(ContextCompat.getColor(requireContext(), R.color.letraGris));
                                txtDescripcion.setTextColor(ContextCompat.getColor(requireContext(), R.color.letraGris));

                                btnEditar.setColorFilter(ContextCompat.getColor(requireContext(), R.color.letraGris));
                                btnBorrar.setColorFilter(ContextCompat.getColor(requireContext(), R.color.letraGris));
                            }
                        });

                        containerRecordatorios.addView(cardView);
                    }
                }

            });


        });



    }

    @Override
    public void onRecordatorioGuardado() {
        cargarRecordatorios();
    }

    public void editarRecordatorio(Recordatorio r)
    {
        ModificacionNotaExterno dialog = new ModificacionNotaExterno();
        dialog.setOnRecordatorioGuardadoListener(this);

        Log.d("EDITAR REC GRAL","ID: "+r.getId()+" ID REMOTO: "+r.getIdRemoto()+" ID PACIENTE: "+r.getPacienteId());

        Bundle args = new Bundle();
        args.putInt("id", r.getId());
        args.putInt("idRemoto", r.getIdRemoto());
        args.putInt("idPaciente", r.getPacienteId());
        args.putString("titulo", r.getTitulo());
        args.putString("descripcion", r.getDescripcion());
        args.putString("imagen", r.getImagenUrl());
        dialog.setArguments(args);


        dialog.show(getChildFragmentManager(), "Editar Recordatorio");
    }
    public void borrarRecordatorio(Recordatorio r, RecordatorioGralNegocio neg, LayoutInflater inflater)
    {

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler mainHandler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            boolean insertado;

            if(NetworkUtils.hayConexion(requireContext()))
            {
                insertado = neg.deleteEx(r);
            }else {
                insertado = false;
            }
            mainHandler.post(() -> {

                if (insertado) {

                    Toast.makeText(requireContext(), "borrado con exito.", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(requireContext(),"Error al borrar!",Toast.LENGTH_SHORT).show();
                }
                cargarRecordatorios();
            });
        });

       /* if(neg.delete(r)>0)
        {
            Toast.makeText(requireContext(), "borrado con exito.", Toast.LENGTH_SHORT).show();
        }*/

    }

    private void confirmarBorrado(Recordatorio recordatorio, RecordatorioGralNegocio neg, LayoutInflater inflater) {

        new MaterialAlertDialogBuilder(requireContext(), R.style.Theme_Oscuro_Dialog)
                .setTitle("Eliminar recordatorio")
                .setMessage("¿Seguro que deseas eliminar "+ recordatorio.getTitulo()+"?")
                .setPositiveButton("Eliminar", (dialog, which) -> borrarRecordatorio(recordatorio,neg,inflater))
                .setNegativeButton("Cancelar", null)
                .show();
    }
}