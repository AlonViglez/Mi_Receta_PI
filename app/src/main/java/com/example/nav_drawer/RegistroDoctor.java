package com.example.nav_drawer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;
import android.os.Bundle;

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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

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
import java.util.UUID;

public class RegistroDoctor extends AppCompatActivity {
    FirebaseFirestore mFirestore;
    FirebaseAuth mAuth;
    TextInputEditText editTextEmail,editTextPassword,editNombre,editRepetirPass, editTelefono;
    LinearLayout linearLayoutSexo;
    RadioButton radioMasculino, radioFemenino, radioOtros;
    EditText editFecha;
    Button buttonReg;
    ProgressBar progressBar;
    TextView textView;
    String sexo = "";
    RadioGroup sexoRadioGroup;
    Button btnSubirINE,btnSubirCedula;
    ImageView imagenINE,imagenCedula;
    Spinner especialidadMedicaSpinner;
    TextView textViewErrorPass,textViewErrorPassRep,textViewCamp;
    private static final int PICK_IMAGE = 1;
    private boolean subirINEActivo = true;
    private Uri imagenINEUri = null;
    private Uri imagenCedulaUri = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_doctor);

        // Agregar esta parte para manejar la selección de imágenes
        btnSubirINE = findViewById(R.id.btnSubirINE);
        imagenINE = findViewById(R.id.imagenINE);
        btnSubirCedula = findViewById(R.id.btnSubirCedula);
        imagenCedula = findViewById(R.id.imagenCedula);
        //FIREBASE
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
        editTelefono = findViewById(R.id.telefonoCelular);
        linearLayoutSexo = findViewById(R.id.linearLayoutSexo);
        radioMasculino = findViewById(R.id.radioMasculino);
        radioFemenino = findViewById(R.id.radioFemenino);
        radioOtros = findViewById(R.id.radioOtros);
        sexoRadioGroup = findViewById(R.id.sexoRadioGroup);
        especialidadMedicaSpinner = findViewById(R.id.especialidadMedicaSpinner);
        textViewErrorPass = findViewById(R.id.textViewErrorPassword);
        textViewErrorPassRep = findViewById(R.id.textViewErrorRepetirPassword);
        textViewCamp = findViewById(R.id.textViewCampos);
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
        //SUBIR INE
        btnSubirINE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                subirINEActivo = true;
                seleccionarImagen();
            }
        });
        //SUBIR CEDULA
        btnSubirCedula.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                subirINEActivo = false;
                seleccionarImagen();
            }
        });
        //FUNCIONALIDAD DEL BOTON REGISTRAR
        buttonReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmail.getText().toString().trim(); //TRIM() elimina los espacios en blanco en ambos extremos del string
                String password = editTextPassword.getText().toString().trim();
                String nombre = editNombre.getText().toString().trim();  // Agrega el nombre obtenido
                String fechaNacimiento = editFecha.getText().toString().trim();  // Agrega la fecha de nacimiento obtenida
                String repass = editRepetirPass.getText().toString().trim();
                String especialidadSeleccionada = especialidadMedicaSpinner.getSelectedItem().toString(); //Especialidad medica seleccionada
                String telefono = editTelefono.getText().toString().trim();
                //VALIDACIONES DE QUE NO ESTEN VACIOS LOS CAMPOS
                if (nombre.isEmpty() && email.isEmpty() && password.isEmpty() && fechaNacimiento.isEmpty() && sexo.isEmpty() && repass.isEmpty() && especialidadSeleccionada.isEmpty() && telefono.isEmpty() /*&& imagenINEUri != null && imagenCedulaUri != null*/) {
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
                    saveUserDataToFirestore(nombre, email, password, fechaNacimiento, sexo, especialidadSeleccionada,telefono,imagenINEUri,imagenCedulaUri);//Guardar datos en Firestore
                }
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {//SUBIR IMAGEN INE Y CEDULA
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == PICK_IMAGE) {
                Uri imageUri = data.getData(); //URL de la imagen
                if (subirINEActivo) {
                    // La imagen es para la INE
                    imagenINEUri = imageUri; // Guardar la Uri de la imagen en la variable
                    imagenINE.setImageURI(imageUri);
                    imagenINE.setVisibility(View.VISIBLE);
                } else {
                    // La imagen es para la cédula profesional
                    imagenCedulaUri = imageUri; // Guardar la Uri de la imagen en la variable
                    imagenCedula.setImageURI(imageUri);
                    imagenCedula.setVisibility(View.VISIBLE);
                }
            }
        }
    }
    public void seleccionarImagen() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE);
    }

    //VALIDACION DE FECHA DE NACIMIENTO
    private boolean validarEdad(Date fechaNacimiento) {
        // Obtener la fecha actual
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -18); // Resta 18 años para obtener la fecha mínima de nacimiento

        // Comparar la fecha de nacimiento con la fecha actual - 18 años
        return !fechaNacimiento.after(calendar.getTime());
    }
    //FUNCION PARA MOSTRAR EL CALENDARIO
    private void mostrarDatePickerDialog() {
        // Obtener la fecha actual
        Calendar calendar = Calendar.getInstance();

        // Crear un DatePickerDialog y establece la fecha actual como fecha predeterminada
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar selectedDate = Calendar.getInstance();
                        selectedDate.set(Calendar.YEAR, year);
                        selectedDate.set(Calendar.MONTH, monthOfYear);
                        selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        // Formatear la fecha seleccionada y la muestra en el EditText
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy", Locale.getDefault());
                        String selectedDateStr = dateFormat.format(selectedDate.getTime());

                        try {
                            // Convertir la fecha seleccionada a Date para validar la edad
                            Date fechaNacimiento = dateFormat.parse(selectedDateStr);

                            // Validar la edad del usuario
                            if (validarEdad(fechaNacimiento)) {
                                editFecha.setText(selectedDateStr);
                            } else {
                                Toast.makeText(RegistroDoctor.this, "Debe ser mayor de 18 años para registrarse.", Toast.LENGTH_SHORT).show();
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                            Toast.makeText(RegistroDoctor.this, "Error al procesar la fecha.", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));

        // Mostrar el DatePickerDialog
        datePickerDialog.show();
    }
    //GUARDAR DATOS EN FIRESTORE
    private void saveUserDataToFirestore(String nombre, String email, String password, String fechaNacimiento, String sexo, String especialidadSeleccionada, String telefono, Uri imagenINEUri, Uri imagenCedulaUri) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_politicas_privacidad_doctor, null);
        Button btnCancel = dialogView.findViewById(R.id.btnCanceldoc);
        Button btnEnviar = dialogView.findViewById(R.id.btnEnviardoc);
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
                // Subir las imágenes a Firebase Storage y obtén las URL de descarga
                uploadImagesToStorage(imagenINEUri, imagenCedulaUri, new OnImagesUploadedListener() {
                    @Override
                    public void onImagesUploaded(String imagenINEUrl, String imagenCedulaUrl) {
                        // Guardar en firestore ya que tengo las URL de las imagenes
                        String hashedPassword = hashPassword(password);
                        if (hashedPassword != null) {
                            Map<String, Object> userData = new HashMap<>();
                            userData.put("id", null); // Inicializar el campo "id" con null
                            userData.put("nombre", nombre);
                            userData.put("correo", email);
                            userData.put("password", hashedPassword);
                            long timestamp = obtenerTimestamp(fechaNacimiento);
                            userData.put("fechanac", timestamp);
                            userData.put("sexo", sexo);
                            userData.put("telefono", telefono);
                            userData.put("especialidad", especialidadSeleccionada);
                            userData.put("imagenINE", imagenINEUrl);
                            userData.put("imagenCedula", imagenCedulaUrl);

                            mFirestore.collection("doctorpendiente")
                                    .add(userData)
                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            // Usuario registrado con éxito en Firebase y datos guardados en Firestore
                                            String documentId = documentReference.getId(); // Obtén el ID generado por Firebase

                                            // Actualiza el campo "id" con el valor del ID generado
                                            mFirestore.collection("doctorpendiente").document(documentId)
                                                    .update("id", documentId)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            // Campo "id" actualizado con éxito
                                                            finish();
                                                            startActivity(new Intent(RegistroDoctor.this, MainActivity.class));
                                                            Toast.makeText(RegistroDoctor.this, "Usuario registrado con éxito", Toast.LENGTH_SHORT).show();
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            // Error al actualizar el campo "id"
                                                            Log.e("Registro", "Error al actualizar el campo 'id'", e);
                                                            Toast.makeText(RegistroDoctor.this, "Error al actualizar el campo 'id'", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            // Error al guardar en Firestore
                                            Log.e("Registro", "Error al guardar en Firestore", e);
                                            Toast.makeText(RegistroDoctor.this, "Error al guardar en Firestore", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    }
                });
            }
        });
        builder.setView(dialogView);
        dialog.show();
    }
    //FUNCION PARA CAMBIAR LA FECHA DE NACIMIENTO STRING A FORMATO TIMESTAMP
    private long obtenerTimestamp(String fecha) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy", Locale.getDefault());
        try {
            Date date = dateFormat.parse(fecha);
            return date.getTime();  // Obtiene el timestamp en milisegundos
        } catch (ParseException e) {
            e.printStackTrace();
            return -1;  // Devuelve -1 si hay un error en el formato de la fecha
        }
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
    // Interfaz para manejar las URL de las imágenes una vez subidas a Firebase Storage
    interface OnImagesUploadedListener {
        void onImagesUploaded(String imagenINEUrl, String imagenCedulaUrl);
    }

    // Subir imágenes a Firebase Storage
    private void uploadImagesToStorage(Uri imagenINEUri, Uri imagenCedulaUri, OnImagesUploadedListener listener) {
        // Sube la imagen de la INE
        String imagenINEPath = "images/ine/" + UUID.randomUUID() + ".jpg";
        StorageReference imagenINERef = FirebaseStorage.getInstance().getReference(imagenINEPath);
        imagenINERef.putFile(imagenINEUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Obtiene la URL de descarga de la imagen de la INE
                        imagenINERef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String imagenINEUrl = uri.toString();

                                // Sube la imagen de la cédula
                                String imagenCedulaPath = "images/cedula/" + UUID.randomUUID() + ".jpg";
                                StorageReference imagenCedulaRef = FirebaseStorage.getInstance().getReference(imagenCedulaPath);
                                imagenCedulaRef.putFile(imagenCedulaUri)
                                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                // Obtiene la URL de descarga de la imagen de la cédula
                                                imagenCedulaRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uri) {
                                                        String imagenCedulaUrl = uri.toString();

                                                        // Llama al listener con las URL de las imágenes
                                                        listener.onImagesUploaded(imagenINEUrl, imagenCedulaUrl);
                                                    }
                                                });
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                // Error al subir la imagen de la cédula
                                                Log.e("Registro", "Error al subir la imagen de la cédula", e);
                                                Toast.makeText(RegistroDoctor.this, "Error al subir la imagen de la cédula", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Error al subir la imagen de la INE
                        Log.e("Registro", "Error al subir la imagen de la INE", e);
                        Toast.makeText(RegistroDoctor.this, "Error al subir la imagen de la INE", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }
}
