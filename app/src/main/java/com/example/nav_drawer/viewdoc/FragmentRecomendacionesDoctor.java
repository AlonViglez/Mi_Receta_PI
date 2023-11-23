package com.example.nav_drawer.viewdoc;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nav_drawer.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentRecomendacionesDoctor#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentRecomendacionesDoctor extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    FirebaseFirestore db;
    String userEmail;
    TextView textFecha,textDoctor,textRecomendacion, txtEspecilidad;
    String fecha, doctor, recomendacion, recomendacionStr;
    String nombreDoctor;
    Button btnenviar;
    EditText editRecomendacion;
    String especialidadmedica;

    public FragmentRecomendacionesDoctor() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentRecomendacionesDoctor.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentRecomendacionesDoctor newInstance(String param1, String param2) {
        FragmentRecomendacionesDoctor fragment = new FragmentRecomendacionesDoctor();
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
        View view = inflater.inflate(R.layout.fragment_recomendaciones_doctor, container, false);
        btnenviar = view.findViewById(R.id.btnrecomendarenviar);
        editRecomendacion = view.findViewById(R.id.editRecomendar);
        // Obtener una referencia al contenedor de tarjetas
        LinearLayout recomendacionesContainer  = view.findViewById(R.id.doctorDarRecomendacion);
        db = FirebaseFirestore.getInstance();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            userEmail = currentUser.getEmail();
            Toast.makeText(getActivity(), "Usuario autenticado" + userEmail, Toast.LENGTH_SHORT).show();
        } else {
            Log.e(TAG, "Usuario no autenticado");
            Toast.makeText(getActivity(), "Usuario no autenticado", Toast.LENGTH_SHORT).show();
        }

        //Consultar el nombre del doctor
        db.collection("altadoctores")
                .whereEqualTo("correo", userEmail)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            nombreDoctor = document.getString("nombre");
                            especialidadmedica = document.getString("especialidad");
                            Toast.makeText(getActivity(), "Nombre: " + nombreDoctor, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // Si hubiera un error
                    }
                });
        //Realizar una consulta para obtener las recomendaciones del doctor
        db.collection("recomendaciones")
                .whereEqualTo("usuario", userEmail)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            fecha = document.getString("fecha");
                            doctor = document.getString("nombreDoctor");
                            recomendacion = document.getString("recomendacion");

                            // Inflar el diseño de la tarjeta personalizado
                            View cardView = getLayoutInflater().inflate(R.layout.doctor_card_recomendar, null);

                            // Configurar los márgenes
                            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT
                            );
                            layoutParams.setMargins(16, 16, 16, 16); // Ajusta los márgenes según tus necesidades

                            // Aplicar los márgenes a la vista de la tarjeta
                            cardView.setLayoutParams(layoutParams);

                            // Encontrar las vistas dentro de la tarjeta
                            textFecha = cardView.findViewById(R.id.textFechaRecomendacion);
                            textDoctor = cardView.findViewById(R.id.textDoctorRecomendacion);
                            textRecomendacion = cardView.findViewById(R.id.textRecomendacion);
                            txtEspecilidad = cardView.findViewById(R.id.textEspecialidadM);

                            // Configurar los elementos de la tarjeta
                            textFecha.setText(fecha);
                            textDoctor.setText(doctor);
                            textRecomendacion.setText(recomendacion);
                            txtEspecilidad.setText(especialidadmedica);

                            // Agregar la tarjeta al contenedor
                            recomendacionesContainer.addView(cardView);
                        }
                    } else {
                        // Si hubiera un error
                    }
                });
        btnenviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recomendacionStr = editRecomendacion.getText().toString().trim();
                if(recomendacionStr.equals("")){
                    Toast.makeText(getActivity(), "Ingrese la recomendacion", Toast.LENGTH_SHORT).show();
                }else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    View dialogView = getLayoutInflater().inflate(R.layout.dialog_recomendar_doctor, null);
                    builder.setView(dialogView);
                    Button btnAceptar = dialogView.findViewById(R.id.btnAceptarResponseDialog);
                    Button btnCancelar = dialogView.findViewById(R.id.btnCancelarResponse);
                    AlertDialog alertDialog = builder.create();
                    btnCancelar.setOnClickListener(v1 -> {
                        alertDialog.dismiss();
                    });
                    btnAceptar.setOnClickListener(v2 -> {
                        Date currentDate = Calendar.getInstance().getTime();
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                        String fechaActual = dateFormat.format(currentDate);
                        // Crear un nuevo documento
                        Map<String, Object> recomendacionData = new HashMap<>();
                        recomendacionData.put("fecha", fechaActual);
                        recomendacionData.put("nombreDoctor", nombreDoctor);
                        recomendacionData.put("recomendacion", recomendacionStr);
                        recomendacionData.put("usuario", userEmail);
                        recomendacionData.put("especialidad", especialidadmedica);

                        // Agregar a la tabla
                        db.collection("recomendaciones")
                                .add(recomendacionData)
                                .addOnSuccessListener(documentReference -> {
                                    Toast.makeText(getActivity(), "Recomendación enviada con éxito", Toast.LENGTH_SHORT).show();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(getActivity(), "Error al enviar la recomendación", Toast.LENGTH_SHORT).show();
                                });
                        alertDialog.dismiss();
                    });
                    alertDialog.show();
                }
            }
        });
        return view;
    }
}