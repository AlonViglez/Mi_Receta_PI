package com.example.nav_drawer.viewpaciente;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

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
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

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
                // Mostrar el TimePicker al hacer clic en el botón
                timePicker.setVisibility(View.VISIBLE);
            }
        });
        // Configurar el TimePicker para mostrar solo horas de 1 a 24
        timePicker.setIs24HourView(true);
        timePicker.setDescendantFocusability(TimePicker.FOCUS_BLOCK_DESCENDANTS);
        timePicker.setFormatter(new TimePicker.Formatter() {
            @Override
            public String format(int value) {
                // Ajustar las opciones de 1 a 24
                return String.format(Locale.getDefault(), "%02d", value);
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

                } else {
                    Toast.makeText(getActivity(), "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
                }
            }
        });
        // Inflate the layout for this fragment
        return view;
    }
    private void enviarDatosFirestore(String medicamento, Integer duracion, Double dosis, Integer intervalo) {
        // Crear un objeto Map con los datos del tratamiento
        Map<String, Object> tratamiento = new HashMap<>();
        tratamiento.put("medicamento", medicamento);
        tratamiento.put("duracion", duracion);
        tratamiento.put("dosis", dosis);
        tratamiento.put("intervalo", intervalo);

        // Obtener una referencia a la colección "tratamientos" en Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("tratamientos")
                .document(userEmail) // Puedes usar el correo del usuario como identificador único
                .set(tratamiento)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getActivity(), "Datos enviados a Firestore correctamente", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "Error al enviar datos a Firestore", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "Error al enviar datos a Firestore", e);
                    }
                });
    }
}
