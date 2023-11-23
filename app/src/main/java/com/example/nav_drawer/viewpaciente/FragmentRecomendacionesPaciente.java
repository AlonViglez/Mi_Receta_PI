package com.example.nav_drawer.viewpaciente;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nav_drawer.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentRecomendacionesPaciente#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentRecomendacionesPaciente extends Fragment {

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
    String fecha, doctor, recomendacion;
    String especialidadmedica;
    LinearLayout recomendacionesContainer;

    public FragmentRecomendacionesPaciente() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentRecomendacionesPaciente.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentRecomendacionesPaciente newInstance(String param1, String param2) {
        FragmentRecomendacionesPaciente fragment = new FragmentRecomendacionesPaciente();
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
        View view = inflater.inflate(R.layout.fragment_recomendaciones_paciente, container, false);
        // Obtener una referencia al contenedor de tarjetas
        recomendacionesContainer  = view.findViewById(R.id.doctorDarRecomendacion);
        //Spinner
        Spinner spinnerOrden = view.findViewById(R.id.spinnerOrden);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.orden_recomendaciones,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerOrden.setAdapter(adapter);
        spinnerOrden.setSelection(0, false); //Desactivado default
        db = FirebaseFirestore.getInstance();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            userEmail = currentUser.getEmail();
           // Toast.makeText(getActivity(), "Usuario autenticado" + userEmail, Toast.LENGTH_SHORT).show();
        } else {
            Log.e(TAG, "Usuario no autenticado");
            Toast.makeText(getActivity(), "Usuario no autenticado", Toast.LENGTH_SHORT).show();
        }
        //Realizar una consulta para obtener las recomendaciones de todos los doctores
        db.collection("recomendaciones")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            fecha = document.getString("fecha");
                            doctor = document.getString("nombreDoctor");
                            recomendacion = document.getString("recomendacion");
                            especialidadmedica = document.getString("especialidad");

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
        spinnerOrden.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Aquí puedes realizar acciones según la selección del usuario
                String opcionSeleccionada = parentView.getItemAtPosition(position).toString();
                if (opcionSeleccionada.equals("Más reciente")) {
                    cargarRecomendacionesRecientes("fecha");
                } else if (opcionSeleccionada.equals("Más antigua")) {
                    cargarRecomendacionesAntiguas("fecha");
                }else if (opcionSeleccionada.equals("Todas")){
                    cargarRecomendacionesTodas("fecha");
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // En caso de que no se seleccione nada
            }
        });
        return view;
    }
    private void cargarRecomendacionesTodas(String campoOrden) {
        recomendacionesContainer.removeAllViews();

        //Realizar una consulta para obtener las recomendaciones de todos los doctores
        db.collection("recomendaciones")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            fecha = document.getString("fecha");
                            doctor = document.getString("nombreDoctor");
                            recomendacion = document.getString("recomendacion");
                            especialidadmedica = document.getString("especialidad");

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
    }
    private void cargarRecomendacionesRecientes(String campoOrden) {
        recomendacionesContainer.removeAllViews();

        db.collection("recomendaciones")
                .orderBy(campoOrden, Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            fecha = document.getString("fecha");
                            doctor = document.getString("nombreDoctor");
                            recomendacion = document.getString("recomendacion");
                            especialidadmedica = document.getString("especialidad");

                            // Inflar el diseño de la tarjeta personalizado
                            View cardView = getLayoutInflater().inflate(R.layout.doctor_card_recomendar, null);

                            // Configurar los márgenes
                            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT
                            );
                            layoutParams.setMargins(16, 16, 16, 16);

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
                        //Error
                    }
                });
    }
    private void cargarRecomendacionesAntiguas(String campoOrden) {
        recomendacionesContainer.removeAllViews();

        db.collection("recomendaciones")
                .orderBy(campoOrden, Query.Direction.ASCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            fecha = document.getString("fecha");
                            doctor = document.getString("nombreDoctor");
                            recomendacion = document.getString("recomendacion");
                            especialidadmedica = document.getString("especialidad");

                            // Inflar el diseño de la tarjeta personalizado
                            View cardView = getLayoutInflater().inflate(R.layout.doctor_card_recomendar, null);

                            // Configurar los márgenes
                            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT
                            );
                            layoutParams.setMargins(16, 16, 16, 16);

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
                        //Error
                    }
                });
    }
}