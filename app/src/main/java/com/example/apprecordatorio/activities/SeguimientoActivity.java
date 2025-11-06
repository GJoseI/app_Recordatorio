package com.example.apprecordatorio.activities;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.apprecordatorio.R;

public class SeguimientoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_seguimiento);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        LinearLayout container = findViewById(R.id.containerRecordatoriosSeguimiento);

        LayoutInflater inflater = LayoutInflater.from(this);

        // 3️⃣ Inflar las cards y agregarlas al contenedor
        // Supongamos que tu layout de card es "item_card_recordatorio.xml"
        for (int i = 0; i < 3; i++) {
            View cardView = inflater.inflate(R.layout.item_seguimiento, container, false);

            // Si tu card tiene textos o botones, los seteás acá:
            TextView titulo = cardView.findViewById(R.id.txtTituloRecSeg);
            titulo.setText("Recordatorio " + (i + 1));

            container.addView(cardView);

        }
    }
}