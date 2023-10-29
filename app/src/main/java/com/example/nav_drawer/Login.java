package com.example.nav_drawer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nav_drawer.viewpaciente.ViewPacient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    TextInputEditText editTextEmail,editTextPassword;
    Button buttonLogin;
    FirebaseAuth mAuth;
    ProgressBar progressBar;
    TextView textView;
    String admin = "jvilla14@ucol.mx"; //ADMIN USER

    @Override
    public void onStart() { //AL INICIAR LA APP SI ESTA UNA CUENTA ACTIVA TE REDIRECCIONA AUTOMATICAMENTE
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            String userEmail = currentUser.getEmail();
            if(userEmail != null && userEmail.equals(admin)){
                Intent adminCheck = new Intent(getApplicationContext(), ViewAdmin.class); //VISTA ADMIN
                startActivity(adminCheck);
                finish();
            }else{
                Intent userCheck = new Intent(getApplicationContext(), ViewPacient.class); //VISTA PACIENTE
                startActivity(userCheck);
                finish();
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
        textView = findViewById(R.id.nuevoregistro);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegistroDoctor.class);//Mandar al activity para registrar si es doctor o usuario(IdentifyRegister)
                startActivity(intent);
                finish();
            }
        });
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressBar.setVisibility(View.VISIBLE);
                String email, password;
                email = String.valueOf(editTextEmail.getText());
                password = String.valueOf(editTextPassword.getText());

                if (TextUtils.isEmpty(email)){
                    Toast.makeText(Login.this, "Enter email", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)){
                    Toast.makeText(Login.this, "Enter password", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);
                                if (task.isSuccessful() && email.equals(admin)) { //USUARIO ADMIN
                                    Toast.makeText(getApplicationContext(), "Inicio de Sesion Exitoso", Toast.LENGTH_SHORT).show();;
                                    Intent ad = new Intent(getApplicationContext(),ViewAdmin.class);
                                    startActivity(ad);
                                    finish();
                                }else if(task.isSuccessful()){ //USUARIO NORMAL
                                    Toast.makeText(getApplicationContext(), "Inicio de Sesion Exitoso", Toast.LENGTH_SHORT).show();;
                                    Intent intent = new Intent(getApplicationContext(),ViewPacient.class);
                                    startActivity(intent);
                                    finish();
                                }else { //ERROR
                                    Toast.makeText(Login.this, "Oops, hubo un error de Autenticacion.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
}