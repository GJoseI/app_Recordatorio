package com.example.apprecordatorio.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.apprecordatorio.entidades.Alarma;
import com.example.apprecordatorio.entidades.Recordatorio;
import com.example.apprecordatorio.openhelper.SQLite_OpenHelper;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RecordatorioDao {

    private SQLite_OpenHelper dbHelper;

    public RecordatorioDao(Context context) {
        dbHelper = new SQLite_OpenHelper(context, "DBRecordatorios", null, 1);
    }

    public long add(Alarma a)
    {
        SQLiteDatabase db = null;
        long resultado = -1;

        try {
            db = dbHelper.getWritableDatabase();
            ContentValues valores = new ContentValues();
            valores.put("titulo", a.getTitulo());
            valores.put("contenido", a.getDescripcion());
            valores.put("imagen", a.getImagenUrl());
            valores.put("tono",a.getTono());
            valores.put("fecha",a.getFecha().toString());
            valores.put("hora",a.getHora());

            resultado = db.insert("recordatorios", null, valores);
        }catch (Exception e)
        {
            e.printStackTrace();
        }finally {
            if(db!=null) {
                db.close();
            }
        }
        return  resultado;
    }


    public int update(Alarma a) {
        SQLiteDatabase db = null;
        int resultado = 0;

        try
        {
            db = dbHelper.getWritableDatabase();
            ContentValues valores = new ContentValues();
            valores.put("titulo", a.getTitulo());
            valores.put("contenido", a.getDescripcion());
            valores.put("imagen", a.getImagenUrl());
            valores.put("tono",a.getTono());
            valores.put("fecha",a.getFecha().toString());
            valores.put("hora",a.getHora());

            resultado = db.update("recordatorios", valores, "id = ?", new String[]{String.valueOf(a.getId())});
        }catch (Exception e)
        {
            e.printStackTrace();
        }finally {
            if(db!=null) {
                db.close();
            }
        }
        return resultado;
    }

    public int delete(Alarma a) {
        SQLiteDatabase db = null;
        int resultado = 0;
        try
        {
            db = dbHelper.getWritableDatabase();
            resultado = db.delete("recordatorios", "id = ?", new String[]{String.valueOf(a.getId())});
        }catch (Exception e)
        {
            e.printStackTrace();
        }finally {
            if(db!=null) {
                db.close();
            }
        }
        return resultado;
    }

    public List<Alarma> readAll() {
        List<Alarma> lista = new ArrayList<>();
        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = dbHelper.getReadableDatabase();

            cursor = db.rawQuery("SELECT * FROM recordatorios ORDER BY id DESC", null);
            while (cursor.moveToNext()) {
                Alarma a = new Alarma();
                a.setId(cursor.getInt(0));
                a.setTitulo(cursor.getString(1));
                a.setDescripcion(cursor.getString(2));
                a.setImagenUrl(cursor.getString(3));
                a.setTono(cursor.getString(4));
                a.setFecha(LocalDate.parse(cursor.getString(5)));
                a.setHora(cursor.getString(6));
                lista.add(a);
            }

        }catch (Exception e)
        {
            e.printStackTrace();
        }finally {
            if (cursor != null) {
                cursor.close();
            }
            if(db!=null) {
                db.close();
            }
        }
        return lista;
    }


}
