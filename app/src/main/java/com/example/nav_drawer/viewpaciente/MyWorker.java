package com.example.nav_drawer.viewpaciente;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.nav_drawer.R;
import com.example.nav_drawer.Inicio;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MyWorker extends Worker {
    public static final String CHANNEL_ID = "channel_id";
    public static final int NOTIFICATION_ID = 1;
    String tratamientoId;
    String userEmail;
    String nombreMedicamento;
    public MyWorker(
            @NonNull Context context,
            @NonNull WorkerParameters params) {
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork() {
        tratamientoId = getInputData().getString("tratamientoId");
        userEmail = getInputData().getString("userEmail");
        nombreMedicamento = getInputData().getString("nombreMedicamento");
        // Realiza la tarea que deseas aquí
        showNotification(getApplicationContext(), nombreMedicamento, "¿Ya te tomaste tu medicamento?");
        return Result.success(); // Si la tarea se completa con éxito
    }

    private void showNotification(Context context, String title, String message) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Crear un canal de notificación (necesario para Android 8.0 y versiones posteriores)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Channel Name", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }
        // Crear un intent para la acción cuando se toque la notificación
        Intent intent = new Intent(context, Inicio.class); // Reemplaza Inicio.class con la actividad principal de tu aplicación
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Obtener el nombre del paquete de la aplicación
        String packageName = context.getPackageName();
        // Obtener la URI del sonido personalizado
        Uri sound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + packageName + "/" + R.raw.pastillas);

        // Construir la notificación
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.pildora)
                .setContentTitle(title)
                .setContentText(message)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setSound(sound)
                .setOngoing(true); // Hace que la notificación sea persistente

        // Agregar un botón "Tomada"
        Intent takenIntent = new Intent(context, TakenButtonReceiver.class);
        takenIntent.setAction("ACTION_TAKEN");
        takenIntent.putExtra("USER_EMAIL", userEmail);
        takenIntent.putExtra("TRATAMIENTO_ID", tratamientoId);

        PendingIntent takenPendingIntent = PendingIntent.getBroadcast(context, 0, takenIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.addAction(R.drawable.icon_disponibilidad, "Tomada", takenPendingIntent);
        // Mostrar la notificación
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }
}
