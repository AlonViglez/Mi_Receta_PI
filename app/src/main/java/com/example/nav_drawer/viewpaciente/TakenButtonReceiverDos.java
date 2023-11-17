package com.example.nav_drawer.viewpaciente;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.TimeUnit;

public class TakenButtonReceiverDos extends BroadcastReceiver {
    String nombreMedicamento, totalTomasStr, tomadaStr;
    int totalPastillas;
    int tomada;
    private static boolean buttonEnabled = true; // Variable para rastrear si el botón está habilitado
    private static final int DELAY_MILLIS = 4000; // Retraso en milisegundos (4 segundos)
    @Override
    public void onReceive(Context context, Intent intent) {
        if ("ACTION_TAKEN".equals(intent.getAction())) {
            // Verificar si el botón está habilitado
            if (buttonEnabled) {
                // Desactivar el botón temporalmente
                buttonEnabled = false;
                // Realizar la acción cuando se hace clic en el botón "Tomada"
                String email = intent.getStringExtra("USER_EMAIL");
                String tratamientoID = intent.getStringExtra("TRATAMIENTO_ID");
                Log.d(TAG, "USER_EMAIL: " + email);
                Log.d(TAG, "Tratamiento_id: " + tratamientoID);
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                if (email != null && !email.isEmpty()) {
                    db.collection("tratamientos")
                            .document(tratamientoID)
                            .update("tomada", FieldValue.increment(1))
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    obtenerDetallesTratamiento(context, tratamientoID, email);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error al actualizar campo 'tomada'", e);
                                    // Puedes manejar el error aquí
                                }
                            });
                }

                // Reactivar el botón después de un cierto tiempo (por ejemplo, 2 segundos)
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        buttonEnabled = true;
                    }
                }, DELAY_MILLIS);
            }
        }
    }
    // Método para obtener los detalles del tratamiento
    private void obtenerDetallesTratamiento(Context context, String tratamientoID, String email) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("tratamientos")
                .document(tratamientoID)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            // Obtener los detalles del tratamiento
                            totalPastillas = documentSnapshot.getLong("totalPastillas").intValue();
                            int intervalo = documentSnapshot.getLong("intervalo").intValue();
                            nombreMedicamento = documentSnapshot.getString("medicamento");

                            // Cancelar la notificación
                            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                            notificationManager.cancel(tratamientoID.hashCode());

                            // Si no hemos alcanzado el total de pastillas, programar la próxima notificación
                            tomada = documentSnapshot.getLong("tomada").intValue();
                            if (tomada < totalPastillas) {
                                // Calcular el tiempo para la próxima notificación (en milisegundos)
                                long delayMillis = intervalo * 1000L; // Convertir el intervalo a milisegundos
                                //Convertir int a string
                                totalTomasStr = String.valueOf(totalPastillas);
                                tomadaStr = String.valueOf(tomada);
                                // Programar la próxima notificación
                                scheduleNextNotification(context, tratamientoID, email, nombreMedicamento, delayMillis, totalTomasStr, tomadaStr);
                            } else {
                                // Todas las pastillas han sido tomadas, puedes realizar acciones adicionales si es necesario
                            }
                            // Marcar la medicación como tomada en base de datos
                            Intent inicioIntent = new Intent(context, ViewPacient.class);
                            inicioIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(inicioIntent);
                            Log.d(TAG, "Detalles del tratamiento obtenidos con éxito");
                        } else {
                            Log.d(TAG, "El documento no existe");
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error al obtener detalles del tratamiento", e);
                        // Puedes manejar el error aquí
                    }
                });
    }
    // Método para programar la próxima notificación
    private void scheduleNextNotification(Context context, String tratamientoID, String userEmail, String nombreMedicamento, long delayMillis, String totalTomasStr, String tomadaStr) {
        OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(MyWorkerDos.class)
                .setInputData(new Data.Builder()
                        .putString("tratamientoId", tratamientoID)
                        .putString("userEmail", userEmail)
                        .putString("nombreMedicamento", nombreMedicamento)
                        .putString("tomada", tomadaStr)
                        .putString("totaltomas", totalTomasStr)
                        .build())
                .setInitialDelay(delayMillis, TimeUnit.MILLISECONDS)
                .build();
        WorkManager.getInstance(context).enqueueUniqueWork(tratamientoID, ExistingWorkPolicy.REPLACE, workRequest);
    }
}
