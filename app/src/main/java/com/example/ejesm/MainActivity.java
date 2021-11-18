package com.example.ejesm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
    public void abrir (View a){
        Intent abriractmodi = new Intent(MainActivity.this,Registro.class);
        startActivity(abriractmodi);
    }
}