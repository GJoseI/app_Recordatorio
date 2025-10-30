package com.example.apprecordatorio.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.apprecordatorio.entidades.Recordatorio;
import com.example.apprecordatorio.enums.TipoRecordatorio;
import com.example.apprecordatorio.openhelper.SQLite_OpenHelper;

import java.util.ArrayList;
import java.util.List;

public class RecordatorioGralDao {

    private SQLite_OpenHelper dbHelper;

    public RecordatorioGralDao(Context context) {
        dbHelper = new SQLite_OpenHelper(context, "DBRecordatorios", null, 1);
    }

    public long add(String titulo, String contenido, String imagen) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put("titulo", titulo);
        valores.put("contenido", contenido);
        valores.put("imagen", imagen);

        long resultado = db.insert("recordatorios", null, valores);
        db.close();
        return resultado;
    }

    public int update(int id, String titulo, String contenido, String imagen) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put("titulo", titulo);
        valores.put("contenido", contenido);
        valores.put("imagen", imagen);

        int resultado = db.update("recordatorios", valores, "id = ?", new String[]{String.valueOf(id)});
        db.close();
        return resultado;
    }

    public int delete(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int resultado = db.delete("recordatorios", "id = ?", new String[]{String.valueOf(id)});
        db.close();
        return resultado;
    }

    public List<Recordatorio> readAll() {
        List<Recordatorio> lista = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM recordatorios ORDER BY id DESC", "general");
        if (cursor.moveToFirst()) {
            do {
                Recordatorio r = new Recordatorio();
                r.setId(cursor.getInt(0));
                r.setTitulo(cursor.getString(1));
                r.setContenido(cursor.getString(2));
                r.setImagen(cursor.getString(3));
                lista.add(r);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return lista;
    }
}
