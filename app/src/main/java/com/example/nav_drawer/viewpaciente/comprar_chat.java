package com.example.nav_drawer.viewpaciente;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.view.View;
import android.widget.Toast;


import com.example.nav_drawer.R;

public class comprar_chat extends AppCompatActivity {
    TextView txtiddoctor;
    Button comprar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comprar_chat);
        txtiddoctor = (TextView) findViewById(R.id.txtiddoc);
        comprar = (Button) findViewById(R.id.btncomprar);
        String idDoctor = getIntent().getStringExtra("idDoctor");
        txtiddoctor.setText("ID doctor: " + idDoctor);
    }
}