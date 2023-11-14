package com.example.nav_drawer.viewpaciente;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;

import com.example.nav_drawer.Inicio;
import com.example.nav_drawer.R;

public class NotificationService extends Service {
    private static final String CHANNEL_ID = "channel_id";
    private static final int NOTIFICATION_ID = 1;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.getAction() != null && intent.getAction().equals("ACTION_SHOW_NOTIFICATION")) {
            showNotification(getApplicationContext(), "¡Hora de tu medicamento!", "Tienes que tomarte tu medicamento");
            // Aquí puedes agregar lógica adicional si es necesario
            // Detener el servicio después de mostrar la notificación
            stopSelf();
        }
        return START_NOT_STICKY;
    }

    private void showNotification(Context context, String title, String message) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Crear un canal de notificación (necesario para Android 8.0 y versiones posteriores)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Channel Name", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        // Crear un intent para la acción cuando se toque la notificación
        Intent intent = new Intent(context, Inicio.class); // Reemplaza YourMainActivity.class con la actividad principal de tu aplicación
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Obtener la URI del sonido predeterminado de notificación
        //Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        // Obtener la URI del sonido personalizado
        Uri sound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getPackageName() + "/" + R.raw.pastillas);  // Reemplaza R.raw.custom_sound con el ID de tu archivo de sonido en la carpeta res/raw

        // Construir la notificación
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.pildora)
                .setContentTitle(title)
                .setContentText(message)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setSound(sound);  // Configurar el sonido predeterminado aquí

        // Mostrar la notificación
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }
}