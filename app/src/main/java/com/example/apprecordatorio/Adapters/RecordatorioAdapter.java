package com.example.apprecordatorio.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apprecordatorio.R;
import com.example.apprecordatorio.Entidades.Recordatorio;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class RecordatorioAdapter extends RecyclerView.Adapter<RecordatorioAdapter.RecordatorioViewHolder> {
    private List<Recordatorio> listaRecordatorios;

    //Constructor: recibe la lista de recordatorios
    public RecordatorioAdapter(List<Recordatorio> listaRecordatorios) {
        this.listaRecordatorios = listaRecordatorios;
    }

    //Crea una nueva vista (solo cuando hace falta)
    @NonNull
    @Override
    public RecordatorioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recordatorio, parent, false);
        return new RecordatorioViewHolder(view);
    }

    //Enlaza los datos del objeto Recordatorio con la vista
    @Override
    public void onBindViewHolder(@NonNull RecordatorioViewHolder holder, int position) {
        Recordatorio rec = listaRecordatorios.get(position);

        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
/*
        holder.tvDescripcion.setText(rec.getDescripcion());
        holder.tvFecha.setText(rec.getFecha().format(formato));
        holder.tvHora.setText(rec.getHora());
        holder.tvEstado.setText(rec.isEstado() ? "Activo" : "Inactivo");

        holder.btnEditar.setOnClickListener(v -> {
            // cambiar despues
        });*/
    }

    // cuantos elementos hay en la lista
    @Override
    public int getItemCount() {
        return listaRecordatorios != null ? listaRecordatorios.size() : 0;
    }

    // Clase interna que representa un ítem de la lista
    public static class RecordatorioViewHolder extends RecyclerView.ViewHolder {
        TextView tvDescripcion, tvFecha, tvHora, tvEstado;
        ImageButton btnEditar;

        public RecordatorioViewHolder(@NonNull View itemView) {
            super(itemView);
          //  tvDescripcion = itemView.findViewById(R.id.tvDescripcion);
           // tvFecha = itemView.findViewById(R.id.tvFecha);
            tvHora = itemView.findViewById(R.id.tvHora);
            //tvEstado = itemView.findViewById(R.id.tvEstado);
            btnEditar = itemView.findViewById(R.id.btnEditar);
        }
    }
}
