package com.example.apprecordatorio.openhelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class SQLite_OpenHelper extends SQLiteOpenHelper {
    public SQLite_OpenHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
            String query = "create table recordatoriosGenerales(id integer primary key autoincrement, titulo text, contenido text, " +
                    "imagen text, baja_logica int,id_remoto int,updated_at int, pending_changes int)";

            String query2 = "create table recordatorios(id integer primary key autoincrement, titulo text, contenido text," +
                   "imagen text, tono text, fecha date,  hora int, minuto int," +
                    "domingo int, lunes int, martes int, miercoles int," +
                    "jueves int, viernes int, sabado int, estado integer, baja_logica int" +
                    ",id_remoto int, updated_at int, pending_changes int)";

            String query3 ="create table paciente(id integer primary key, nombre text)";

            db.execSQL(query2);
            db.execSQL(query);
            db.execSQL(query3);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
