package com.example.apprecordatorio.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.apprecordatorio.entidades.Recordatorio;

import com.example.apprecordatorio.openhelper.SQLite_OpenHelper;

import java.util.ArrayList;
import java.util.List;

public class RecordatorioGralDao {

    private SQLite_OpenHelper dbHelper;

    public RecordatorioGralDao(Context context) {
        dbHelper = new SQLite_OpenHelper(context, "DBRecordatorios", null, 1);
    }

    public long add(Recordatorio rec) {
        SQLiteDatabase db = null;
        long resultado = -1;
        try {
             db = dbHelper.getWritableDatabase();
            ContentValues valores = new ContentValues();
            valores.put("titulo", rec.getTitulo());
            valores.put("contenido", rec.getDescripcion());
            valores.put("imagen", rec.getImagenUrl());

             resultado = db.insert("recordatoriosGenerales", null, valores);
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

    public int update(Recordatorio rec) {
        SQLiteDatabase db = null;
        int resultado = 0;

        try
        {
            db = dbHelper.getWritableDatabase();
            ContentValues valores = new ContentValues();
            valores.put("titulo", rec.getTitulo());
            valores.put("contenido", rec.getDescripcion());
            valores.put("imagen", rec.getImagenUrl());

             resultado = db.update("recordatoriosGenerales", valores, "id = ?", new String[]{String.valueOf(rec.getId())});
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

    public int delete(Recordatorio rec) {
        SQLiteDatabase db = null;
        int resultado = 0;
        try
        {
            db = dbHelper.getWritableDatabase();
            resultado = db.delete("recordatoriosGenerales", "id = ?", new String[]{String.valueOf(rec.getId())});
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

    public List<Recordatorio> readAll() {
        List<Recordatorio> lista = new ArrayList<>();
        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = dbHelper.getReadableDatabase();

            cursor = db.rawQuery("SELECT * FROM recordatoriosGenerales ORDER BY id DESC", null);
            while (cursor.moveToNext()) {
                Recordatorio r = new Recordatorio();
                r.setId(cursor.getInt(0));
                r.setTitulo(cursor.getString(1));
                r.setDescripcion(cursor.getString(2));
                r.setImagenUrl(cursor.getString(3));
                lista.add(r);
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

    public Recordatorio readOne(int id) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        Recordatorio r = null;

        try {
            db = dbHelper.getReadableDatabase();

            cursor = db.rawQuery("SELECT * FROM recordatoriosGenerales WHERE id = ?", new String[]{String.valueOf(id)});


            if (cursor.moveToFirst()) {
                r = new Recordatorio();
                r.setId(cursor.getInt(0));
                r.setTitulo(cursor.getString(1));
                r.setDescripcion(cursor.getString(2));
                r.setImagenUrl(cursor.getString(3));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return r;
    }
}
