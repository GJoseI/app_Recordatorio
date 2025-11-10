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
                    "imagen text)";

            String query2 = "create table recordatorios(id integer primary key autoincrement, titulo text, contenido text," +
                   "imagen text, tono text, fecha date,  hora text, estado integer)";

            db.execSQL(query2);
            db.execSQL(query);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
