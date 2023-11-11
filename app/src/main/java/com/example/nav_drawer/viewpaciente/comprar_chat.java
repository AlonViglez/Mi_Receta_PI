package com.example.nav_drawer.viewpaciente;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.nav_drawer.R;

public class comprar_chat extends AppCompatActivity {
    TextView txtiddoctor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comprar_chat);
        txtiddoctor = (TextView) findViewById(R.id.txtiddoc);
        String idDoctor = getIntent().getStringExtra("idDoctor");
        txtiddoctor.setText(idDoctor);
    }
}