package com.example.nav_drawer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class IdentifyRegister extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identify_register);
        ImageView btnUsuario = findViewById(R.id.btn_usuario);
        ImageView btnDoctor = findViewById(R.id.btn_doctor);

        //Boton USUARIO
        btnUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(IdentifyRegister.this, Registro.class);
                startActivity(intent);
            }
        });

        // Boton DOCTOR
        btnDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(IdentifyRegister.this, RegistroDoctor.class);
                startActivity(intent);
            }
        });
    }
}