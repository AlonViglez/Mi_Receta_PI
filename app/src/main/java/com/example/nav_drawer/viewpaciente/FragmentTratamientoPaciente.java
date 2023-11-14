package com.example.nav_drawer.viewpaciente;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.lang.reflect.Field;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentTratamientoPaciente#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentTratamientoPaciente extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Handler handler = new Handler();
    String horainicio;
    String userEmail;
    FirebaseAuth mAuth;
    EditText editMedicamento, editDuracion, editIntervalo, editDosis;
    Button btnmedicamento;
    Button btnMostrarTimePicker;
    TimePicker timePicker;
    public FragmentTratamientoPaciente() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentTratamientoPaciente.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentTratamientoPaciente newInstance(String param1, String param2) {
        FragmentTratamientoPaciente fragment = new FragmentTratamientoPaciente();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tratamiento_paciente, container, false);
        TextView titleRecetar = view.findViewById(R.id.titleRecetar);
        editMedicamento = view.findViewById(R.id.editMedicamento);
        editDuracion = view.findViewById(R.id.editDuracion);
        editDosis = view.findViewById(R.id.editDosis);
        editIntervalo = view.findViewById(R.id.editIntervalo);
        btnmedicamento = view.findViewById(R.id.btn_registro_medicamento);
        btnMostrarTimePicker = view.findViewById(R.id.btnMostrarTimePicker);
        timePicker = view.findViewById(R.id.timePicker);
        mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            userEmail = currentUser.getEmail();
            Toast.makeText(getActivity(), "Usuario autenticado" + userEmail, Toast.LENGTH_SHORT).show();
            // Resto del código...
        } else {
            Log.e(TAG, "Usuario no autenticado");
            Toast.makeText(getActivity(), "Usuario no autenticado", Toast.LENGTH_SHORT).show();
        }
        // Configurar el evento click del botón para mostrar el TimePicker
        btnMostrarTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener la hora actual
                LocalTime currentTime = LocalTime.now();

                // Crear un TimePicker personalizado sin minutos y formato de 12 horas
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        getActivity(),
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
                                Toast.makeText(getActivity(), "Hora seleccionada: " + horainicio , Toast.LENGTH_SHORT).show();
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
                String medicamento = editMedicamento.getText().toString();
                String duracionStr = editDuracion.getText().toString();
                String dosisStr = editDosis.getText().toString();
                String intervaloStr = editIntervalo.getText().toString();
                // Verificar que los campos no estén vacíos
                if (!medicamento.isEmpty() && !duracionStr.isEmpty() && !dosisStr.isEmpty() && !intervaloStr.isEmpty()) {
                    // Enviar los datos a Firestore
                    //Conversiones
                    int duracion = Integer.parseInt(duracionStr);
                    double dosis = Double.parseDouble(dosisStr);
                    int intervalo = Integer.parseInt(intervaloStr);

                    enviarDatosFirestore(medicamento, duracion, dosis, intervalo);
                    scheduleNotification(intervalo * 1000); // Convertir el intervalo a milisegundos
                } else {
                    Toast.makeText(getActivity(), "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }
    private void enviarDatosFirestore(String medicamento, Integer duracion, Double dosis, Integer intervalo) {
        // Crear un objeto Map con los datos del tratamiento
        Map<String, Object> tratamiento = new HashMap<>();
        tratamiento.put("medicamento", medicamento);
        tratamiento.put("duracion", duracion);
        tratamiento.put("dosis", dosis);
        tratamiento.put("intervalo", intervalo);
        tratamiento.put("selectime", horainicio);
        tratamiento.put("usuario", userEmail);

        // Obtener una referencia a la colección "tratamientos" en Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("tratamientos")
                .add(tratamiento) // Utilizar "add" para crear un nuevo documento con un ID automático
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(getActivity(), "Tratamiento registrado correctamente", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "Error al registrar tratamiento", Toast.LENGTH_SHORT).show();
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
    private void scheduleNotification(int delayMillis) {
        // Crear una tarea única con WorkManager
        OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(MyWorker.class)
                .setInitialDelay(delayMillis, TimeUnit.MILLISECONDS)
                .build();
        // Enviar la tarea a WorkManager
        WorkManager.getInstance(requireContext()).enqueue(workRequest);
    }
}
