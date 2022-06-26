package com.example.proyecto_final;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class TelefonoActivity extends AppCompatActivity {

    Button ambulancia, bomberos, policia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_telefono);

        ambulancia = findViewById(R.id.btnAmbulancia);
        bomberos = findViewById(R.id.btnBomberos);
        policia = findViewById(R.id.btnPolicia);

    }

    public void llamarAmb(View view) {
        Intent i = new Intent(Intent.ACTION_DIAL);
        i.setData(Uri.parse("tel:107"));
        startActivity(i);
    }

    public void llamarBom(View view) {
        Intent i = new Intent(Intent.ACTION_DIAL);
        i.setData(Uri.parse("tel:100"));
        startActivity(i);
    }

    public void llamarPol(View view) {
        Intent i = new Intent(Intent.ACTION_DIAL);
        i.setData(Uri.parse("tel:911"));
        startActivity(i);
    }

    public void llamarDef(View view) {
        Intent i = new Intent(Intent.ACTION_DIAL);
        i.setData(Uri.parse("tel:103"));
        startActivity(i);
    }

}