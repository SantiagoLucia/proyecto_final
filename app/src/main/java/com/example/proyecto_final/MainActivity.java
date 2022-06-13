package com.example.proyecto_final;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    public static Activity mainAct;
    private static int logueado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainAct = this;
        SharedPreferences prefs = getSharedPreferences("mi_auto", Context.MODE_PRIVATE);
        logueado = prefs.getInt("logueado",0);
        if (logueado == 1) {
            Intent i = new Intent(this, MenuActivity.class);
            startActivity(i);
        } else {
            setContentView(R.layout.activity_main);
        }
    }

    public void irIniciar(View view) {
        Intent i = new Intent(this, IniciarSesionActivity.class);
        startActivity(i);
    }

    public void irRegistrar(View view) {
        Intent i = new Intent(this, RegistrarseActivity.class);
        startActivity(i);
    }


}