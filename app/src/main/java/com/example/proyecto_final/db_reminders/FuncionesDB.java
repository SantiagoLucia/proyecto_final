package com.example.proyecto_final.db_reminders;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class FuncionesDB extends BBDD_Helper{
    Context context;
    public FuncionesDB(Context context) {
        super(context);
        this.context = context;
    }

    String value = "";

    public String Insert(Reminders reminder){
        BBDD_Helper dbHelper = new BBDD_Helper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(Estructura_BBDD.NOMBRE_COL2, reminder.getMessage());
        values.put(Estructura_BBDD.NOMBRE_COL3, df.format(reminder.getRemindDate()));

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(Estructura_BBDD.TABLE_NAME, null, values);
        value = "Se guardó el registro con clave: " + newRowId;
        Log.d("STORED_REMINDER",value);
        return value;
    }

    @SuppressLint("Range")
    public List<Reminders> getAll(){
        BBDD_Helper dbHelper = new BBDD_Helper(context);
        List<Reminders> arr = new ArrayList<Reminders>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                Estructura_BBDD.NOMBRE_COL1,
                Estructura_BBDD.NOMBRE_COL2,
                Estructura_BBDD.NOMBRE_COL3,
        };

        try {
            Cursor cursor = db.rawQuery("select * from alertas",null);

            if (cursor.moveToFirst()) {
                do {
                    Reminders rem = new Reminders();

                    Log.d("REMIND", String.valueOf(cursor));
                    rem.setMessage(cursor.getString(cursor.getColumnIndex("Nombre")));
                    Locale dateEs = new Locale("es","ES");
                    SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", dateEs);
                    Date remint = null;
                    try {
                        remint = df.parse(cursor.getString(cursor.getColumnIndex("fecha")));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    rem.setRemindDate(remint);
                    rem.setId(cursor.getInt(cursor.getColumnIndex("Id")));
                    Log.d("CURSOR_vx1", String.valueOf(rem.getId()));
                    arr.add(rem);
                } while (cursor.moveToNext());
            }
            return (List<Reminders>) arr;
        } catch (Exception e) {
            Log.d("ERROR_QR", String.valueOf(e));
            return null;
        }
    }

    public String Delete(Integer id){
        Log.d("DELETE_1","Entre al Delete");
        Log.d("DELETE_2", String.valueOf(id));
        BBDD_Helper dbHelper = new BBDD_Helper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String selection = Estructura_BBDD.NOMBRE_COL1 + "=?";
        String[] selectionArgs = {String.valueOf(id)};
        int deleted = db.delete(Estructura_BBDD.TABLE_NAME, selection, selectionArgs);
        Log.d("DELETE_3", String.valueOf(deleted));
        if(deleted != 0) {
            value = "Correcto";
        } else {
            value = "Error";
        }
        return value;
    }

    @SuppressLint("Range")
    public List<Reminders> orderThetable(){
        BBDD_Helper dbHelper = new BBDD_Helper(context);
        List<Reminders> arr = new ArrayList<Reminders>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                // Estructura_BBDD.NOMBRE_COL1,
                Estructura_BBDD.NOMBRE_COL2,
                Estructura_BBDD.NOMBRE_COL3,
        };

        try {
            Cursor cursor = db.rawQuery("select * from alertas order by fecha",null);

            if (cursor.moveToFirst()) {
                do {
                    Reminders rem = new Reminders();
                    Log.d("REMIND", String.valueOf(cursor));
                    rem.setMessage(cursor.getString(cursor.getColumnIndex("Nombre")));
                    SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.ENGLISH);
                    Date remint = null;
                    try {
                        remint = df.parse(cursor.getString(cursor.getColumnIndex("fecha")));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    rem.setRemindDate(remint);
                    arr.add(rem);
                } while (cursor.moveToNext());
            }
            return (ArrayList<Reminders>) arr;
        } catch (Exception e) {
            Log.d("ERROR_QR", String.valueOf(e));
            return null;
        }
    }
}
