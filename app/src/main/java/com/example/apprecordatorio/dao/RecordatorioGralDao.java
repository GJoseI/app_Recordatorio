package com.example.apprecordatorio.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.apprecordatorio.entidades.Alarma;
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
           // valores.put("id",rec.getId());
            valores.put("titulo", rec.getTitulo());
            valores.put("contenido", rec.getDescripcion());
            valores.put("imagen", rec.getImagenUrl());
            valores.put("baja_logica",false);
            valores.put("updated_at", System.currentTimeMillis());
            valores.put("pending_changes", 1);

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

    public long addFromSync(Recordatorio rec) {
        SQLiteDatabase db = null;
        long resultado = -1;
        try {
            db = dbHelper.getWritableDatabase();
            ContentValues valores = new ContentValues();
            // valores.put("id",rec.getId());
            valores.put("titulo", rec.getTitulo());
            valores.put("contenido", rec.getDescripcion());
            valores.put("imagen", rec.getImagenUrl());
            valores.put("baja_logica",false);
            valores.put("updated_at", rec.getUpdatedAt());
            valores.put("pending_changes", 0);
            valores.put("id_remoto", rec.getIdRemoto());

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
           // valores.put("id",rec.getId());
            valores.put("titulo", rec.getTitulo());
            valores.put("contenido", rec.getDescripcion());
            valores.put("imagen", rec.getImagenUrl());
            valores.put("baja_logica", rec.isBajaLogica() ? 1 : 0);
            valores.put("updated_at", System.currentTimeMillis());
            valores.put("pending_changes", 1);

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
    public int updateIdRemoto(Recordatorio r) {
        SQLiteDatabase db = null;
        int resultado = 0;

        try
        {
            db = dbHelper.getWritableDatabase();
            ContentValues valores = new ContentValues();
            valores.put("id_remoto", r.getIdRemoto());
            resultado = db.update("recordatoriosGenerales", valores, "id = ?", new String[]{String.valueOf(r.getId())});
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

    public long updateFromSync(Recordatorio r) {
        SQLiteDatabase db = null;
        int resultado = 0;

        try
        {
            db = dbHelper.getWritableDatabase();
            ContentValues cv = new ContentValues();
            // valores.put("id",rec.getId());
            cv.put("id_remoto", r.getIdRemoto());
            cv.put("titulo", r.getTitulo());
            cv.put("contenido", r.getDescripcion());
            cv.put("imagen", r.getImagenUrl());
            //cv.put("id_paciente", r.getPacienteId());
            cv.put("baja_logica", r.isBajaLogica() ? 1 : 0);
            cv.put("updated_at", r.getUpdatedAt());
            cv.put("pending_changes", 0);

            resultado = db.update("recordatoriosGenerales", cv, "id_remoto = ?", new String[]{String.valueOf(r.getIdRemoto())});
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

    public int setPendingChanges(Recordatorio r) {
        SQLiteDatabase db = null;
        int resultado = 0;

        try
        {
            db = dbHelper.getWritableDatabase();
            ContentValues valores = new ContentValues();
            valores.put("pending_changes", r.isPendingChanges());
            resultado = db.update("recordatoriosGenerales", valores, "id = ?", new String[]{String.valueOf(r.getId())});
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
  /*
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
    }*/
  public int delete(Recordatorio rec) {
      SQLiteDatabase db = null;
      int resultado = 0;

      try {
          db = dbHelper.getWritableDatabase();

          ContentValues cv = new ContentValues();
          cv.put("baja_logica", 1);
          cv.put("updated_at", System.currentTimeMillis());
          cv.put("pending_changes", 1);

          resultado = db.update(
                  "recordatoriosGenerales",
                  cv,
                  "id = ?",
                  new String[]{ String.valueOf(rec.getId()) }
          );

      } catch (Exception e) {
          e.printStackTrace();
      } finally {
          if (db != null) db.close();
      }

      return resultado; // devuelve 1 si se marcó, 0 si no existía
  }


    public List<Recordatorio> readAll() {
        List<Recordatorio> lista = new ArrayList<>();
        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = dbHelper.getReadableDatabase();

            cursor = db.rawQuery("SELECT * FROM recordatoriosGenerales WHERE baja_logica = 0 ORDER BY id DESC", null);
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
    public int traerMaximoId()
    {

        int id = 0;
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = dbHelper.getReadableDatabase();

            cursor = db.rawQuery("SELECT MAX(id) from recordatoriosGenerales", null);
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

    public List<Recordatorio> getAllPendingSync() {
        List<Recordatorio> lista = new ArrayList<>();
        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = dbHelper.getReadableDatabase();

            cursor = db.rawQuery("SELECT * FROM recordatoriosGenerales WHERE pending_changes = 1 ORDER BY id DESC", null);
            while (cursor.moveToNext()) {
                Recordatorio r = new Recordatorio();
                r.setId(cursor.getInt(0));
                r.setTitulo(cursor.getString(1));
                r.setDescripcion(cursor.getString(2));
                r.setImagenUrl(cursor.getString(3));
                r.setBajaLogica(cursor.getInt(4)==1);
                r.setIdRemoto(cursor.getInt(5));
                r.setUpdatedAt(cursor.getLong(6));
                r.setPendingChanges(cursor.getInt(7)==1);
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

    public Recordatorio readOneByIdRemoto(int id) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        Recordatorio r = null;

        try {
            db = dbHelper.getReadableDatabase();

            cursor = db.rawQuery("SELECT * FROM recordatoriosGenerales WHERE id_remoto = ?", new String[]{String.valueOf(id)});


            if (cursor.moveToFirst()) {
                r = new Recordatorio();
                r.setId(cursor.getInt(0));
                r.setTitulo(cursor.getString(1));
                r.setDescripcion(cursor.getString(2));
                r.setImagenUrl(cursor.getString(3));
                r.setBajaLogica(cursor.getInt(4)==1);
                r.setIdRemoto(cursor.getInt(5));
                r.setUpdatedAt(cursor.getLong(6));
                r.setPendingChanges(cursor.getInt(7)==1);
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
