package com.example.proyecto_final;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        MainActivity.mainAct.finish();
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