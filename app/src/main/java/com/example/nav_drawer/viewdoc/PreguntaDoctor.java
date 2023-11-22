package com.example.nav_drawer.viewdoc;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nav_drawer.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PreguntaDoctor#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PreguntaDoctor extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    View cardView;


    EditText respuestaedit;


    Button btnenviar;

    FirebaseFirestore db;

    String userEmail, usuariopaciente;

    String RespuestaStr;

    String idpregunta;
    String Nombredoctor, preguntapaciente, nombredoctor, nombrepaciente, fechanac;


    TextView textfecha,textNombrepaciente, pregunta,nombredoc,respdoc;
    String respuestactr;

    public PreguntaDoctor() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PreguntaDoctor.
     */
    // TODO: Rename and change types and number of parameters
    public static PreguntaDoctor newInstance(String param1, String param2) {
        PreguntaDoctor fragment = new PreguntaDoctor();
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
        View view = inflater.inflate(R.layout.fragment_pregunta_doctor, container, false);

        LinearLayout tratamientosContainer  = view.findViewById(R.id.doctorContainerPregunta);
        db = FirebaseFirestore.getInstance();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            userEmail = currentUser.getEmail();
            Toast.makeText(getActivity(), "Usuario autenticado" + userEmail, Toast.LENGTH_SHORT).show();
        } else {
            Log.e(TAG, "Usuario no autenticado");
            Toast.makeText(getActivity(), "Usuario no autenticado", Toast.LENGTH_SHORT).show();
        }
        //llamar el nombre del paciente
        db.collection("altadoctores")
                .whereEqualTo("correo", userEmail)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Nombredoctor = document.getString("nombre");
                        }
                    } else {
                        // Si hubiera un error
                    }
                });
        List<View> cardList = new ArrayList<>();
        //mostar los datos
        db.collection("preguntas")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            preguntapaciente = document.getString("pregunta");
                            nombredoctor = document.getString("nombredoctor");
                            nombrepaciente = document.getString("nombrepaciente");
                            idpregunta = document.getString("idpregunta");
                            usuariopaciente = document.getString("usuario");
                            //mostrar fecha
                            fechanac = document.getString("fecha");
                            //Nombre ya lo tengo//

                            // Inflar el diseño de la tarjeta personalizado
                            cardView = getLayoutInflater().inflate(R.layout.doctor_card_pregunta, null);

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

                            // Generar identificadores únicos para EditText y Button
                            int editTextId = View.generateViewId();
                            respuestaedit = cardView.findViewById(R.id.editresp);
                            respuestaedit.setId(editTextId);

                            int buttonId = View.generateViewId();
                            btnenviar = cardView.findViewById(R.id.btnenviar);
                            btnenviar.setId(buttonId);
                            // Configurar los elementos de la tarjeta
                            textfecha.setText(fechanac);
                            textNombrepaciente.setText(nombrepaciente);
                            pregunta.setText(preguntapaciente);
                            nombredoc.setText(nombredoctor);

                            cardView.setTag(idpregunta);
                            cardList.add(cardView);
                            // Agregar la tarjeta al contenedor
                            tratamientosContainer.addView(cardView);
                            String idPreguntaAsociado = (String) cardView.getTag();

                            EditText respuestaEdit = cardView.findViewById(editTextId);
                            // Verificar si el EditText está vacío y es en el tag correspondiente
                            if (respuestaEdit.getText().toString().trim().isEmpty()) {
                                Toast.makeText(getActivity(), "Ingrese texto en la tarjeta con ID de pregunta: " + idPreguntaAsociado, Toast.LENGTH_SHORT).show();
                            }
                            // Configurar el OnClickListener para el Button de esta tarjeta
                            Button enviarButton = cardView.findViewById(buttonId);
                            enviarButton.setOnClickListener(v -> {
                                respuestactr = respuestaEdit.getText().toString().trim();
                                if (respuestactr.isEmpty()) {
                                    Toast.makeText(getActivity(), "Ingrese texto en la tarjeta con ID de pregunta: " + idPreguntaAsociado, Toast.LENGTH_SHORT).show();
                                } else {
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
                                        Map<String, Object> dataContestada = new HashMap<>();
                                        dataContestada.put("respuestadoc", respuestactr);
                                        dataContestada.put("nombredoctor", Nombredoctor);
                                        dataContestada.put("useremail", userEmail);
                                        dataContestada.put("pregunta", preguntapaciente);
                                        dataContestada.put("nombrepaciente", nombrepaciente);
                                        dataContestada.put("idpregunta", idpregunta);
                                        dataContestada.put("usuario", usuariopaciente);
                                        db.collection("preguntascontestadas")
                                                .document(idpregunta)
                                                .set(dataContestada)
                                                .addOnSuccessListener(documentReference -> {
                                                    // La respuesta se ha agregado a la nueva colección

                                                    // Eliminar la pregunta contestada de la colección original
                                                    db.collection("preguntas")
                                                            .document(idpregunta)
                                                            .delete()
                                                            .addOnSuccessListener(aVoid -> {
                                                            })
                                                            .addOnFailureListener(e -> {
                                                            });
                                                })
                                                .addOnFailureListener(e -> {
                                                });
                                        alertDialog.dismiss();
                                    });
                                    alertDialog.show();
                                }
                            });
                        }
                    } else {
                        // Si hubiera un error
                    }
                    /*for (View cardView : cardList) {
                        respuestaedit = cardView.findViewById(R.id.editresp);
                        btnenviar = cardView.findViewById(R.id.btnenviar);
                        String idPreguntaAsociado = (String) cardView.getTag();
                        //boton al enviar la respuesta
                        int cardId = cardView.getId();
                        btnenviar.setOnClickListener(v -> {
                            respuestactr = respuestaedit.getText().toString().trim();
                            if(respuestactr.isEmpty()){
                                Toast.makeText(getActivity(), "Ingrese texto", Toast.LENGTH_SHORT).show();
                            }else{
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
                                    Map<String, Object> dataContestada = new HashMap<>();
                                    dataContestada.put("respuestadoc", respuestactr);
                                    dataContestada.put("nombredoctor", Nombredoctor);
                                    dataContestada.put("useremail", userEmail);
                                    dataContestada.put("pregunta", preguntapaciente);
                                    dataContestada.put("nombrepaciente", nombrepaciente);
                                    dataContestada.put("idpregunta", idpregunta);
                                    dataContestada.put("usuario", usuariopaciente);
                                    db.collection("preguntascontestadas")
                                            .document(idpregunta)
                                            .set(dataContestada)
                                            .addOnSuccessListener(documentReference -> {
                                                // La respuesta se ha agregado a la nueva colección

                                                // Eliminar la pregunta contestada de la colección original
                                                db.collection("preguntas")
                                                        .document(idpregunta)
                                                        .delete()
                                                        .addOnSuccessListener(aVoid -> {
                                                        })
                                                        .addOnFailureListener(e -> {
                                                        });
                                            })
                                            .addOnFailureListener(e -> {
                                            });
                                    alertDialog.dismiss();
                                });
                                alertDialog.show();
                            }
                        });
                    }*/
                });
        return view;
    }

}