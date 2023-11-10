package com.example.nav_drawer.viewpaciente;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.nav_drawer.R;
import com.example.nav_drawer.viewAdmin.DetallesDoctor;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentDoctoresPaciente#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentDoctoresPaciente extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextView doctorNameTextView;
    private TextView especialidadMedicaTextView;

    public FragmentDoctoresPaciente() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentDoctoresPaciente.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentDoctoresPaciente newInstance(String param1, String param2) {
        FragmentDoctoresPaciente fragment = new FragmentDoctoresPaciente();
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
        View view = inflater.inflate(R.layout.fragment_doctores_paciente, container, false);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // Obtener una referencia al contenedor de tarjetas
        LinearLayout doctorsContainer = view.findViewById(R.id.doctorsContainerHelp);

        // Realizar una consulta para obtener los datos de los doctores
        db.collection("altadoctores")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String nombreDoctor = document.getString("nombre");
                            String especialidadMedica = document.getString("especialidad");
                            String idDoctor = document.getString("id");
                            // Inflar el diseño de la tarjeta personalizado
                            View cardView = getLayoutInflater().inflate(R.layout.doctor_card_ayuda_chat, null);

                            // Configurar los márgenes
                            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT
                            );
                            layoutParams.setMargins(16, 16, 16, 16); // Ajusta los márgenes según tus necesidades

                            // Aplicar los márgenes a la vista de la tarjeta
                            cardView.setLayoutParams(layoutParams);

                            // Encontrar las vistas dentro de la tarjeta
                            ImageView doctorIconImageView = cardView.findViewById(R.id.doctorIcon);
                            TextView doctorNameTextView = cardView.findViewById(R.id.doctorName);
                            TextView especialidadMedicaTextView = cardView.findViewById(R.id.especialidadMedica);

                            // Configurar los elementos de la tarjeta
                            doctorNameTextView.setText(nombreDoctor);
                            especialidadMedicaTextView.setText(especialidadMedica);

                            // Agregar la tarjeta al contenedor
                            doctorsContainer.addView(cardView);
                        }
                    } else {
                        // Si hubiera un error
                    }
                });
        return view;
    }
}