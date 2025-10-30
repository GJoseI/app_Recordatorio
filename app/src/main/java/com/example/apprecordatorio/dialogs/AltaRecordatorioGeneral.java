package com.example.apprecordatorio.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.apprecordatorio.R;

import java.util.zip.Inflater;

public class AltaRecordatorioGeneral extends DialogFragment {

    private TextView titulo;
    private TextView texto;

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

        builder.setView(view);
        AlertDialog dialog = builder.create();

        btnCancelar.setOnClickListener(v -> dialog.dismiss());
        btnGuardar.setOnClickListener(v -> {
            String titulo = editTitulo.getText().toString().trim();
            String contenido = editContenido.getText().toString().trim();
            if (!titulo.isEmpty()) {
                //listener.onRecordatorioGuardado(titulo, contenido);
                dialog.dismiss();
            } else {
                editTitulo.setError("Ingrese un título");
            }
        });

        return dialog;
    }

    public static String TAG = "PurchaseConfirmationDialog";
}
