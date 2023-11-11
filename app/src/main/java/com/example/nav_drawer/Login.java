package com.example.nav_drawer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nav_drawer.viewAdmin.ViewAdministrador;
import com.example.nav_drawer.viewpaciente.ViewPacient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Login extends AppCompatActivity {

    TextInputEditText editTextEmail,editTextPassword;
    TextView olvido_contrasena;
    Button buttonLogin;
    FirebaseAuth mAuth;
    ProgressBar progressBar;
    TextView identify;
    String admin = "jvilla14@ucol.mx"; //ADMIN USER
    String email, password;

    @Override
    public void onStart() { //AL INICIAR LA APP SI ESTA UNA CUENTA ACTIVA TE REDIRECCIONA AUTOMATICAMENTE
        super.onStart();
        // Verificar la conexión a Internet antes de intentar la autenticación en onStart
        if (!isNetworkAvailable()) {
            Toast.makeText(Login.this, "No hay conexión a Internet", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userEmail = currentUser.getEmail();
            if (userEmail != null) {
                if (userEmail.equals(admin)) {
                    // El usuario es el administrador
                    Toast.makeText(Login.this, "Bienvenido de nuevo", Toast.LENGTH_SHORT).show();
                    Intent adminCheck = new Intent(getApplicationContext(), ViewAdministrador.class);
                    startActivity(adminCheck);
                    finish();
                } else {
                    // Verifica si el usuario es un doctor
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    CollectionReference doctorsRef = db.collection("altadoctores");
                    Query query = doctorsRef.whereEqualTo("correo", userEmail);
                    query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                if (!task.getResult().isEmpty()) {
                                    // El usuario es un doctor
                                    Toast.makeText(Login.this, "Bienvenido de nuevo", Toast.LENGTH_SHORT).show();
                                    Intent doctorIntent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(doctorIntent);
                                    finish();
                                } else {
                                    // El usuario no es un doctor, por lo tanto, se asume que es un paciente
                                    Toast.makeText(Login.this, "Bienvenido de nuevo", Toast.LENGTH_SHORT).show();
                                    Intent userCheck = new Intent(getApplicationContext(), ViewPacient.class);
                                    startActivity(userCheck);
                                    finish();
                                }
                            } else {
                                // Handle error
                                Toast.makeText(getApplicationContext(), "Error al verificar el tipo de usuario", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);
        buttonLogin = findViewById(R.id.btn_login);
        progressBar = findViewById(R.id.progressBar);
        identify = findViewById(R.id.nuevoregistro);
        olvido_contrasena = findViewById(R.id.olvido_contrasena);
        //BOTON DE REGISTRAR
        identify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), IdentifyRegister.class);//Mandar al activity para registrar si es doctor o usuario(IdentifyRegister)
                startActivity(intent);
                finish();
            }
        });
        //LOGIN BOTON
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                email = String.valueOf(editTextEmail.getText());
                password = String.valueOf(editTextPassword.getText());

                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                    Toast.makeText(Login.this, "Enter email and password", Toast.LENGTH_SHORT).show();
                    return;
                }
                //Llamar a funcion para hashear contraseña
                String hashedPassword = hashPassword(password);
                mAuth.signInWithEmailAndPassword(email, hashedPassword)
                        .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    // Verificar si el usuario es un administrador
                                    if (email.equalsIgnoreCase(admin)) {
                                        // El usuario es un administrador
                                        Toast.makeText(getApplicationContext(), "Inicio de Sesion Exitoso (Admin)", Toast.LENGTH_SHORT).show();
                                        Intent adminIntent = new Intent(getApplicationContext(), ViewAdministrador.class);
                                        startActivity(adminIntent);
                                        finish();
                                    } else {
                                        // Verificar si el usuario es un doctor
                                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                                        CollectionReference doctorsRef = db.collection("altadoctores");
                                        Query query = doctorsRef.whereEqualTo("correo", email.toLowerCase());
                                        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    if (!task.getResult().isEmpty()) {
                                                        // El usuario es un doctor
                                                        Toast.makeText(getApplicationContext(), "Inicio de Sesion Exitoso (Doctor)", Toast.LENGTH_SHORT).show();
                                                        Intent doctorIntent = new Intent(getApplicationContext(), MainActivity.class);
                                                        startActivity(doctorIntent);
                                                        finish();
                                                    } else {
                                                        // El usuario no es un doctor, por lo tanto, se asume que es un paciente
                                                        Toast.makeText(getApplicationContext(), "Inicio de Sesion Exitoso (Paciente)", Toast.LENGTH_SHORT).show();
                                                        Intent patientIntent = new Intent(getApplicationContext(), ViewPacient.class);
                                                        startActivity(patientIntent);
                                                        finish();
                                                    }
                                                } else {
                                                    // Mostrar error
                                                    Toast.makeText(getApplicationContext(), "Error al verificar el tipo de usuario", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }
                                } else {
                                    //LOGUEO CON EL OTRO TIPO DE HASH(SI ES QUE LE DIO A RECUPERAR CONTRASEÑA)
                                    mAuth.signInWithEmailAndPassword(email, password)
                                            .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                                                @Override
                                                public void onComplete(@NonNull Task<AuthResult> task) {
                                                    progressBar.setVisibility(View.GONE);
                                                    if (task.isSuccessful()) {
                                                        // Verificar si el usuario es un administrador
                                                        if (email.equalsIgnoreCase(admin)) {
                                                            // El usuario es un administrador
                                                            Toast.makeText(getApplicationContext(), "Inicio de Sesion Exitoso (Admin)", Toast.LENGTH_SHORT).show();
                                                            Intent adminIntent = new Intent(getApplicationContext(), ViewAdministrador.class);
                                                            startActivity(adminIntent);
                                                            finish();
                                                        } else {
                                                            // Verificar si el usuario es un doctor
                                                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                                                            CollectionReference doctorsRef = db.collection("altadoctores");
                                                            Query query = doctorsRef.whereEqualTo("correo", email.toLowerCase());
                                                            query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                    if (task.isSuccessful()) {
                                                                        if (!task.getResult().isEmpty()) {
                                                                            // El usuario es un doctor
                                                                            Toast.makeText(getApplicationContext(), "Inicio de Sesion Exitoso (Doctor)", Toast.LENGTH_SHORT).show();
                                                                            Intent doctorIntent = new Intent(getApplicationContext(), MainActivity.class);
                                                                            startActivity(doctorIntent);
                                                                            finish();
                                                                        } else {
                                                                            // El usuario no es un doctor, por lo tanto, se asume que es un paciente
                                                                            Toast.makeText(getApplicationContext(), "Inicio de Sesion Exitoso (Paciente)", Toast.LENGTH_SHORT).show();
                                                                            Intent patientIntent = new Intent(getApplicationContext(), ViewPacient.class);
                                                                            startActivity(patientIntent);
                                                                            finish();
                                                                        }
                                                                    } else {
                                                                        // Mostrar error
                                                                        Toast.makeText(getApplicationContext(), "Error al verificar el tipo de usuario", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                }
                                                            });
                                                        }
                                                    } else {
                                                        Toast.makeText(Login.this, "Oops, hubo un error de Autenticacion.", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                    //Toast.makeText(Login.this, "Oops, hubo un error de Autenticacion.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
        //RECUPERAR CONTRASEÑA (TEXTVIEW)
        olvido_contrasena.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showResetPasswordDialog();
            }//MANDO A LA FUNCION
        });
    }
    //FUNCION PARA RECUPERAR CONTRASEÑA
    private void showResetPasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_reset_password, null);
        EditText resetEmail = dialogView.findViewById(R.id.resetEmail);
        Button btnCancel = dialogView.findViewById(R.id.btnCancel);
        Button btnEnviar = dialogView.findViewById(R.id.btnEnviar);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cerrar el cuadro de diálogo cuando se hace clic en "Cancelar"
                dialog.dismiss();
            }
        });
        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = resetEmail.getText().toString();
                if (!email.isEmpty()) {
                    FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        // Envío de correo electrónico de restablecimiento de contraseña exitoso
                                        Toast.makeText(getApplicationContext(), "Se ha enviado un correo electrónico de restablecimiento de contraseña a " + email, Toast.LENGTH_SHORT).show();
                                    } else {
                                        // Si hay un error en el envío del correo electrónico de restablecimiento de contraseña
                                        Toast.makeText(getApplicationContext(), "No se pudo enviar el correo electrónico de restablecimiento de contraseña. Verifica la dirección de correo electrónico.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                    // Cerrar el cuadro de diálogo después de enviar el correo electrónico
                    dialog.dismiss();
                } else {
                    Toast.makeText(getApplicationContext(), "Ingresa tu dirección de correo electrónico", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setView(dialogView);
        dialog.show();
    }
    //HASHEAR CONTRASEÑA
    private String hashPassword(String password) {
        try {
            // Obtén una instancia de MessageDigest para SHA-256
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            // Convierte la contraseña en bytes y hashea
            byte[] hashBytes = md.digest(password.getBytes(StandardCharsets.UTF_8));

            // Convierte el hash en una representación hexadecimal
            BigInteger bigInt = new BigInteger(1, hashBytes);
            String hashedPassword = bigInt.toString(16);
            return hashedPassword;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            // Manejar la excepción
            return null;
        }
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
        return false;
    }

}