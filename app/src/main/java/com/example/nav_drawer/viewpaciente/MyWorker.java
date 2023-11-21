package com.example.nav_drawer.viewpaciente;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
    String totaltomas;
    String tomada;
    public MyWorker(
            @NonNull Context context,
            @NonNull WorkerParameters params) {
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork() {
        // Obtener DATOS
        tratamientoId = getInputData().getString("tratamientoId");
        userEmail = getInputData().getString("userEmail");
        totaltomas = getInputData().getString("tomada");
        tomada = getInputData().getString("totaltomas");
        nombreMedicamento = getInputData().getString("nombreMedicamento");
        // Realiza la tarea que deseas aquí
        showNotification(getApplicationContext(), nombreMedicamento, "¿Ya te tomaste tu medicamento?", tratamientoId);
        return Result.success(); // Si la tarea se completa con éxito
    }
    //Metodo para mostrar notificacion
    private void showNotification(Context context, String title, String message, String tratamientoId) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Crear un canal de notificación (necesario para Android 8.0 y versiones posteriores)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(tratamientoId, "Channel Name", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }
        /* Crear un intent para la acción cuando se toque la notificación
        Intent intent = new Intent(context, Inicio.class); // Reemplaza Inicio.class con la actividad principal de tu aplicación
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        */
        // Obtener el nombre del paquete de la aplicación
        String packageName = context.getPackageName();
        // Obtener la URI del sonido personalizado
        Uri sound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + packageName + "/" + R.raw.pastillas);

        // Construir la notificación
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, tratamientoId)
                .setSmallIcon(R.drawable.pildora)
                .setContentTitle(title)
                .setContentText(message)
                .setContentIntent(null)
                .setAutoCancel(true)
                .setSound(sound)
                .setOngoing(true); // Hace que la notificación sea persistente

        // Agregar un botón "Tomada"
        Intent takenIntent = new Intent(context, TakenButtonReceiver.class);
        takenIntent.setAction("ACTION_TAKEN");
        takenIntent.putExtra("USER_EMAIL", userEmail);
        takenIntent.putExtra("TRATAMIENTO_ID", tratamientoId);

        PendingIntent takenPendingIntent = PendingIntent.getBroadcast(context, tratamientoId.hashCode(), takenIntent, PendingIntent.FLAG_MUTABLE);

        builder.addAction(R.drawable.icon_disponibilidad, "Tomada", takenPendingIntent);
        // Mostrar la notificación
        notificationManager.notify(tratamientoId.hashCode(), builder.build());
    }
}
