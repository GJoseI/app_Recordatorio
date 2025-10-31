package com.example.apprecordatorio.fragments;

import android.app.Dialog;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apprecordatorio.R;
import com.example.apprecordatorio.dialogs.AltaRecordatorioGeneral;
import com.example.apprecordatorio.entidades.Recordatorio;
import com.example.apprecordatorio.interfaces.OnRecordatorioGuardadoListener;
import com.example.apprecordatorio.negocio.RecordatorioGralNegocio;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;


public class GeneralesFragment extends Fragment implements OnRecordatorioGuardadoListener {

    private LinearLayout containerRecordatorios;


    private List<Recordatorio> lista;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_generales, container, false);

        containerRecordatorios = view.findViewById(R.id.containerRecordatoriosGenerales);
        FloatingActionButton  btnAgregar = view.findViewById(R.id.btnAgregarRecGral);

        btnAgregar.setOnClickListener(v -> agregarRecordatorio(inflater));

        cargarRecordatorios(inflater);

        return view;
    }


    private void agregarRecordatorio(LayoutInflater inflater) {

        AltaRecordatorioGeneral dialog = new AltaRecordatorioGeneral();
        dialog.setOnRecordatorioGuardadoListener(this);
        dialog.show(getChildFragmentManager(), "hola");
    }

    private void cargarRecordatorios(LayoutInflater inflater)
    {
        containerRecordatorios.removeAllViews();
        RecordatorioGralNegocio neg = new RecordatorioGralNegocio(requireContext());
        lista = neg.readAll();

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

                txtTitulo.setText(r.getTitulo());
                txtDescripcion.setText(r.getDescripcion());

                cardView.setOnClickListener(v -> {

                    if (layoutExpandible.getVisibility() != View.VISIBLE) {

                        layoutExpandible.setVisibility(View.VISIBLE);

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
    }

    @Override
    public void onRecordatorioGuardado() {
        cargarRecordatorios(LayoutInflater.from(requireContext()));
    }

    public void editarRecordatorio(Recordatorio r)
    {}
    public void borrarRecordatorio(Recordatorio r, RecordatorioGralNegocio neg, LayoutInflater inflater)
    {
        if(neg.delete(r)>0)
        {
            Toast.makeText(requireContext(), "borrado con exito.", Toast.LENGTH_SHORT).show();
        }
        cargarRecordatorios(inflater);
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