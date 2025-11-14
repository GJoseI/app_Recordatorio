package com.example.apprecordatorio.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import com.example.apprecordatorio.entidades.Paciente;
import com.example.apprecordatorio.openhelper.SQLite_OpenHelper;

public class PacienteDao {
    private SQLite_OpenHelper dbHelper;

    public PacienteDao(Context context)
    {
        dbHelper = new SQLite_OpenHelper(context,"DBRecordatorios",null,1);
    }

   public long add(Paciente p)
    {
        SQLiteDatabase db = null;
        long resultado = -1;

        try {
            db = dbHelper.getWritableDatabase();
            ContentValues valores = new ContentValues();
            valores.put("id",p.getId());
            valores.put("nombre",p.getNombre());

            resultado = db.insert("paciente", null, valores);
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

    public Paciente read()
    {
        Paciente p = null;
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = dbHelper.getReadableDatabase();

            cursor = db.rawQuery("SELECT * FROM paciente", null);
            while (cursor.moveToNext()) {
                p = new Paciente();
                p.setId(cursor.getInt(0));
                p.setNombre(cursor.getString(1));
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
        return p;
    }
}
