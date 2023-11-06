package com.example.nav_drawer;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nav_drawer.viewpaciente.ViewPacient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import android.app.Application;
import com.google.firebase.FirebaseApp;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import android.app.DatePickerDialog;
import android.widget.DatePicker;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class Registro extends AppCompatActivity {
    FirebaseFirestore mFirestore;
    FirebaseAuth mAuth;
    TextInputEditText editTextEmail,editTextPassword,editNombre,editRepetirPass;
    LinearLayout linearLayoutSexo;
    RadioButton radioMasculino, radioFemenino, radioOtros;
    RadioGroup sexoRadioGroup;
    EditText editFecha;
    Button buttonReg;
    ProgressBar progressBar;
    TextView textView;
    String sexo = "";
    String hashedPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        FirebaseApp.initializeApp(this);
        mFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBarr);
        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);
        editFecha = findViewById(R.id.fecha);
        editNombre = findViewById(R.id.nombre);
        buttonReg = findViewById(R.id.btn_registro);
        textView = findViewById(R.id.loginNow);
        editRepetirPass = findViewById(R.id.repetirPassword);
        linearLayoutSexo = findViewById(R.id.linearLayoutSexo);
        radioMasculino = findViewById(R.id.radioMasculino);
        radioFemenino = findViewById(R.id.radioFemenino);
        radioOtros = findViewById(R.id.radioOtros);
        sexoRadioGroup = findViewById(R.id.sexoRadioGroup);
        //SELECCIONAR SEXO
        sexoRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // Verificar cuál RadioButton se ha seleccionado
                if(checkedId == R.id.radioFemenino){
                    sexo = "Femenino";
                } else if (checkedId == R.id.radioMasculino) {
                    sexo = "Masculino";
                } else if (checkedId == R.id.radioOtros) {
                    sexo = "Otros";
                }
            }
        });
        //MOSTRAR PICKER DE CALENDARIO EN FECHA DE NACIMIENTO
        editFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarDatePickerDialog();
            }
        });
        //FUNCIONALIDAD DEL BOTON REGISTRAR
        buttonReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();
                String nombre = editNombre.getText().toString().trim();  // Agrega el nombre obtenido
                String fechaNacimiento = editFecha.getText().toString().trim();  // Agrega la fecha de nacimiento obtenida
                String repass = editRepetirPass.getText().toString().trim();
                TextView textViewErrorPass = findViewById(R.id.textViewErrorPassword);
                TextView textViewErrorPassRep = findViewById(R.id.textViewErrorRepetirPassword);
                TextView textViewCamp = findViewById(R.id.textViewCampos);
                //VALIDACIONES DE QUE NO ESTEN VACIOS LOS CAMPOS
                if (nombre.isEmpty() && email.isEmpty() && password.isEmpty() && fechaNacimiento.isEmpty() && sexo.isEmpty() && repass.isEmpty()) {
                    //Toast.makeText(Registro.this, "Complete los datos", Toast.LENGTH_SHORT).show();
                    textViewCamp.setText("Complete todos los campos");
                    textViewCamp.setVisibility(View.VISIBLE);
                } else if (!password.equals(repass)) {
                    // Contraseñas no coinciden
                    //Toast.makeText(Registro.this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                    textViewCamp.setVisibility(View.GONE);
                    textViewErrorPass.setText("Las contraseñas no coinciden");
                    textViewErrorPassRep.setText("Las contraseñas no coinciden");
                    textViewErrorPass.setVisibility(View.VISIBLE);
                    textViewErrorPassRep.setVisibility(View.VISIBLE);
                    editTextPassword.setText("");  // Limpiar contraseñas
                    editRepetirPass.setText("");  // Limpiar contraseñas repetidas
                }else {
                    textViewCamp.setVisibility(View.GONE);  // Ocultar mensaje de error si estaba visible
                    textViewErrorPass.setVisibility(View.GONE);
                    textViewErrorPassRep.setVisibility(View.GONE);
                    hashedPassword = hashPassword(password);
                    registerUser(nombre, email, hashedPassword, fechaNacimiento, sexo);
                }
            }
        });
    }

    //VALIDACION DE FECHA DE NACIMIENTO
    private boolean validarEdad(Date fechaNacimiento) {
        // Obtiene la fecha actual
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -18); // Resta 18 años para obtener la fecha mínima de nacimiento

        // Compara la fecha de nacimiento con la fecha actual - 18 años
        return !fechaNacimiento.after(calendar.getTime());
    }
    //FUNCION PARA MOSTRAR EL CALENDARIO
    private void mostrarDatePickerDialog() {
        // Obtiene la fecha actual
        Calendar calendar = Calendar.getInstance();

        // Crea un DatePickerDialog y establece la fecha actual como fecha predeterminada
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar selectedDate = Calendar.getInstance();
                        selectedDate.set(Calendar.YEAR, year);
                        selectedDate.set(Calendar.MONTH, monthOfYear);
                        selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        // Formatea la fecha seleccionada y la muestra en el EditText
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy", Locale.getDefault());
                        String selectedDateStr = dateFormat.format(selectedDate.getTime());

                        try {
                            // Convierte la fecha seleccionada a Date para validar la edad
                            Date fechaNacimiento = dateFormat.parse(selectedDateStr);

                            // Valida la edad del usuario
                            if (validarEdad(fechaNacimiento)) {
                                editFecha.setText(selectedDateStr);
                            } else {
                                Toast.makeText(Registro.this, "Debe ser mayor de 18 años para registrarse.", Toast.LENGTH_SHORT).show();
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                            Toast.makeText(Registro.this, "Error al procesar la fecha.", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));

        // Muestra el DatePickerDialog
        datePickerDialog.show();
    }
    //Autenticacion
    private void registerUser(String nombre, String email, String hashedPassword, String fechaNacimiento, String sexo) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_politicas_privacidad_usuario, null);
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
                mAuth.createUserWithEmailAndPassword(email, hashedPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Registro exitoso, ahora guarda los datos en Firestore
                            saveUserDataToFirestore(nombre, email, hashedPassword, fechaNacimiento, sexo);
                        } else {
                            // Error al registrar al usuario
                            Toast.makeText(Registro.this, "Error al registrar: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        builder.setView(dialogView);
        dialog.show();
    }
    //GUARDAR DATOS EN FIRESTORE
    private void saveUserDataToFirestore(String nombre, String email, String hashedPassword, String fechaNacimiento, String sexo) {
        if (hashedPassword != null) {
            Map<String, Object> userData = new HashMap<>();
            userData.put("id", null);
            userData.put("nombre", nombre);
            userData.put("correo", email);
            userData.put("password", hashedPassword);
            long timestamp = obtenerTimestamp(fechaNacimiento);
            userData.put("fechanac", timestamp);
            userData.put("sexo", sexo);

            mFirestore.collection("users")
                    .add(userData)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            // Usuario registrado con éxito en Firebase y datos guardados en Firestore
                            String documentId = documentReference.getId(); // Obtener el ID generado por Firebase

                            // Actualizar el campo "id" con el valor del ID generado
                            mFirestore.collection("users").document(documentId)
                                    .update("id", documentId)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            // Campo "id" actualizado con éxito
                                            finish();
                                            startActivity(new Intent(Registro.this, ViewPacient.class));
                                            Toast.makeText(Registro.this, "Usuario registrado con éxito", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            // Error al actualizar el campo "id"
                                            Log.e("Registro", "Error al actualizar el campo 'id'", e);
                                            Toast.makeText(Registro.this, "Error al actualizar el campo 'id'", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Error al guardar en Firestore
                            Log.e("Registro", "Error al guardar en Firestore", e);
                            Toast.makeText(Registro.this, "Error al guardar en Firestore", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
    //FUNCION PARA CAMBIAR LA FECHA DE NACIMIENTO STRING A FORMATO TIMESTAMP
    private long obtenerTimestamp(String fecha) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy", Locale.getDefault());
        try {
            Date date = dateFormat.parse(fecha);
            return date.getTime();  // Obtener el timestamp en milisegundos
        } catch (ParseException e) {
            e.printStackTrace();
            return -1;  // Devolver -1 si hay un error en el formato de la fecha
        }
    }
    //FUNCION PARA HASHEAR CONTRASEÑA
    private String hashPassword(String password) {
        try {
            // Obtener una instancia de MessageDigest para SHA-256
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            // Convierte la contraseña en bytes y hashea
            byte[] hashBytes = md.digest(password.getBytes(StandardCharsets.UTF_8));
            // Convierto el hash en una representación hexadecimal
            BigInteger bigInt = new BigInteger(1, hashBytes);
            String hashedPassword = bigInt.toString(16);
            return hashedPassword;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            // Manejar la excepción
            return null;
        }
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }
}