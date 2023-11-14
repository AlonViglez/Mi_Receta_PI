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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nav_drawer.IdentifyRegister;
import com.example.nav_drawer.Login;
import com.example.nav_drawer.PerfilFragment;
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

    TextView textViewCamp;

    Button editProfile;
    String id;

    Intent intent;

    ImageView btnregresar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizarperfildoc);

        editnombre = findViewById(R.id.profileName);
        editnumero = findViewById(R.id.profilenum);
        editdescripcion = findViewById(R.id.profiledescr);
        editProfile = findViewById(R.id.editbutton);
        btnregresar = findViewById(R.id.btnatras);
        textViewCamp = findViewById(R.id.textViewCampos);
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


        //Actualizar los campos del perfil
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String valorEditText = editdescripcion.getText().toString();
                String valorEditTextnombre = editnombre.getText().toString();
                String valorEditTextnumero = editnumero.getText().toString();
                uploadTextToFirestore(userEmail, valorEditText, valorEditTextnombre, valorEditTextnumero);
            }
        });

        //Boton de regresar al perfil
        btnregresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Actualizarperfildoc.this, PerfilFragment.class);
                startActivity(intent);
            }
        });
    }

    public void uploadTextToFirestore(String email, String valorEditText, String valorEditTextnombre, String valorEditTextnumero) {
        try {
            Toast.makeText(Actualizarperfildoc.this, " "+valorEditText + valorEditTextnombre + valorEditTextnumero, Toast.LENGTH_SHORT).show();
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

            if (currentUser != null) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                if (valorEditTextnombre.isEmpty()){
                    textViewCamp.setText("Debe de rellenar los campos vacios");
                    textViewCamp.setVisibility(View.VISIBLE);
                }else {
                    if (valorEditText.isEmpty()) {
                        textViewCamp.setText("Debe de rellenar los campos vacios");
                        textViewCamp.setVisibility(View.VISIBLE);
                    } else {

                        if (valorEditTextnumero.isEmpty() || valorEditTextnumero.length() != 10) {
                            //VALIDACION DE DIGITOS DEL TELEFONO 10
                            textViewCamp.setText("El número de teléfono debe tener 10 dígitos");
                            textViewCamp.setVisibility(View.VISIBLE);
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
                                        .update("nombre", valorEditTextnombre)
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
                                        .update("telefono", valorEditTextnumero)
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
                                Toast.makeText(Actualizarperfildoc.this, "Datos actualizados correctamente", Toast.LENGTH_SHORT).show();

                            } else {
                                Log.e(TAG, "El correo electrónico es nulo o vacío");
                                Toast.makeText(Actualizarperfildoc.this, "El correo electrónico es nulo o vacío", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            textViewCamp.setText("El número de teléfono debe tener 10 dígitos");
                            textViewCamp.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Error: " + e.getMessage());
            Toast.makeText(Actualizarperfildoc.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}