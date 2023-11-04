package com.example.nav_drawer.viewAdmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nav_drawer.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.bumptech.glide.Glide;

public class DetallesDoctor extends AppCompatActivity {
    Intent intent;
    String doctorId;
    ImageView backButton;
    ImageView cedulaImageView,ineImageView;
    TextView nombreDoctorTextView,especialidadMedicaTextView,sexoTextView,telefonoTextView;
    ImageButton btnRechazar, btnAceptar;
    String pass,email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_doctor);
        intent = getIntent();//Obtener intent
        doctorId = intent.getStringExtra("doctorId"); //Recibimos el id del fragment
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference doctorCollection = db.collection("doctorpendiente");
        backButton = findViewById(R.id.backButton);
        nombreDoctorTextView = findViewById(R.id.nombreDoctorTextView);
        especialidadMedicaTextView = findViewById(R.id.especialidadMedicaTextView);
        sexoTextView = findViewById(R.id.sexoTextView);
        telefonoTextView = findViewById(R.id.telefonoTextView);
        cedulaImageView = findViewById(R.id.imagenCedulaImageView);
        ineImageView = findViewById(R.id.imagenINEImageView);
        btnRechazar = findViewById(R.id.btnRechazar);
        btnAceptar = findViewById(R.id.btnAceptar);
        // Regresar
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed(); // Simular el comportamiento del botón de retroceso
            }
        });
        //Mostrar los detalles del doctor mediante el id
        doctorCollection.document(doctorId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            // El documento existe, puedes obtener los datos del doctor
                            String nombreDoctor = documentSnapshot.getString("nombre");
                            String especialidadMedica = documentSnapshot.getString("especialidad");
                            String sexo = documentSnapshot.getString("sexo");
                            String telefono = documentSnapshot.getString("telefono");
                            String cedulaImageUrl = documentSnapshot.getString("imagenCedula");
                            String ineImageUrl = documentSnapshot.getString("imagenINE");
                            pass = documentSnapshot.getString("password");
                            email = documentSnapshot.getString("correo");

                            // Actualiza las vistas con los datos del doctor
                            nombreDoctorTextView.setText(nombreDoctor);
                            especialidadMedicaTextView.setText(especialidadMedica);
                            sexoTextView.setText(sexo);
                            telefonoTextView.setText(telefono);

                            // Utiliza Glide para cargar las imágenes de cédula e INE si son URLs
                            if (cedulaImageUrl != null) {
                                Glide.with(DetallesDoctor.this).load(cedulaImageUrl).into(cedulaImageView);
                            }
                            if (ineImageUrl != null) {
                                Glide.with(DetallesDoctor.this).load(ineImageUrl).into(ineImageView);
                            }
                        } else {
                            // Error
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Manejar cualquier error que pueda ocurrir durante la consulta
                    }
                });
        //BOTON RECHAZAR
        btnRechazar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doctorCollection.document(doctorId)
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                AdminPeticiones adminPeticionesFragment = new AdminPeticiones();
                                getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.framepeticiones, adminPeticionesFragment)
                                        .addToBackStack(null)
                                        .commit();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                //ERROR
                            }
                        });
            }
        });
        //BOTON ACEPTAR
        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // REGISTRAR EN AUTENTICACIÓN AL DOCTOR
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, pass)
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                // PASAR SUS DATOS A LA NUEVA TABLA
                                doctorCollection.document(doctorId) // OBTENGO TODOS SUS DATOS
                                        .get()
                                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                            @Override
                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                if (documentSnapshot.exists()) {
                                                    // INSERTO LOS DATOS EN LA NUEVA TABLA
                                                    db.collection("altadoctores").document(doctorId)
                                                            .set(documentSnapshot.getData())
                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {
                                                                    // CORRECTO
                                                                    // ELIMINO LOS DATOS
                                                                    doctorCollection.document(doctorId)
                                                                            .delete()
                                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                @Override
                                                                                public void onSuccess(Void aVoid) {
                                                                                    AdminPeticiones adminPeticionesFragment = new AdminPeticiones();
                                                                                    getSupportFragmentManager().beginTransaction()
                                                                                            .replace(R.id.framepeticiones, adminPeticionesFragment)
                                                                                            .addToBackStack(null)
                                                                                            .commit();
                                                                                }
                                                                            })
                                                                            .addOnFailureListener(new OnFailureListener() {
                                                                                @Override
                                                                                public void onFailure(@NonNull Exception e) {
                                                                                    // ERROR AL ELIMINAR DATOS
                                                                                }
                                                                            });
                                                                }
                                                            })
                                                            .addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                    // ERROR DURANTE LA INSERCIÓN EN LA NUEVA TABLA
                                                                }
                                                            });
                                                } else {
                                                    // El doc no existe
                                                }
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                // MANEJAR ERROR AL OBTENER DATOS
                                            }
                                        });
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // MANEJAR ERROR DE REGISTRO EN AUTENTICACIÓN
                            }
                        });
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // Limpiar los TextView
        nombreDoctorTextView.setText("");
        especialidadMedicaTextView.setText("");
        sexoTextView.setText("");
        telefonoTextView.setText("");
        cedulaImageView.setImageResource(android.R.color.transparent);
        ineImageView.setImageResource(android.R.color.transparent);
    }
}