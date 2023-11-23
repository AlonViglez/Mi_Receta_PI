package com.example.nav_drawer.viewpaciente;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nav_drawer.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PreguntaPaciente#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PreguntaPaciente extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    FirebaseFirestore db;

    String userEmail;

    String preguntapaciente,nombredoctor,fechanac ;

    long timestamp;

    Date fecha;

    SimpleDateFormat sdf;

    String fechaFormateada;

    String respuestadoctor;

    String Nombrepaciente, nombrepaciente;


    // tarjeta

   TextView textfecha,textNombrepaciente,pregunta, nombredoc, respdoc;


   Button btnnuevotratamiento;


   EditText preguntaedit;


   String PreguntaStr;
    public PreguntaPaciente() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PreguntaPaciente.
     */
    // TODO: Rename and change types and number of parameters
    public static PreguntaPaciente newInstance(String param1, String param2) {
        PreguntaPaciente fragment = new PreguntaPaciente();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pregunta_paciente, container, false);
        LinearLayout tratamientosContainer  = view.findViewById(R.id.pacienteContainerPregunta);
        preguntaedit = view.findViewById(R.id.pregunta);
        btnnuevotratamiento = view.findViewById(R.id.btnrecomendarenviar);
        Spinner spinnerFiltro = view.findViewById(R.id.spinnerFiltro);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.opciones_filtro, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFiltro.setAdapter(adapter);
        spinnerFiltro.setSelection(0, false);
        db = FirebaseFirestore.getInstance();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            userEmail = currentUser.getEmail();
           // Toast.makeText(getActivity(), "Usuario autenticado" + userEmail, Toast.LENGTH_SHORT).show();
        } else {
            Log.e(TAG, "Usuario no autenticado");
            Toast.makeText(getActivity(), "Usuario no autenticado", Toast.LENGTH_SHORT).show();
        }
        //llamar el nombre del paciente
        db.collection("users")
                .whereEqualTo("correo", userEmail)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Nombrepaciente = document.getString("nombre");
                        }
                    } else {
                        // Si hubiera un error
                    }
                });
        //Realizar una consulta para obtener las preguntas del usuario específico
        db.collection("preguntas")
                .whereEqualTo("usuario", userEmail)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            preguntapaciente = document.getString("pregunta");//nombredoctor = document.getString("nombredoctor");
                            nombrepaciente = document.getString("nombrepaciente");
                            //respuestadoctor = document.getString("respuestadoc");
                            //mostrar fecha
                            fechanac =  document.getString("fecha");
                            //Nombre ya lo tengo//

                            // Inflar el diseño de la tarjeta personalizado
                            View cardView = getLayoutInflater().inflate(R.layout.paciente_card_pregunta_sin_responder, null);

                            // Configurar los márgenes
                            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT
                            );
                            layoutParams.setMargins(16, 16, 16, 16); // Ajusta los márgenes según tus necesidades

                            // Aplicar los márgenes a la vista de la tarjeta
                            cardView.setLayoutParams(layoutParams);

                            // Encontrar las vistas dentro de la tarjeta
                            textfecha = cardView.findViewById(R.id.fecha);
                            textNombrepaciente = cardView.findViewById(R.id.textNombrepaciente);
                            pregunta = cardView.findViewById(R.id.textpregunta);


                            // Configurar los elementos de la tarjeta
                            textfecha.setText(fechanac);
                            textNombrepaciente.setText(nombrepaciente);
                            pregunta.setText(preguntapaciente);

                            // Agregar la tarjeta al contenedor
                            tratamientosContainer.addView(cardView);
                        }
                    } else {
                        // Si hubiera un error
                    }
                });
        spinnerFiltro.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String opcionSeleccionada = parentView.getItemAtPosition(position).toString();
                // Actualizar la consulta según la opción seleccionada
                if (opcionSeleccionada.equals("Sin responder")) {
                    tratamientosContainer.removeAllViews();
                    //Realizar una consulta para obtener las preguntas del usuario específico
                    db.collection("preguntas")
                            .whereEqualTo("usuario", userEmail)
                            .get()
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        preguntapaciente = document.getString("pregunta");
                                       // nombredoctor = document.getString("nombredoctor");
                                        nombrepaciente = document.getString("nombrepaciente");
                                        //respuestadoctor = document.getString("respuestadoc");
                                        //mostrar fecha
                                        fechanac =  document.getString("fecha");
                                        //Nombre ya lo tengo//

                                        // Inflar el diseño de la tarjeta personalizado
                                        View cardView = getLayoutInflater().inflate(R.layout.paciente_card_pregunta_sin_responder, null);

                                        // Configurar los márgenes
                                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                                LinearLayout.LayoutParams.MATCH_PARENT,
                                                LinearLayout.LayoutParams.WRAP_CONTENT
                                        );
                                        layoutParams.setMargins(16, 16, 16, 16); // Ajusta los márgenes según tus necesidades

                                        // Aplicar los márgenes a la vista de la tarjeta
                                        cardView.setLayoutParams(layoutParams);

                                        // Encontrar las vistas dentro de la tarjeta
                                        textfecha = cardView.findViewById(R.id.fecha);
                                        textNombrepaciente = cardView.findViewById(R.id.textNombrepaciente);
                                        pregunta = cardView.findViewById(R.id.textpregunta);
                                        //nombredoc = cardView.findViewById(R.id.textnombredoctor);
                                        //respdoc = cardView.findViewById(R.id.textrespuesta);

                                        // Configurar los elementos de la tarjeta
                                        textfecha.setText(fechanac);
                                        textNombrepaciente.setText(nombrepaciente);
                                        pregunta.setText(preguntapaciente);

                                        // Agregar la tarjeta al contenedor
                                        tratamientosContainer.addView(cardView);
                                    }
                                } else {
                                    // Si hubiera un error
                                }
                            });
                } else if (opcionSeleccionada.equals("Respondidas")) {
                    tratamientosContainer.removeAllViews();
                    //Realizar una consulta para obtener las preguntas del usuario específico
                    db.collection("preguntascontestadas")
                            .whereEqualTo("usuario", userEmail)
                            .get()
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        preguntapaciente = document.getString("pregunta");
                                        nombredoctor = document.getString("nombredoctor");
                                        nombrepaciente = document.getString("nombrepaciente");
                                        respuestadoctor = document.getString("respuestadoc");
                                        //mostrar fecha
                                        fechanac =  document.getString("fecha");
                                        //Nombre ya lo tengo//

                                        // Inflar el diseño de la tarjeta personalizado
                                        View cardView = getLayoutInflater().inflate(R.layout.paciente_card_pregunta, null);

                                        // Configurar los márgenes
                                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                                LinearLayout.LayoutParams.MATCH_PARENT,
                                                LinearLayout.LayoutParams.WRAP_CONTENT
                                        );
                                        layoutParams.setMargins(16, 16, 16, 16); // Ajusta los márgenes según tus necesidades

                                        // Aplicar los márgenes a la vista de la tarjeta
                                        cardView.setLayoutParams(layoutParams);

                                        // Encontrar las vistas dentro de la tarjeta
                                        textfecha = cardView.findViewById(R.id.fecha);
                                        textNombrepaciente = cardView.findViewById(R.id.textNombrepaciente);
                                        pregunta = cardView.findViewById(R.id.textpregunta);
                                        nombredoc = cardView.findViewById(R.id.textnombredoctor);
                                        respdoc = cardView.findViewById(R.id.textrespuesta);

                                        // Configurar los elementos de la tarjeta
                                        textfecha.setText(fechanac);
                                        textNombrepaciente.setText(nombrepaciente);
                                        pregunta.setText(preguntapaciente);
                                        nombredoc.setText(nombredoctor);
                                        respdoc.setText(respuestadoctor);

                                        // Agregar la tarjeta al contenedor
                                        tratamientosContainer.addView(cardView);
                                    }
                                } else {
                                    // Si hubiera un error
                                }
                            });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // No se realiza ninguna acción cuando no hay nada seleccionado
            }
        });
        btnnuevotratamiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreguntaStr = preguntaedit.getText().toString().trim();
                if(PreguntaStr.equals("")){
                    Toast.makeText(getActivity(), "Ingrese la recomendacion", Toast.LENGTH_SHORT).show();
                }else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    View dialogView = getLayoutInflater().inflate(R.layout.dialog_responder_doctor, null);
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
                        recomendacionData.put("idpregunta",null);
                        recomendacionData.put("fecha", fechaActual);
                        recomendacionData.put("nombrepaciente", Nombrepaciente);
                        recomendacionData.put("pregunta", PreguntaStr);
                        recomendacionData.put("usuario", userEmail);
                        // Agregar a la tabla
                        db.collection("preguntas")
                                .add(recomendacionData)
                                .addOnSuccessListener(documentReference -> {
                                    String idPregunta = documentReference.getId();
                                    db.collection("preguntas")
                                            .document(documentReference.getId())
                                            .update("idpregunta", idPregunta)
                                            .addOnSuccessListener(aVoid -> {
                                            })
                                            .addOnFailureListener(e -> {
                                            });
                                })
                                .addOnFailureListener(e -> {
                                });
                        preguntaedit.setText("");
                        alertDialog.dismiss();
                    });
                    alertDialog.show();
                }
            }
        });
        return view;
    }
}