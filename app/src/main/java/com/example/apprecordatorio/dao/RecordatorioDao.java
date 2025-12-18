package com.example.apprecordatorio.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

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
            //valores.put("id",a.getId());
            valores.put("titulo", a.getTitulo());
            valores.put("contenido", a.getDescripcion());
            valores.put("imagen", a.getImagenUrl());
            valores.put("tono",a.getTono());
            //valores.put("fecha",a.getFecha().toString());
            valores.put("hora",a.getHora());
            valores.put("minuto",a.getMinuto());
            valores.put("domingo",a.isDomingo());
            valores.put("lunes",a.isLunes());
            valores.put("martes",a.isMartes());
            valores.put("miercoles",a.isMiercoles());
            valores.put("jueves",a.isJueves());
            valores.put("viernes",a.isViernes());
            valores.put("sabado",a.isSabado());
            valores.put("estado",true);
            valores.put("baja_logica",false);
            valores.put("updated_at",System.currentTimeMillis());
            valores.put("pending_changes",1);

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

    public long addFromSync(Alarma a)
    {
        SQLiteDatabase db = null;
        long resultado = -1;

        try {
            db = dbHelper.getWritableDatabase();
            ContentValues valores = new ContentValues();
            //valores.put("id",a.getId());
            valores.put("titulo", a.getTitulo());
            valores.put("contenido", a.getDescripcion());
            valores.put("imagen", a.getImagenUrl());
            valores.put("tono",a.getTono());
            valores.put("hora",a.getHora());
            valores.put("minuto",a.getMinuto());
            valores.put("domingo",a.isDomingo());
            valores.put("lunes",a.isLunes());
            valores.put("martes",a.isMartes());
            valores.put("miercoles",a.isMiercoles());
            valores.put("jueves",a.isJueves());
            valores.put("viernes",a.isViernes());
            valores.put("sabado",a.isSabado());
            valores.put("estado",true);
            valores.put("baja_logica",a.isBajaLogica());
            valores.put("updated_at",a.getUpdatedAt());
            valores.put("pending_changes",0);
            valores.put("id_remoto",a.getIdRemoto());

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
            //valores.put("id",a.getId());
            valores.put("titulo", a.getTitulo());
            valores.put("contenido", a.getDescripcion());
            valores.put("imagen", a.getImagenUrl());
            valores.put("tono",a.getTono());
            //valores.put("fecha",a.getFecha().toString());
            valores.put("hora",a.getHora());
            valores.put("minuto",a.getMinuto());
            valores.put("domingo",a.isDomingo());
            valores.put("lunes",a.isLunes());
            valores.put("martes",a.isMartes());
            valores.put("miercoles",a.isMiercoles());
            valores.put("jueves",a.isJueves());
            valores.put("viernes",a.isViernes());
            valores.put("sabado",a.isSabado());
            valores.put("estado",a.isEstado());
            valores.put("baja_logica",a.isBajaLogica());
            valores.put("updated_at",System.currentTimeMillis());
            valores.put("pending_changes",1);


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

    public int updateIdRemoto(Alarma a) {
        SQLiteDatabase db = null;
        int resultado = 0;

        try
        {
            Log.d("UPD IDREM","EN RDAO UPDATE ID REMOTO"+a.getIdRemoto());
            db = dbHelper.getWritableDatabase();
            ContentValues valores = new ContentValues();
            valores.put("id_remoto", a.getIdRemoto());
            resultado = db.update("recordatorios", valores, "id = ?", new String[]{String.valueOf(a.getId())});
        }catch (Exception e)
        {
            e.printStackTrace();
        }finally {
            if(db!=null) {
                db.close();
            }
        }
        Log.d("UPD IDREM","RESULTADO"+resultado);
        return resultado;
    }

    public int updateFromSync(Alarma a) {
        SQLiteDatabase db = null;
        int resultado = 0;

        try
        {
            db = dbHelper.getWritableDatabase();
            ContentValues valores = new ContentValues();
            //valores.put("id",a.getId());
            valores.put("titulo", a.getTitulo());
            valores.put("contenido", a.getDescripcion());
            valores.put("imagen", a.getImagenUrl());
            valores.put("tono",a.getTono());
           // valores.put("fecha",a.getFecha().toString());
            valores.put("hora",a.getHora());
            valores.put("minuto",a.getMinuto());
            valores.put("domingo",a.isDomingo());
            valores.put("lunes",a.isLunes());
            valores.put("martes",a.isMartes());
            valores.put("miercoles",a.isMiercoles());
            valores.put("jueves",a.isJueves());
            valores.put("viernes",a.isViernes());
            valores.put("sabado",a.isSabado());
            valores.put("estado",a.isEstado());
            valores.put("baja_logica",a.isBajaLogica());
            valores.put("updated_at",a.getUpdatedAt());
            valores.put("pending_changes",0);


            resultado = db.update("recordatorios", valores, "id_remoto = ?", new String[]{String.valueOf(a.getIdRemoto())});
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
    public int setPendingChanges(Alarma r) {
        SQLiteDatabase db = null;
        int resultado = 0;

        try
        {
            db = dbHelper.getWritableDatabase();
            ContentValues valores = new ContentValues();
            valores.put("pending_changes", r.isPendingChanges());
            resultado = db.update("recordatorios", valores, "id = ?", new String[]{String.valueOf(r.getId())});
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

    /// no borrar por las dudas
    /*public int delete(Alarma a) {
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
    }*/
    public int delete(Alarma a) {
        SQLiteDatabase db = null;
        int resultado = 0;

        try {
            db = dbHelper.getWritableDatabase();

            ContentValues cv = new ContentValues();
            cv.put("baja_logica", 1);
            cv.put("updated_at",System.currentTimeMillis());
            cv.put("imagen","null");
            cv.put("pending_changes",1);

            resultado = db.update(
                    "recordatorios",
                    cv,
                    "id = ?",
                    new String[]{ String.valueOf(a.getId()) }
            );

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) db.close();
        }

        return resultado; // devuelve 1 si se marcó, 0 si no existía
    }

    public List<Alarma> readAll() {
        List<Alarma> lista = new ArrayList<>();
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = dbHelper.getReadableDatabase();

            cursor = db.rawQuery("SELECT * FROM recordatorios WHERE baja_logica = 0 ORDER BY id DESC", null);
            while (cursor.moveToNext()) {
                Alarma a = new Alarma();
                a.setId(cursor.getInt(0));
                a.setTitulo(cursor.getString(1));
                a.setDescripcion(cursor.getString(2));
                a.setImagenUrl(cursor.getString(3));
                a.setTono(cursor.getString(4));
               // a.setFecha(LocalDate.parse(cursor.getString(5)));
                a.setHora(cursor.getInt(6));
                a.setMinuto(cursor.getInt(7));
                a.setDomingo(cursor.getInt(8)==1);
                a.setLunes(cursor.getInt(9)==1);
                a.setMartes(cursor.getInt(10)==1);
                a.setMiercoles(cursor.getInt(11)==1);
                a.setJueves(cursor.getInt(12)==1);
                a.setViernes(cursor.getInt(13)==1);
                a.setSabado(cursor.getInt(14)==1);
                a.setEstado( cursor.getInt(15)==1);
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

    public void cambiarEstado (Alarma a) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        if (a.isEstado())
        {
            values.put("estado", 0);
            db.update("recordatorios", values, "id = ?", new String[]{String.valueOf(a.getId())});
        }else
        {
            values.put("estado", 1);
            db.update("recordatorios", values, "id = ?", new String[]{String.valueOf(a.getId())});
        }
    }

    public Boolean desactivarAlarma(Alarma a)
    {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

            values.put("estado", 0);
            int r =db.update("recordatorios", values, "id = ?", new String[]{String.valueOf(a.getId())});

        return ( r > 0);
    }

    public Boolean activarAlarma(Alarma a)
    {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("estado", 1);
        int r =db.update("recordatorios", values, "id = ?", new String[]{String.valueOf(a.getId())});

        return ( r > 0);
    }

    public int traerProximoId()
    {

        int id = 0;
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = dbHelper.getReadableDatabase();

            cursor = db.rawQuery("SELECT MAX(id) from recordatorios", null);
            while (cursor.moveToNext()) {
               id =   cursor.getInt(0);
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
        return id+1;
    }

    public int traerIdMaximo()
    {
        int id = 0;
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = dbHelper.getReadableDatabase();

            cursor = db.rawQuery("SELECT MAX(id) from recordatorios", null);
            while (cursor.moveToNext()) {
                id =   cursor.getInt(0);
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
        return id;
    }

    public List<Alarma> getAllPendingSync() {
        List<Alarma> lista = new ArrayList<>();
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = dbHelper.getReadableDatabase();

            cursor = db.rawQuery("SELECT * FROM recordatorios WHERE pending_changes = 1 ORDER BY id DESC", null);
            while (cursor.moveToNext()) {
                Alarma a = new Alarma();
                a.setId(cursor.getInt(0));
                a.setTitulo(cursor.getString(1));
                a.setDescripcion(cursor.getString(2));
                a.setImagenUrl(cursor.getString(3));
                a.setTono(cursor.getString(4));
               // a.setFecha(LocalDate.parse(cursor.getString(5)));
                a.setHora(cursor.getInt(6));
                a.setMinuto(cursor.getInt(7));
                a.setDomingo(cursor.getInt(8)==1);
                a.setLunes(cursor.getInt(9)==1);
                a.setMartes(cursor.getInt(10)==1);
                a.setMiercoles(cursor.getInt(11)==1);
                a.setJueves(cursor.getInt(12)==1);
                a.setViernes(cursor.getInt(13)==1);
                a.setSabado(cursor.getInt(14)==1);
                a.setEstado( cursor.getInt(15)==1);
                a.setBajaLogica(cursor.getInt(16)==1);
                a.setIdRemoto(cursor.getInt(17));
                a.setUpdatedAt(cursor.getLong(18));
                a.setPendingChanges(cursor.getInt(19)==1);
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

    public Alarma readOneByIdRemoto(int id) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        Alarma a = null;

        try {
            db = dbHelper.getReadableDatabase();

            cursor = db.rawQuery("SELECT * FROM recordatorios WHERE id_remoto = ?", new String[]{String.valueOf(id)});


            if (cursor.moveToFirst()) {
                a = new Alarma();
                a.setId(cursor.getInt(0));
                a.setTitulo(cursor.getString(1));
                a.setDescripcion(cursor.getString(2));
                a.setImagenUrl(cursor.getString(3));
                a.setTono(cursor.getString(4));
               // a.setFecha(LocalDate.parse(cursor.getString(5)));
                a.setHora(cursor.getInt(6));
                a.setMinuto(cursor.getInt(7));
                a.setDomingo(cursor.getInt(8)==1);
                a.setLunes(cursor.getInt(9)==1);
                a.setMartes(cursor.getInt(10)==1);
                a.setMiercoles(cursor.getInt(11)==1);
                a.setJueves(cursor.getInt(12)==1);
                a.setViernes(cursor.getInt(13)==1);
                a.setSabado(cursor.getInt(14)==1);
                a.setEstado( cursor.getInt(15)==1);
                a.setBajaLogica(cursor.getInt(16)==1);
                a.setIdRemoto(cursor.getInt(17));
                a.setUpdatedAt(cursor.getLong(18));
                a.setPendingChanges(cursor.getInt(19)==1);
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
        return a;
    }

    public Integer getIdRemoto(int id) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        int a = 0;

        try {
            db = dbHelper.getReadableDatabase();

            cursor = db.rawQuery("SELECT id_remoto FROM recordatorios WHERE id = ?", new String[]{String.valueOf(id)});


            if (cursor.moveToFirst()) {
               a = cursor.getInt(0);
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
        return a;
    }

}
