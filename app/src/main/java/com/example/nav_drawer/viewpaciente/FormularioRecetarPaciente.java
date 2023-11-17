package com.example.nav_drawer.viewpaciente;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;
import static androidx.core.content.ContentProviderCompat.requireContext;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.work.Data;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.nav_drawer.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class FormularioRecetarPaciente extends AppCompatActivity {
    private Handler handler = new Handler();
    int numnotificacion = 0;
    String horainicio;
    String userEmail;
    FirebaseAuth mAuth;
    EditText editMedicamento, editDuracion, editIntervalo, editDosis;
    Button btnmedicamento;
    Button btnMostrarTimePicker;
    TimePicker timePicker;
    String tratamientoId;
    String medicamento,duracionStr,dosisStr,intervaloStr, totalTomasStr, tomada;
    int totalTomas;
    int primeratoma = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_recetar_paciente);
        TextView titleRecetar = findViewById(R.id.titleRecetar);
        editMedicamento = findViewById(R.id.editMedicamento);
        editDuracion = findViewById(R.id.editDuracion);
        editDosis = findViewById(R.id.editDosis);
        editIntervalo = findViewById(R.id.editIntervalo);
        btnmedicamento = findViewById(R.id.btn_registro_medicamento);
        btnMostrarTimePicker = findViewById(R.id.btnMostrarTimePicker);
        timePicker = findViewById(R.id.timePicker);
        mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            userEmail = currentUser.getEmail();
            Toast.makeText(FormularioRecetarPaciente.this, "Usuario autenticado" + userEmail, Toast.LENGTH_SHORT).show();
        } else {
            Log.e(TAG, "Usuario no autenticado");
            Toast.makeText(FormularioRecetarPaciente.this, "Usuario no autenticado", Toast.LENGTH_SHORT).show();
        }
        // Configurar el evento click del botón para mostrar el TimePicker
        btnMostrarTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener la hora actual
                LocalTime currentTime = LocalTime.now();

                // Crear un TimePicker personalizado sin minutos y formato de 12 horas
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        FormularioRecetarPaciente.this,
                        AlertDialog.THEME_HOLO_LIGHT,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                // Actualizar la hora seleccionada en el TimePicker
                                minute = 0;
                                // Puedes realizar cualquier acción que desees con la hora seleccionada
                                // Por ejemplo, mostrarla en un TextView
                                horainicio = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute);
                                // Puedes mostrar la hora seleccionada en un TextView o cualquier otro componente
                                // En este ejemplo, se mostrará en un Toast
                                Toast.makeText(FormularioRecetarPaciente.this, "Hora seleccionada: " + horainicio , Toast.LENGTH_SHORT).show();
                            }
                        },
                        currentTime.getHour(),
                        0, // Establecer minutos en 0
                        true // Mostrar formato de 24 horas
                );
                // Establecer el título del diálogo
                timePickerDialog.setTitle("Primer toma de medicamento:");
                // Mostrar el diálogo
                timePickerDialog.show();
            }
        });
        btnmedicamento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener los valores de los EditText
                medicamento = editMedicamento.getText().toString();
                duracionStr = editDuracion.getText().toString();
                dosisStr = editDosis.getText().toString();
                intervaloStr = editIntervalo.getText().toString();
                // Verificar que los campos no estén vacíos
                if (!medicamento.isEmpty() && !duracionStr.isEmpty() && !dosisStr.isEmpty() && !intervaloStr.isEmpty()) {
                    // Enviar los datos a Firestore
                    //Conversiones
                    int duracion = Integer.parseInt(duracionStr);
                    double dosis = Double.parseDouble(dosisStr);
                    int intervalo = Integer.parseInt(intervaloStr);
                    // Calcular el total de tomas por día
                    totalTomas = calcularTotalTomasPorDia(duracion,horainicio, intervalo);
                    //Convertir totaltomas a string
                    totalTomasStr = String.valueOf(totalTomas);
                    tomada = String.valueOf(primeratoma);
                    enviarDatosFirestore(medicamento, duracion, dosis, intervalo,primeratoma,totalTomas);
                } else {
                    Toast.makeText(FormularioRecetarPaciente.this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void enviarDatosFirestore(String medicamento, Integer duracion, Double dosis, Integer intervalo,Integer primeratoma, Integer totalTomas) {
        // Crear un objeto Map con los datos del tratamiento
        Map<String, Object> tratamiento = new HashMap<>();
        tratamiento.put("medicamento", medicamento);
        tratamiento.put("duracion", duracion);
        tratamiento.put("dosis", dosis);
        tratamiento.put("intervalo", intervalo);
        tratamiento.put("selectime", horainicio);
        tratamiento.put("usuario", userEmail);
        tratamiento.put("tomada", primeratoma);
        tratamiento.put("totalPastillas", totalTomas);

        // Obtener una referencia a la colección "tratamientos" en Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("tratamientos")
                .add(tratamiento) // Utilizar "add" para crear un nuevo documento con un ID automático
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        // Obtener el ID del tratamiento después de agregarlo
                        tratamientoId = documentReference.getId();
                        // Actualizar el documento con el ID
                        documentReference.update("id", tratamientoId);
                        Toast.makeText(FormularioRecetarPaciente.this, "Tratamiento registrado correctamente", Toast.LENGTH_SHORT).show();
                        scheduleNotification(intervalo * 1000, tratamientoId, userEmail,medicamento,tomada,totalTomasStr); // Convertir el intervalo a milisegundos
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(FormularioRecetarPaciente.this, "Error al registrar tratamiento", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "Error al registrar tratamiento", e);
                    }
                });
    }
    /*Método primero para programar la notificación
    private void scheduleNotification(int delayMillis) {
        Toast.makeText(getActivity(), "Tiempo para notificar: " + delayMillis, Toast.LENGTH_SHORT).show();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // LLAMAR AL SERVICIO DE NOTIFICACIONES
                Intent notificationIntent = new Intent(getActivity(), NotificationService.class);
                notificationIntent.setAction("ACTION_SHOW_NOTIFICATION");
                getActivity().startForegroundService(notificationIntent);
            }
        }, delayMillis);
    }*/
    //Segundo metodo para programar la notificacion en segundo plano
    private void scheduleNotification(int delayMillis,String tratamientoId,String userEmail, String medicamento, String tomada, String totalTomasStr) {
        // Crear una tarea única con WorkManager
        numnotificacion += 1;
        if(numnotificacion == 1) {
            OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(MyWorker.class)
                    .setInputData(new Data.Builder()
                            .putString("tratamientoId", tratamientoId)
                            .putString("userEmail", userEmail)
                            .putString("nombreMedicamento", medicamento)
                            .putString("tomada", tomada)
                            .putString("totaltomas", totalTomasStr)
                            .build())
                    .setInitialDelay(delayMillis, TimeUnit.MILLISECONDS)
                    .build();
            // Enviar la tarea a WorkManager con el mismo ID que el tratamientoId
            WorkManager.getInstance(this).enqueueUniqueWork(tratamientoId, ExistingWorkPolicy.REPLACE, workRequest);
        } else if (numnotificacion == 2) {
            OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(MyWorkerDos.class)
                    .setInputData(new Data.Builder()
                            .putString("tratamientoId", tratamientoId)
                            .putString("userEmail", userEmail)
                            .putString("nombreMedicamento", medicamento)
                            .putString("tomada", tomada)
                            .putString("totaltomas", totalTomasStr)
                            .build())
                    .setInitialDelay(delayMillis, TimeUnit.MILLISECONDS)
                    .build();
            // Enviar la tarea a WorkManager con el mismo ID que el tratamientoId
            WorkManager.getInstance(this).enqueueUniqueWork(tratamientoId, ExistingWorkPolicy.REPLACE, workRequest);
        }
    }
    // Método para calcular el total de tomas por día considerando la hora de inicio
    private int calcularTotalTomasPorDia(int duracion, String horainicio, int intervalo) {
        int horasPorDia = 24;
        // Obtener la hora de inicio y convertirla a formato de 24 horas
        String[] horaInicioArray = horainicio.split(":");
        int horaInicio = Integer.parseInt(horaInicioArray[0]);
        // Calcular el total de tomas por día
        int totalTomas = duracion * horasPorDia;
        totalTomas = (totalTomas - horaInicio)/intervalo;
        return totalTomas;
    }
}