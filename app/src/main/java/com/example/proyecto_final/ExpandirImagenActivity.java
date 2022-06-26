package com.example.proyecto_final;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

public class ExpandirImagenActivity extends AppCompatActivity {

    ImageView img;
    Uri image_uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expandir_imagen);
        SharedPreferences prefs = getSharedPreferences("mi_auto", Context.MODE_PRIVATE);
        img = findViewById(R.id.imagenEst);
        image_uri = Uri.parse(prefs.getString("foto",""));
        img.setImageURI(image_uri);
    }

}