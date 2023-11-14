package com.example.nav_drawer.viewpaciente;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.work.impl.utils.ForceStopRunnable;

import com.example.nav_drawer.Inicio;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class TakenButtonReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if ("ACTION_TAKEN".equals(intent.getAction())) {
            // Realiza la acción cuando se hace clic en el botón "Tomada"
            //Obtener el correo electrónico del usuario
            String Email = intent.getStringExtra("USER_EMAIL");
            String tratamientoID = intent.getStringExtra("TRATAMIENTO_ID");
            Log.d(TAG, "USER_EMAIL" + Email);
            Log.d(TAG, "Tratamiento_id " + tratamientoID);
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            if (!Email.isEmpty()) {
                // Llamar a update aquí
                db.collection("tratamientos")
                        .document(tratamientoID)
                        .update("tomada", 1)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // Cancelar la notificación
                                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                                notificationManager.cancel(MyWorker.NOTIFICATION_ID);
                                // Puede ser marcar la medicación como tomada en tu base de datos, etc.
                                Intent inicioIntent = new Intent(context, Inicio.class);
                                inicioIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(inicioIntent);
                                Log.d(TAG, "Campo 'descripcion' actualizado con éxito");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error al actualizar campo 'descripcion'", e);
                                // Puedes manejar el error aquí
                            }
                        });
            }
        }
    }
}
