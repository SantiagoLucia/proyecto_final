package com.example.proyecto_final;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        MainActivity.mainAct.finish();
    }

    public void cerrarSesion(View view) {
        SharedPreferences prefs = getSharedPreferences("mi_auto", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("logueado", 0);
        editor.commit();
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
        finish();
    }

    public void aEstacionar(View view) {
        Intent i = new Intent(getApplicationContext(), EstacionarActivity.class);
        startActivity(i);
    }

    public void aInfracciones(View view) {
        Intent i = new Intent(getApplicationContext(), MultasActivity.class);
        startActivity(i);
    }
}