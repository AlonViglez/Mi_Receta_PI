package com.example.nav_drawer.viewdoc;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.nav_drawer.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Actualizarperfildoc extends AppCompatActivity {
    EditText editnombre;
    EditText editnumero;

    String userEmail;

    EditText editdescripcion;

    Button editProfile;
    String id;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizarperfildoc);

        editnombre = findViewById(R.id.profileName);
        editnumero = findViewById(R.id.profilenum);
        editdescripcion = findViewById(R.id.profiledescr);
        editProfile = findViewById(R.id.editbutton);

        intent = getIntent();
        id = intent.getStringExtra("id"); //Recibimos el id del fragment

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            userEmail = currentUser.getEmail();
            Toast.makeText(this, "Usuario autenticado" + userEmail, Toast.LENGTH_SHORT).show();
            // Resto del código...
        } else {
            Log.e(TAG, "Usuario no autenticado");
            Toast.makeText(this, "Usuario no autenticado", Toast.LENGTH_SHORT).show();
        }

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String valorEditText = editdescripcion.getText().toString();
                String valorEditTextnombre = editnombre.getText().toString();
                String valorEditTextnumero = editnumero.getText().toString();
                uploadTextToFirestore(userEmail, valorEditText, valorEditTextnombre, valorEditTextnumero);
            }
        });
    }

    public void uploadTextToFirestore(String email, String valorEditText, String valorEditTextnombre, String valorEditTextnumero) {
        try {
            Toast.makeText(Actualizarperfildoc.this, " "+valorEditText + valorEditTextnombre + valorEditTextnumero, Toast.LENGTH_SHORT).show();
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

            if (currentUser != null) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();

                Map<String, Object> data = new HashMap<>();
                data.put("descripcion", valorEditText);
                if (email != null && !email.isEmpty()) {
                    // Llamar a update aquí
                    db.collection("altadoctores")
                            .document(id)
                            .update("descripcion", valorEditText)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "Campo 'descripcion' actualizado con éxito");
                                    // Puedes realizar acciones adicionales si es necesario
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error al actualizar campo 'descripcion'", e);
                                    // Puedes manejar el error aquí
                                    Toast.makeText(Actualizarperfildoc.this, "Error al actualizar campo 'descripcion'", Toast.LENGTH_SHORT).show();
                                }
                            });
                    Map<String, Object> data2 = new HashMap<>();
                    data2.put("nombre", valorEditTextnombre);
                    db.collection("altadoctores")
                            .document(id)
                            .update("nombre",valorEditTextnombre)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "Campo 'descripcion' actualizado con éxito");
                                    // Puedes realizar acciones adicionales si es necesario
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error al actualizar campo 'descripcion'", e);
                                    // Puedes manejar el error aquí
                                    Toast.makeText(Actualizarperfildoc.this, "Error al actualizar campo 'descripcion'", Toast.LENGTH_SHORT).show();
                                }
                            });
                    Map<String, Object> data3 = new HashMap<>();
                    data3.put("telefono", valorEditTextnumero);
                    db.collection("altadoctores")
                            .document(id)
                            .update("telefono",valorEditTextnumero)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "Campo 'descripcion' actualizado con éxito");
                                    // Puedes realizar acciones adicionales si es necesario
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error al actualizar campo 'descripcion'", e);
                                    // Puedes manejar el error aquí
                                    Toast.makeText(Actualizarperfildoc.this, "Error al actualizar campo 'descripcion'", Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    Log.e(TAG, "El correo electrónico es nulo o vacío");
                    Toast.makeText(Actualizarperfildoc.this, "El correo electrónico es nulo o vacío", Toast.LENGTH_SHORT).show();
                }
            } else {
                Log.e(TAG, "Usuario no autenticado");
                Toast.makeText(Actualizarperfildoc.this, "Usuario no autenticado", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.e(TAG, "Error: " + e.getMessage());
            Toast.makeText(Actualizarperfildoc.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}