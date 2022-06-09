package com.example.proyecto_final;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MultasActivity extends AppCompatActivity {

    Button btn_prov, btn_nac;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multas);

        btn_prov = findViewById(R.id.infracProvincia);
        btn_nac = findViewById(R.id.infracNacion);

        btn_prov.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), WebActivity.class);
                i.putExtra("url", "https://infraccionesba.gba.gob.ar/consulta-infraccion");
                startActivity(i);

            }
        });

        btn_nac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), WebActivity.class);
                i.putExtra("url", "https://consultainfracciones.seguridadvial.gob.ar");
                startActivity(i);

            }
        });
    }
}