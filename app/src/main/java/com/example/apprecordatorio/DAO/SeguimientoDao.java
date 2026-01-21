package com.example.apprecordatorio.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.apprecordatorio.Entidades.Alarma;
import com.example.apprecordatorio.Entidades.Seguimiento;
import com.example.apprecordatorio.OpenHelper.SQLite_OpenHelper;

import java.util.ArrayList;
import java.util.List;

public class SeguimientoDao {


    private SQLite_OpenHelper dbHelper;

    public SeguimientoDao(Context context) {
        dbHelper = new SQLite_OpenHelper(context, "DBRecordatorios", null, 1);
    }
    public List<Seguimiento> readAllPendingUpload()
    {
        List<Seguimiento> lista = new ArrayList<>();
        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = dbHelper.getReadableDatabase();

            cursor = db.rawQuery("SELECT * FROM seguimiento WHERE pending_upload = 1 ORDER BY id asc", null);
            while (cursor.moveToNext()) {
                Seguimiento s = new Seguimiento();

                s.setId(cursor.getInt(0));

                Alarma a = new Alarma();
                a.setIdRemoto(cursor.getInt(1));
                a.setPacienteId(cursor.getInt(2));
                s.setAlarma(a);

                s.setAtendida(cursor.getInt(3)==1);
                s.setTimestamp(cursor.getString(4));

                s.setPending_upload(cursor.getInt(5)==1);

                lista.add(s);
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

    public int setPendingUpload(Seguimiento s) {
        SQLiteDatabase db = null;
        int resultado = 0;

        try
        {
            db = dbHelper.getWritableDatabase();
            ContentValues valores = new ContentValues();
            valores.put("pending_upload", s.isPending_upload());
            resultado = db.update("seguimiento", valores, "id = ?", new String[]{String.valueOf(s.getId())});
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

    public long add(Seguimiento s) {
        SQLiteDatabase db = null;
        long resultado = -1;
        try {
            db = dbHelper.getWritableDatabase();
            ContentValues valores = new ContentValues();
            // valores.put("id",rec.getId());
            valores.put("pending_upload",1);
            valores.put("id_paciente",s.getAlarma().getPacienteId());
            valores.put("id_alarma",s.getAlarma().getIdRemoto());
            valores.put("timestamp",s.getTimestamp());
            valores.put("atendida",s.isAtendida());

            resultado = db.insert("seguimiento", null, valores);
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
}
